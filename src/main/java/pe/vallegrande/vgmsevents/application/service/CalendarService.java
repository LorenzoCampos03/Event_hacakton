package pe.vallegrande.vgmsevents.application.service;

import pe.vallegrande.vgmsevents.domain.model.AcademicCalendar;
import pe.vallegrande.vgmsevents.domain.model.EventCalendar;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.AcademicCalendarRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.ImportCalendarsRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.EventCalendarRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.response.ImportResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CalendarService {

    /**
     * Import calendars and event-calendar links in a single transactional operation.
     * Returns an ImportResult with created records.
     */
    Mono<ImportResult> importAll(ImportCalendarsRequest request);

    /** Create a single academic calendar. */
    Mono<AcademicCalendar> createCalendar(AcademicCalendarRequest request);

    /** Associate a list of event ids to a calendar in a transactional operation. */
    Mono<Void> addEventsToCalendar(Integer calendarId, List<Long> eventIds);

    /** List all calendars */
    Flux<AcademicCalendar> listCalendars();

    /** Get calendar by id */
    Mono<AcademicCalendar> getCalendarById(Integer id);

    /** List calendars by institution */
    Flux<AcademicCalendar> listByInstitution(String  institutionId);

    /** List event_calendar entries for a calendar */
    Flux<EventCalendar> listEventCalendarsByCalendarId(Integer calendarId);
}
