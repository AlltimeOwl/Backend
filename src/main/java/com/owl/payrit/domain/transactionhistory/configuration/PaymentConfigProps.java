package com.owl.payrit.domain.transactionhistory.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("custom.portone.payment")
public class PaymentConfigProps {

    private String PID;
    private String PGCode;
    private String testPGCode;
    private int paperCost;
    private int notiCost;
}
