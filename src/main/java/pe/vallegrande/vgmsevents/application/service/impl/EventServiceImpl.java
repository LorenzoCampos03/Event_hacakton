package pe.vallegrande.vgmsevents.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.vallegrande.vgmsevents.application.service.EventService;
import pe.vallegrande.vgmsevents.application.service.InstitutionService;
import pe.vallegrande.vgmsevents.domain.model.Event;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.EventCreateRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.response.EventResponse;
import pe.vallegrande.vgmsevents.infrastructure.repository.EventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InstitutionService institutionService;

    @Override
    public Flux<EventResponse> listActiveEvents() {
        return eventRepository.findAllActive()
                .flatMap(event -> institutionService.getInstitutionById(event.getInstitutionId())
                        .map(institution -> {
                            EventResponse response = new EventResponse(event);
                            if (institution.getInstitutionInformation() != null &&
                                    institution.getInstitutionInformation().getInstitutionName() != null) {
                                response.setInstitutionName(
                                        institution.getInstitutionInformation().getInstitutionName());
                            } else {
                                response.setInstitutionName("Desconocida");
                            }
                            return response;
                        })
                        .onErrorResume(e -> {
                            // En caso de que falle la llamada al microservicio
                            EventResponse fallback = new EventResponse(event);
                            fallback.setInstitutionName("Desconocida");
                            return Mono.just(fallback);
                        }));
    }

    @Override
    public Flux<EventResponse> listInactiveEvents() {
        return eventRepository.findAllInactive()
                .flatMap(event -> institutionService.getInstitutionById(event.getInstitutionId())
                        .map(institution -> {
                            EventResponse response = new EventResponse(event);
                            if (institution.getInstitutionInformation() != null &&
                                    institution.getInstitutionInformation().getInstitutionName() != null) {
                                response.setInstitutionName(
                                        institution.getInstitutionInformation().getInstitutionName());
                            } else {
                                response.setInstitutionName("Desconocida");
                            }
                            return response;
                        })
                        .onErrorResume(e -> {
                            EventResponse fallback = new EventResponse(event);
                            fallback.setInstitutionName("Desconocida");
                            return Mono.just(fallback);
                        }));
    }

    @Override
    public Mono<EventResponse> getEventById(Long id) {
        return eventRepository.findById(id)
                .flatMap(event -> institutionService.getInstitutionById(event.getInstitutionId())
                        .map(inst -> {
                            EventResponse response = new EventResponse(event);
                            if (inst.getInstitutionInformation() != null &&
                                    inst.getInstitutionInformation().getInstitutionName() != null) {
                                response.setInstitutionName(inst.getInstitutionInformation().getInstitutionName());
                            } else {
                                response.setInstitutionName("Desconocida");
                            }
                            return response;
                        })
                        .onErrorReturn(new EventResponse(event)) // fallback si falla el microservicio
                );
    }

    @Override
    public Mono<EventResponse> addEvent(EventCreateRequest request) {
        if (request.getInstitutionId() == null) {
            return Mono.error(new IllegalArgumentException("El institution_id no puede ser nulo"));
        }

        System.out.println("DEBUG: Inicio addEvent con institutionId = " + request.getInstitutionId());

        return institutionService.getInstitutionById(request.getInstitutionId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La institución no existe")))
                .flatMap(institution -> {
                    Event event = new Event();
                    event.setEventId(null);
                    event.setInstitutionId(request.getInstitutionId());
                    event.setTitle(request.getTitle());
                    event.setDescription(request.getDescription());
                    event.setStartDate(request.getStartDate());
                    event.setEndDate(request.getEndDate());
                    event.setEventType(request.getEventType());
                    event.setIsHoliday(request.getIsHoliday() != null ? request.getIsHoliday() : false);
                    event.setIsRecurring(request.getIsRecurring() != null ? request.getIsRecurring() : false);
                    event.setIsNational(request.getIsNational() != null ? request.getIsNational() : false);
                    event.setAffectsClasses(request.getAffectsClasses() != null ? request.getAffectsClasses() : false);
                    event.setCreatedBy(request.getCreatedBy());
                    event.setStatus("ACTIVE");
                    event.setCreatedAt(LocalDateTime.now());
                    event.setUpdatedAt(LocalDateTime.now());

                    System.out.println("DEBUG: Guardando evento: " + event.getTitle());

                    return eventRepository.save(event)
                            .flatMap(savedEvent -> institutionService.getInstitutionById(savedEvent.getInstitutionId())
                                    .map(inst -> {
                                        EventResponse response = new EventResponse(savedEvent);
                                        if (inst.getInstitutionInformation() != null &&
                                                inst.getInstitutionInformation().getInstitutionName() != null) {
                                            response.setInstitutionName(
                                                    inst.getInstitutionInformation().getInstitutionName());
                                            System.out.println("DEBUG: Institución obtenida = "
                                                    + inst.getInstitutionInformation().getInstitutionName());
                                        } else {
                                            System.out.println(
                                                    "WARNING: institutionInformation es null para institutionId = "
                                                            + savedEvent.getInstitutionId());
                                            response.setInstitutionName("Desconocida");
                                        }
                                        return response;
                                    })
                                    .onErrorResume(e -> {
                                        System.out.println("ERROR: No se pudo obtener institución: " + e.getMessage());
                                        return Mono.just(new EventResponse(savedEvent));
                                    }));
                });
    }

    @Override
    public Mono<EventResponse> updateEvent(Long id, EventCreateRequest request) {
        return eventRepository.findByIdAndActive(id)
                .switchIfEmpty(Mono.error(new RuntimeException("El evento no existe o está inactivo")))
                .flatMap(existingEvent -> {
                    Mono<Event> validationMono;
                    if (request.getInstitutionId() != null) {
                        validationMono = institutionService.getInstitutionById(request.getInstitutionId())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("La institución no existe")))
                                .map(inst -> {
                                    existingEvent.setInstitutionId(request.getInstitutionId());
                                    return existingEvent;
                                });
                    } else {
                        validationMono = Mono.just(existingEvent);
                    }

                    return validationMono.flatMap(event -> {
                        if (request.getTitle() != null)
                            event.setTitle(request.getTitle());
                        if (request.getDescription() != null)
                            event.setDescription(request.getDescription());
                        if (request.getStartDate() != null)
                            event.setStartDate(request.getStartDate());
                        if (request.getEndDate() != null)
                            event.setEndDate(request.getEndDate());
                        if (request.getEventType() != null)
                            event.setEventType(request.getEventType());
                        if (request.getIsHoliday() != null)
                            event.setIsHoliday(request.getIsHoliday());
                        if (request.getIsRecurring() != null)
                            event.setIsRecurring(request.getIsRecurring());
                        if (request.getIsNational() != null)
                            event.setIsNational(request.getIsNational());
                        if (request.getAffectsClasses() != null)
                            event.setAffectsClasses(request.getAffectsClasses());
                        event.setUpdatedAt(LocalDateTime.now());

                        return eventRepository.save(event)
                                .flatMap(savedEvent -> institutionService
                                        .getInstitutionById(savedEvent.getInstitutionId())
                                        .map(inst -> {
                                            EventResponse response = new EventResponse(savedEvent);
                                            if (inst.getInstitutionInformation() != null &&
                                                    inst.getInstitutionInformation().getInstitutionName() != null) {
                                                response.setInstitutionName(
                                                        inst.getInstitutionInformation().getInstitutionName());
                                            } else {
                                                response.setInstitutionName("Desconocida");
                                            }
                                            return response;
                                        })
                                        .onErrorReturn(new EventResponse(savedEvent)));
                    });
                });
    }

    @Override
    public Mono<Void> logicalDeleteEvent(Long id) {
        return eventRepository.findByIdAndActive(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Event not found or already inactive")))
                .flatMap(event -> {
                    event.setStatus("INACTIVE");
                    event.setUpdatedAt(LocalDateTime.now());
                    return eventRepository.save(event);
                })
                .then();
    }

    @Override
    public Mono<Void> restoreEvent(Long id) {
        return eventRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Event not found")))
                .flatMap(event -> {
                    if ("ACTIVE".equals(event.getStatus())) {
                        return Mono.error(new RuntimeException("Event is already active"));
                    }
                    event.setStatus("ACTIVE");
                    event.setUpdatedAt(LocalDateTime.now());
                    return eventRepository.save(event).then();
                });
    }
}
