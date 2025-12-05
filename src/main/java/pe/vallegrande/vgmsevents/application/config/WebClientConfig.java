package pe.vallegrande.vgmsevents.application.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {
    @Value("${institutions.service.base-url:http://localhost:9080/api/v1/institutions}")
    private String institutionsBaseUrl;

    @Bean
    public WebClient institutionsWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(institutionsBaseUrl)
                .build();
    }
}
