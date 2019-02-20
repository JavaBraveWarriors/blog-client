package com.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@EnableWebMvc
@PropertySource({
        "classpath:application.properties"
})
@ComponentScan("com.blog")
@Import({
        RestClientConfiguration.class,
        JacksonConfiguration.class,
        LocaleConfiguration.class,
        ThymeleafConfiguration.class})
public class MvcWebConfig implements WebMvcConfigurer {

    private LocaleChangeInterceptor localeChangeInterceptor;

    @Autowired
    public MvcWebConfig(LocaleChangeInterceptor localeChangeInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/WEB-INF/static/css/").setCachePeriod(31556926)
                .addResourceLocations("/WEB-INF/static/js/").setCachePeriod(31556926);
        registry.addResourceHandler("/font/**")
                .addResourceLocations("/WEB-INF/static/font/roboto").setCachePeriod(31556926);

    }
}
