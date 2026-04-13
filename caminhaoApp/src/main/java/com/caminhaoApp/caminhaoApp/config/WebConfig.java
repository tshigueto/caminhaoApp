package com.caminhaoApp.caminhaoApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Pega o caminho C:/uploads/caminhaoapp/ do seu application.properties
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cria a rota virtual "/uploads/" que aponta para a pasta física
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}



/* package com.caminhaoApp.caminhaoApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Pega o caminho C:/uploads/caminhaoapp/ do seu application.properties
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cria a rota virtual "/uploads/" que aponta para a pasta física
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + uploadPath);
    }
}*/
