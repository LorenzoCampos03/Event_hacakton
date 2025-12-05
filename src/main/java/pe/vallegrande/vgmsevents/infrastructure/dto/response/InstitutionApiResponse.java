package pe.vallegrande.vgmsevents.infrastructure.dto.response;

import lombok.Data;

@Data
public class InstitutionApiResponse {
    private boolean success;
    private String message;
    private InstitutionResponse data; // aquí va tu InstitutionResponse existente
}
