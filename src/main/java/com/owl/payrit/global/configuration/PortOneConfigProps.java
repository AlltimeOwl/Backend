package com.owl.payrit.global.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("custom.portone.global")
public class PortOneConfigProps {

    private String storeId;
    private String accessKey;
    private String secretKey;

}
