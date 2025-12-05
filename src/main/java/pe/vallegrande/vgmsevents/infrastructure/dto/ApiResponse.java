package pe.vallegrande.vgmsevents.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String timestamp;
    private String path;

    // Método para respuesta exitosa
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                java.time.LocalDateTime.now().toString(),
                null
        );
    }

    // Método para respuesta de error simple
    public static <T> ApiResponse<T> error(String message, String path) {
        return new ApiResponse<>(
                false,
                message,
                null,
                java.time.LocalDateTime.now().toString(),
                path
        );
    }
}
