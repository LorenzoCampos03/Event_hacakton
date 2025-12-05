package pe.vallegrande.vgmsevents.infrastructure.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class InstitutionClient {

    private final WebClient institutionsWebClient;

    // Inyecta el WebClient definido en WebClientConfig
    public InstitutionClient(@Qualifier("institutionsWebClient") WebClient institutionsWebClient) {
        this.institutionsWebClient = institutionsWebClient;
    }

    // Listar todas las instituciones (devuelve el body.data si tu API lo envuelve en ApiResponse)
    public Flux<InstitutionDto> getAllInstitutions() {
        return institutionsWebClient.get()
                // si tu WebClient base ya apunta a .../api/v1/institutions, usa .uri("/")
                .uri("/") 
                .retrieve()
                .bodyToFlux(InstitutionDto.class);
    }

    // Obtener institución por id
    public Mono<InstitutionDto> getInstitutionById(String id) {
        return institutionsWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(InstitutionDto.class);
    }
}
