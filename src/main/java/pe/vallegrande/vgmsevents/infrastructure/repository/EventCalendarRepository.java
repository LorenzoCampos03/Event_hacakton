package pe.vallegrande.vgmsevents.infrastructure.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.vallegrande.vgmsevents.domain.model.EventCalendar;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EventCalendarRepository extends ReactiveCrudRepository<EventCalendar, Integer> {

    Flux<EventCalendar> findByCalendarId(Integer calendarId);

    Flux<EventCalendar> findByEventId(Long eventId);

    Mono<Boolean> existsByCalendarIdAndEventId(Integer calendarId, Long eventId);
}
