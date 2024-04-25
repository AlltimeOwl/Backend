package com.owl.payrit.domain.docsinfo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "custom.azure.storage")
public class AzureStorageConfigProps {

    private String accountName;
    private String accountKey;
    private String shareName;
    private String dirName;
    private String urlHeader;
    private String imgHeader;
    private String sasToken;

    @Bean
    public String getConnectStr() {

        StringBuilder sb = new StringBuilder();

        sb.append("DefaultEndpointsProtocol=https;");
        sb.append("AccountName=%s;".formatted(accountName));
        sb.append("AccountKey=%s".formatted(accountKey));

        return sb.toString();
    }
}
