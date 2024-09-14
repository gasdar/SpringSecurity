package com.spring.security.app.configs;

import java.util.logging.Logger;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(
    value={
        @PropertySource(value="classpath:env.properties"),
        @PropertySource(value="classpath:other-file.properties")
    }
)
public class PropertyFileConfig {

    public final static Logger LOGGER = Logger.getLogger(PropertyFileConfig.class.getName());

}
