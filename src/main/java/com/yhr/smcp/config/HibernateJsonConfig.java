package com.yhr.smcp.config;

import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateJsonConfig {
    @Bean
    public HibernatePropertiesCustomizer jsonFormatMapperCustomizer() {
        com.fasterxml.jackson.databind.ObjectMapper jackson2Mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        return properties -> properties.put(
                "hibernate.type.json_format_mapper",
                new JacksonJsonFormatMapper(jackson2Mapper)
        );
    }
}
