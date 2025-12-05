package pe.vallegrande.vgmsevents.infrastructure.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.vallegrande.vgmsevents.application.service.InstitutionService;
import pe.vallegrande.vgmsevents.infrastructure.dto.response.InstitutionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/institutions")
@CrossOrigin(origins = "*")
public class InstitutionRest {

    @Autowired
    private InstitutionService institutionService;

    @GetMapping
    public Flux<InstitutionResponse> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/{id}")
    public Mono<InstitutionResponse> getInstitutionById(@PathVariable String id) {
        return institutionService.getInstitutionById(id);
    }
}
