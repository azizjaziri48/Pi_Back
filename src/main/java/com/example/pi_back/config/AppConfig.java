package com.example.pi_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Integer riskscore() {
        return 0;
    }
}