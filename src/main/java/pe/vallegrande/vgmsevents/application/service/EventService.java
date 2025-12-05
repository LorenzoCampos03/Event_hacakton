package pe.vallegrande.vgmsevents.application.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.UUID;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.EventCreateRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.response.EventResponse;

public interface EventService {
    
    Flux<EventResponse> listActiveEvents();
    
    Flux<EventResponse> listInactiveEvents();
    
    Mono<EventResponse> getEventById(Long id);
    
    Mono<EventResponse> addEvent(EventCreateRequest request);
    
    Mono<EventResponse> updateEvent(Long id, EventCreateRequest request);
    
    Mono<Void> logicalDeleteEvent(Long id);
    
    Mono<Void> restoreEvent(Long id);
    
    
}
