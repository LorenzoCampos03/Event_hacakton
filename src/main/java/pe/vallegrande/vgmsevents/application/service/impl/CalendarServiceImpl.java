package pe.vallegrande.vgmsevents.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pe.vallegrande.vgmsevents.application.service.CalendarService;
import pe.vallegrande.vgmsevents.domain.model.AcademicCalendar;
import pe.vallegrande.vgmsevents.domain.model.EventCalendar;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.AcademicCalendarRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.EventCalendarRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.ImportCalendarsRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.response.ImportResult;
import pe.vallegrande.vgmsevents.infrastructure.repository.AcademicCalendarRepository;
import pe.vallegrande.vgmsevents.infrastructure.repository.EventCalendarRepository;
import pe.vallegrande.vgmsevents.infrastructure.repository.EventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final AcademicCalendarRepository academicCalendarRepository;
    private final EventCalendarRepository eventCalendarRepository;
    private final EventRepository eventRepository;
    private final TransactionalOperator transactionalOperator;

    // =========================
    // IMPORTACIÓN TRANSACCIONAL
    // =========================
    @Override
    public Mono<ImportResult> importAll(ImportCalendarsRequest request) {

        List<AcademicCalendarRequest> calendars = request.getAcademic_calendar() == null
                ? List.of() : request.getAcademic_calendar();
        List<EventCalendarRequest> events = request.getEvent_calendar() == null
                ? List.of() : request.getEvent_calendar();

        return Flux.fromIterable(calendars)
                .flatMap(this::validateAndSaveCalendar)
                .collectList()
                .flatMap(savedCalendars ->
                        Flux.fromIterable(events)
                                .flatMap(this::validateAndSaveEventCalendar)
                                .collectList()
                                .map(savedEventCalendars ->
                                        new ImportResult(savedCalendars, savedEventCalendars))
                )
                .doOnSubscribe(s -> log.info("Iniciando importación transaccional de calendarios y eventos..."))
                .doOnSuccess(r -> log.info("Importación completada correctamente: {} calendarios, {} vínculos.",
                        r.getAcademicCalendars().size(), r.getEventCalendars().size()))
                .doOnError(e -> log.error("Error durante la importación: {}", e.getMessage()))
                .as(transactionalOperator::transactional); // <--- rollback si ocurre un error
    }

    // =========================
    // MÉTODOS CRUD
    // =========================

    @Override
    public Mono<AcademicCalendar> createCalendar(AcademicCalendarRequest request) {
        return validateAndSaveCalendar(request);
    }

    @Override
    public Mono<Void> addEventsToCalendar(Integer calendarId, List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Mono.empty();
        }

        return Flux.fromIterable(eventIds)
                .flatMap(eventId -> {
                    EventCalendarRequest req = new EventCalendarRequest();
                    req.setCalendarId(calendarId);
                    req.setEventId(eventId);
                    return validateAndSaveEventCalendar(req);
                })
                .then()
                .as(transactionalOperator::transactional);
    }

    @Override
    public Flux<AcademicCalendar> listCalendars() {
        return academicCalendarRepository.findAll();
    }

    @Override
    public Mono<AcademicCalendar> getCalendarById(Integer id) {
        return academicCalendarRepository.findById(id);
    }

    @Override
    public Flux<AcademicCalendar> listByInstitution(String institutionId) {
        return academicCalendarRepository.findByInstitutionId(institutionId);
    }

    @Override
    public Flux<EventCalendar> listEventCalendarsByCalendarId(Integer calendarId) {
        return eventCalendarRepository.findByCalendarId(calendarId);
    }

    // =========================
    // VALIDACIONES PRIVADAS
    // =========================

    private Mono<AcademicCalendar> validateAndSaveCalendar(AcademicCalendarRequest req) {

        if (req.getAcademicYear() == null || req.getAcademicYear() < 2000 || req.getAcademicYear() > 2100) {
            return Mono.error(new IllegalArgumentException("Año académico inválido: " + req.getAcademicYear()));
        }

        if (req.getStartDate() == null || req.getEndDate() == null ||
                !req.getEndDate().isAfter(req.getStartDate())) {
            return Mono.error(new IllegalArgumentException("Fechas inválidas: la fecha de fin debe ser posterior a la de inicio."));
        }

        return academicCalendarRepository
                .existsByInstitutionIdAndAcademicYear(req.getInstitutionId(), req.getAcademicYear())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("El calendario académico para la institución "
                                + req.getInstitutionId() + " y año " + req.getAcademicYear() + " ya existe."));
                    }

                    AcademicCalendar cal = new AcademicCalendar();
                    cal.setCalendarId(req.getCalendarId());
                    cal.setInstitutionId(req.getInstitutionId());
                    cal.setAcademicYear(req.getAcademicYear());
                    cal.setStartDate(req.getStartDate());
                    cal.setEndDate(req.getEndDate());
                    cal.setCreatedAt(req.getCreatedAt() == null ? LocalDateTime.now() : req.getCreatedAt());
                    cal.setUpdatedAt(req.getUpdatedAt() == null ? LocalDateTime.now() : req.getUpdatedAt());

                    log.info("Guardando calendario académico para institución {} - año {}",
                            req.getInstitutionId(), req.getAcademicYear());

                    return academicCalendarRepository.save(cal);
                });
    }

    private Mono<EventCalendar> validateAndSaveEventCalendar(EventCalendarRequest req) {

        if (req.getCalendarId() == null || req.getEventId() == null) {
            return Mono.error(new IllegalArgumentException("calendar_id y event_id son obligatorios."));
        }

        Mono<Boolean> calendarExists = academicCalendarRepository.findById(req.getCalendarId()).hasElement();
        Mono<Boolean> eventExists = eventRepository.findById(req.getEventId()).hasElement();

        return Mono.zip(calendarExists, eventExists)
                .flatMap(tuple -> {
                    boolean calExists = tuple.getT1();
                    boolean evExists = tuple.getT2();

                    if (!calExists) {
                        return Mono.error(new RuntimeException("No se encontró el calendar_id: " + req.getCalendarId()));
                    }
                    if (!evExists) {
                        return Mono.error(new RuntimeException("No se encontró el event_id: " + req.getEventId()));
                    }

                    return eventCalendarRepository
                            .existsByCalendarIdAndEventId(req.getCalendarId(), req.getEventId())
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new RuntimeException("El evento ya está vinculado al calendario: "
                                            + "calendar_id=" + req.getCalendarId() + ", event_id=" + req.getEventId()));
                                }

                                EventCalendar ec = new EventCalendar();
                                ec.setEventCalendarId(req.getEventCalendarId());
                                ec.setCalendarId(req.getCalendarId());
                                ec.setEventId(req.getEventId());
                                ec.setCreatedAt(LocalDateTime.now());

                                log.info("Vinculando evento {} con calendario {}", req.getEventId(), req.getCalendarId());
                                return eventCalendarRepository.save(ec);
                            });
                });
    }
}
