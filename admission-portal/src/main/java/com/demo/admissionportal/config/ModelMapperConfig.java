package com.demo.admissionportal.config;


import com.demo.admissionportal.converter.LocalDateTimeConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Model mapper config.
 */
@Configuration
public class ModelMapperConfig {
    /**
     * Getmodel mapper model mapper.
     *
     * @return the model mapper
     */
    @Bean
    public ModelMapper GetmodelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.addConverter(new LocalDateTimeConverter());
        return modelMapper;
    }

}
