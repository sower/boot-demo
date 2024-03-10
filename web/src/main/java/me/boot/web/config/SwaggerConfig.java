package me.boot.web.config;

import com.google.common.collect.ImmutableList;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig
 *
 * @since 2024/03/09
 **/
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bootOpenAPI() {

        // 自定义请求头
        ImmutableList<String> headers = ImmutableList.of("X-Trace-ID", "X-User-ID");
        Components components = new Components().addSecuritySchemes("basicScheme",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"));
        headers.forEach(header -> components
            .addSecuritySchemes(header, new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(header)));

        return new OpenAPI()
            .info(new Info().title("Boot Demo API")
                .description("Boot Demo application")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("https://boot.me")))
            .externalDocs(new ExternalDocumentation()
                .description("Boot Demo Wiki Documentation")
                .url("https://boot.me/docs"))
            .components(components);
    }


    @Bean
    public GroupedOpenApi binApi() {
        return GroupedOpenApi.builder()
            .group("Http Bin")
            .pathsToMatch("/bin/**")
            .build();
    }

//    @Bean
    public GroupedOpenApi othersApi() {
        return GroupedOpenApi.builder()
            .group("Demo Api")
            .pathsToExclude("/bin/**")
            .build();
    }

}
