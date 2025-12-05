package pe.vallegrande.vgmsevents.infrastructure.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.vallegrande.vgmsevents.application.service.CalendarService;
import pe.vallegrande.vgmsevents.infrastructure.dto.ApiResponse;
import pe.vallegrande.vgmsevents.infrastructure.dto.ErrorResponse;
import pe.vallegrande.vgmsevents.infrastructure.dto.request.ImportCalendarsRequest;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/calendars")
public class CalendarRest {

        @Autowired
        private CalendarService calendarService;

        @PostMapping("/import")
        public Mono<ResponseEntity<Object>> importCalendars(@RequestBody ImportCalendarsRequest request) {
                return calendarService.importAll(request)
                                .map(result -> ResponseEntity.status(HttpStatus.CREATED)
                                                .body((Object) ApiResponse.success("Import completed", result)))
                                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body((Object) new ErrorResponse(
                                                                java.time.LocalDateTime.now().toString(),
                                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                                "Import Error",
                                                                e.getMessage(),
                                                                null))));
        }

        @PostMapping
        public Mono<ResponseEntity<Object>> createCalendar(
                        @RequestBody pe.vallegrande.vgmsevents.infrastructure.dto.request.AcademicCalendarRequest request) {

                // 🔍 Validación previa antes de llamar al servicio
                if (request.getInstitutionId() == null || request.getInstitutionId().isEmpty()) {
                        return Mono.just(ResponseEntity.badRequest()
                                        .body((Object) new ErrorResponse(
                                                        java.time.LocalDateTime.now().toString(),
                                                        HttpStatus.BAD_REQUEST.value(),
                                                        "Bad Request",
                                                        "El campo institutionId no puede estar vacío.",
                                                        null)));
                }

                if (request.getAcademicYear() == null || request.getAcademicYear() < 2000
                                || request.getAcademicYear() > 2100) {
                        return Mono.just(ResponseEntity.badRequest()
                                        .body((Object) new ErrorResponse(
                                                        java.time.LocalDateTime.now().toString(),
                                                        HttpStatus.BAD_REQUEST.value(),
                                                        "Bad Request",
                                                        "El campo academicYear debe estar entre 2000 y 2100.",
                                                        null)));
                }

                if (request.getStartDate() == null || request.getEndDate() == null ||
                                !request.getEndDate().isAfter(request.getStartDate())) {
                        return Mono.just(ResponseEntity.badRequest()
                                        .body((Object) new ErrorResponse(
                                                        java.time.LocalDateTime.now().toString(),
                                                        HttpStatus.BAD_REQUEST.value(),
                                                        "Bad Request",
                                                        "Las fechas son inválidas: endDate debe ser posterior a startDate.",
                                                        null)));
                }

                // ✅ Si pasa las validaciones, se crea el calendario
                return calendarService.createCalendar(request)
                                .map(saved -> ResponseEntity.status(HttpStatus.CREATED)
                                                .body((Object) ApiResponse.success("Calendar created", saved)))
                                .onErrorResume(IllegalArgumentException.class,
                                                e -> Mono.just(ResponseEntity.badRequest()
                                                                .body((Object) new ErrorResponse(
                                                                                java.time.LocalDateTime.now()
                                                                                                .toString(),
                                                                                HttpStatus.BAD_REQUEST.value(),
                                                                                "Bad Request",
                                                                                e.getMessage(),
                                                                                null))))
                                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body((Object) new ErrorResponse(
                                                                java.time.LocalDateTime.now().toString(),
                                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                                "Create Error",
                                                                e.getMessage(),
                                                                null))));
        }

        @PostMapping("/{id}/events")
        public Mono<ResponseEntity<Object>> addEvents(@PathVariable("id") Integer id,
                        @RequestBody java.util.List<Long> eventIds) {
                return calendarService.addEventsToCalendar(id, eventIds)
                                .then(Mono.just(ResponseEntity.ok()
                                                .body((Object) ApiResponse.success("Events associated", null))))
                                .onErrorResume(IllegalArgumentException.class,
                                                e -> Mono.just(ResponseEntity.badRequest()
                                                                .body((Object) new ErrorResponse(
                                                                                java.time.LocalDateTime.now()
                                                                                                .toString(),
                                                                                HttpStatus.BAD_REQUEST.value(),
                                                                                "Bad Request",
                                                                                e.getMessage(),
                                                                                null))))
                                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body((Object) new ErrorResponse(
                                                                java.time.LocalDateTime.now().toString(),
                                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                                "Associate Error",
                                                                e.getMessage(),
                                                                null))));
        }

        @GetMapping
        public Mono<ResponseEntity<Object>> listCalendars() {
                return calendarService.listCalendars()
                                .collectList()
                                .map(list -> ResponseEntity.ok()
                                                .body((Object) ApiResponse.success("Calendars retrieved", list)))
                                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body((Object) new ErrorResponse(
                                                                java.time.LocalDateTime.now().toString(),
                                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                                "Internal Server Error",
                                                                e.getMessage(),
                                                                null))));
        }

        @GetMapping("/{id}")
        public Mono<ResponseEntity<Object>> getCalendar(@PathVariable("id") Integer id) {
                return calendarService.getCalendarById(id)
                                .map(cal -> ResponseEntity.ok()
                                                .body((Object) ApiResponse.success("Calendar retrieved", cal)))
                                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body((Object) new ErrorResponse(
                                                                java.time.LocalDateTime.now().toString(),
                                                                HttpStatus.NOT_FOUND.value(),
                                                                "Not Found",
                                                                "Calendar not found",
                                                                null)));
        }

        @GetMapping("/institution/{institutionId}")
        public Mono<ResponseEntity<Object>> getByInstitution(@PathVariable("institutionId") String institutionId) {
                return calendarService.listByInstitution(institutionId)
                                .collectList()
                                .map(list -> ResponseEntity.ok()
                                                .body((Object) ApiResponse.success("Calendars retrieved", list)))
                                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body((Object) new ErrorResponse(
                                                                java.time.LocalDateTime.now().toString(),
                                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                                "Internal Server Error",
                                                                e.getMessage(),
                                                                null))));
        }

        @GetMapping("/{id}/event-calendar")
        public Mono<ResponseEntity<Object>> listEventCalendars(@PathVariable("id") Integer id) {
                return calendarService.listEventCalendarsByCalendarId(id)
                                .collectList()
                                .map(list -> ResponseEntity.ok()
                                                .body((Object) ApiResponse.success("EventCalendars retrieved", list)))
                                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body((Object) new ErrorResponse(
                                                                java.time.LocalDateTime.now().toString(),
                                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                                "Internal Server Error",
                                                                e.getMessage(),
                                                                null))));
        }
}
