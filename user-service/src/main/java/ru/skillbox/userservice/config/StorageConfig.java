package ru.skillbox.userservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@Log4j2
public class StorageConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imagesPath = Paths.get("", "images").toAbsolutePath().toString();
        System.out.println("Путь к папке с картинками: " + imagesPath);
        registry.addResourceHandler("/api/v1/img/**")
                .addResourceLocations("file:" + imagesPath + "/")
                .resourceChain(true)
                .addResolver(new LoggingResourceResolver());
    }


    @Component
    public static class LoggingResourceResolver implements ResourceResolver {

        @Override
        public Resource resolveResource(HttpServletRequest request, String requestPath,
                                        List<? extends Resource> locations, ResourceResolverChain chain) {
            String basePath = Paths.get(System.getProperty("user.dir"), "images").toAbsolutePath().toString();
            File file = new File(basePath + "/" + requestPath);

            if (Files.exists(file.toPath())) {
                log.info("File found: " + file.getAbsolutePath());
            } else {
                log.warn("File not found: " + file.getAbsolutePath());
            }
            return chain.resolveResource(request, requestPath, locations);
        }

        @Override
        public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
            return null;
        }
    }
}