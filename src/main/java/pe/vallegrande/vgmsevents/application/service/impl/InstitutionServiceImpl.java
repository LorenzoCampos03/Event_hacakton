package pe.vallegrande.vgmsevents.application.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.vallegrande.vgmsevents.application.service.InstitutionService;
import pe.vallegrande.vgmsevents.infrastructure.dto.response.InstitutionApiResponse;
import pe.vallegrande.vgmsevents.infrastructure.dto.response.InstitutionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final WebClient institutionsWebClient;

    public InstitutionServiceImpl(@Qualifier("institutionsWebClient") WebClient institutionsWebClient) {
        this.institutionsWebClient = institutionsWebClient;
    }

    @Override
    public Flux<InstitutionResponse> getAllInstitutions() {
        return institutionsWebClient.get()
                .uri("/list") // Ajusta según la ruta real del microservicio
                .retrieve()
                .bodyToFlux(InstitutionResponse.class)
                .doOnNext(inst -> System.out.println("DEBUG: Institution en listAll: " + inst))
                .doOnError(err -> System.out.println("ERROR getAllInstitutions: " + err.getMessage()));
    }

    @Override
    public Mono<InstitutionResponse> getInstitutionById(String institutionId) {
        return institutionsWebClient.get()
                .uri("/{id}", institutionId)
                .retrieve()
                .bodyToMono(InstitutionApiResponse.class)
                .map(InstitutionApiResponse::getData) // esto extrae solo el objeto data
                .doOnNext(inst -> System.out.println("DEBUG: Institution obtenida: " + inst));
    }
}
