package com.sankore.bank.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.application.version}")
    private String applicationVersion;

    @Value("${spring.application.description}")
    private String applicationDescription;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Docket createDocker() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.wayapaychat.bank"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(createInfo()).select().build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()));
    }

    @Bean
    public ApiInfo createInfo() {

        return new ApiInfo(applicationName, applicationDescription, applicationVersion, "",
                           new Contact("Payment Solution Service",
                                       "http://www.github.com/Nazel7",
                                       "ga.olanipekun@gmail.com"),
                                       "Apache Licence Version 2.0.0",
                                       "http://www.apache.org/licenses/LICENSE-2.0",
                                       Collections.emptyList());
    }


    private SecurityContext securityContext() {

        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private ApiKey apiKey() {
        return new ApiKey("jwt", "Authorization", "header");
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("jwt", authorizationScopes));
    }
}
