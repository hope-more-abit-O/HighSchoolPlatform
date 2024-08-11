package com.demo.admissionportal.config;


import com.demo.admissionportal.converter.*;
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
     * Get model mapper.
     *
     * @return the model mapper
     */
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addConverter(new LocalDateTimeConverter());
        modelMapper.addConverter(new AccountStatusConverter());
        modelMapper.addConverter(new GenderStatusConverter());
        modelMapper.addConverter(new RoleToStringConverter());
        modelMapper.addConverter(new DateToStringConverter());
        modelMapper.addConverter(new UniversityTypeToStringConverter());
        modelMapper.addConverter(new PostPropertiesConverter());
        modelMapper.addConverter(new PostStatusConverter());
        modelMapper.addConverter(new CreateUniversityRequestStatusToStringConverter());
        modelMapper.addConverter(new MessageStatusConverter());
        modelMapper.addConverter(new ReportStatusConverter());
        modelMapper.addConverter(new ReportTypeConverter());
        modelMapper.addConverter(new StudentReportStatusConverter());
        modelMapper.addConverter(new SubjectStatusConverter());
        modelMapper.addConverter(new EducationLevelConverter());
        modelMapper.addConverter(new AdmissionScoreStatusConverter());
        return modelMapper;
    }

}