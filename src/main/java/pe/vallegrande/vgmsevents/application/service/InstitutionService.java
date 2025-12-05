package pe.vallegrande.vgmsevents.application.service;

import pe.vallegrande.vgmsevents.infrastructure.dto.response.InstitutionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InstitutionService {
    Flux<InstitutionResponse> getAllInstitutions();
    Mono<InstitutionResponse> getInstitutionById(String institutionId);
}
