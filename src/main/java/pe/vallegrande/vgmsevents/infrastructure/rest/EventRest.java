package pe.vallegrande.vgmsevents.infrastructure.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.vallegrande.vgmsevents.application.service.EventService;
import pe.vallegrande.vgmsevents.infrastructure.dto.ApiResponse;
import pe.vallegrande.vgmsevents.infrastructure.dto.ErrorResponse;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.EventCreateRequest;
import pe.vallegrande.vgmsevents.infrastructure.dto.response.EventResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
public class EventRest {

    @Autowired
    private EventService eventService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listActiveEvents() {
        return eventService.listActiveEvents()
                .collectList()
                .map(list -> ResponseEntity.ok().body((Object) ApiResponse.success("Active events retrieved", list)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body((Object) new ErrorResponse(
                                java.time.LocalDateTime.now().toString(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                e.getMessage(),
                                null
                        ))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> ResponseEntity.ok().body((Object) ApiResponse.success("Event retrieved", event)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body((Object) new ErrorResponse(
                                java.time.LocalDateTime.now().toString(),
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                "Event not found",
                                null
                        )));
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> addEvent(@RequestBody EventCreateRequest request) {
        return eventService.addEvent(request)
                .map(event -> ResponseEntity.status(HttpStatus.CREATED).body((Object) ApiResponse.success("Event created", event)))
                .onErrorResume(IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.badRequest()
                                .body((Object) new ErrorResponse(
                                        java.time.LocalDateTime.now().toString(),
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Bad Request",
                                        e.getMessage(),
                                        null
                                ))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> updateEvent(
            @PathVariable Long id,
            @RequestBody EventCreateRequest request) {
        return eventService.updateEvent(id, request)
                .map(event -> ResponseEntity.ok().body((Object) ApiResponse.success("Event updated", event)))
                .onErrorResume(IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.badRequest()
                                .body((Object) new ErrorResponse(
                                        java.time.LocalDateTime.now().toString(),
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Bad Request",
                                        e.getMessage(),
                                        null
                                ))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body((Object) new ErrorResponse(
                                java.time.LocalDateTime.now().toString(),
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                "Event not found",
                                null
                        )));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteEvent(@PathVariable Long id) {
        return eventService.logicalDeleteEvent(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(RuntimeException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body((Object) new ErrorResponse(
                                        java.time.LocalDateTime.now().toString(),
                                        HttpStatus.NOT_FOUND.value(),
                                        "Not Found",
                                        e.getMessage(),
                                        null
                                ))));
    }

    @PatchMapping("/{id}/restore")
    public Mono<ResponseEntity<Object>> restoreEvent(@PathVariable Long id) {
        return eventService.restoreEvent(id)
                .then(Mono.just(ResponseEntity.ok().body((Object) ApiResponse.success("Event restored", null))))
                .onErrorResume(RuntimeException.class,
                        e -> Mono.just(ResponseEntity.badRequest()
                                .body((Object) new ErrorResponse(
                                        java.time.LocalDateTime.now().toString(),
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Bad Request",
                                        e.getMessage(),
                                        null
                                ))));
    }



    @GetMapping("/inactive")
    public Mono<ResponseEntity<Object>> listInactiveEvents() {
        return eventService.listInactiveEvents()
                .collectList()
                .map(list -> ResponseEntity.ok().body((Object) ApiResponse.success("Inactive events retrieved", list)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body((Object) new ErrorResponse(
                                java.time.LocalDateTime.now().toString(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                e.getMessage(),
                                null
                        ))));
    }
}

