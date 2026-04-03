package com.berd.dev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.berd.dev.interceptor.LastUrlInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LastUrlInterceptor lastUrlInterceptor;

    @SuppressWarnings("null")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // On applique l'intercepteur partout sauf sur les fichiers statiques
        registry.addInterceptor(lastUrlInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/error", "/assets/**");
    }

    @SuppressWarnings("null")
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cache pour CSS (1 an)
        registry.addResourceHandler("/assets/css/**")
                .addResourceLocations("classpath:/static/assets/css/")
                .setCachePeriod(31536000);

        // Cache pour JS (1 an)
        registry.addResourceHandler("/assets/js/**")
                .addResourceLocations("classpath:/static/assets/js/")
                .setCachePeriod(31536000);

        // Cache pour images (1 an)
        registry.addResourceHandler("/assets/images/**")
                .addResourceLocations("classpath:/static/assets/images/")
                .setCachePeriod(31536000);

        // Cache pour vendors (1 an)
        registry.addResourceHandler("/assets/vendors/**")
                .addResourceLocations("classpath:/static/assets/vendors/")
                .setCachePeriod(31536000);

        // Cache pour tous les autres assets (1 mois)
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(2592000);
    }
}
