package pe.vallegrande.vgmsevents.infrastructure.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import pe.vallegrande.vgmsevents.domain.model.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EventRepository extends ReactiveCrudRepository<Event, Long> {
    
    @Query("SELECT * FROM event WHERE status = 'ACTIVE' ORDER BY start_date DESC")
    Flux<Event> findAllActive();
    
    @Query("SELECT * FROM event WHERE status = 'INACTIVE' ORDER BY start_date DESC")
    Flux<Event> findAllInactive();
    
    @Query("SELECT * FROM event WHERE event_id = :id AND status = 'ACTIVE'")
    Mono<Event> findByIdAndActive(@Param("id") Long id);
}
