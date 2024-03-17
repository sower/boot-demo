package me.boot.web.config;

import com.google.common.collect.ImmutableList;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import lombok.Data;
import me.boot.base.constant.Constants;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * SwaggerConfig
 *
 * @since 2024/03/09
 **/
@Data
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bootOpenAPI(@Value("${spring.application.version:unknown}") String appVersion) {
        final String securitySchemeName = "bearerAuth";
        Components components = new Components().addSecuritySchemes(securitySchemeName,
            new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP)
                .scheme("bearer").bearerFormat("JWT"));

//            .addSecuritySchemes("basicScheme",
//            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"));

        BiConsumer<String, SecurityScheme.In> addSecurityScheme = (item, ins) -> components.addSecuritySchemes(
            item, new SecurityScheme().type(Type.APIKEY).name(item).in(ins));

        // 公共请求头
        ImmutableList<String> headers = ImmutableList.of(Constants.TRACE_ID,
            HttpHeaders.ACCEPT_LANGUAGE);
        headers.forEach(header -> addSecurityScheme.accept(header, SecurityScheme.In.HEADER));
        // 站点Cookies
        Set<String> cookies = Collections.singleton("sessionId");
        cookies.forEach(cookie -> addSecurityScheme.accept(cookie, In.COOKIE));

        // 对所有端点应用
        Map<String, SecurityScheme> securitySchemes = components.getSecuritySchemes();
        SecurityRequirement requirement = new SecurityRequirement();
        securitySchemes.keySet().forEach(requirement::addList);
        return new OpenAPI().info(
            new Info().title("Boot Demo API").description("Boot Demo application")
                .version(appVersion)
                .license(new License().name("Apache 2.0").url("https://boot.me"))).externalDocs(
            new ExternalDocumentation().description("Boot Demo Wiki Documentation")
                .url("https://boot.me/docs")).components(components).addSecurityItem(requirement);
    }


    @Bean
    public GroupedOpenApi binApi() {
        return GroupedOpenApi.builder().group("Http Bin").pathsToMatch("/bin/**").build();
    }

    //    @Bean
    public GroupedOpenApi othersApi() {
        return GroupedOpenApi.builder().group("Demo Api").pathsToExclude("/bin/**").build();
    }

}
