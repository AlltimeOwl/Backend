package com.owl.payrit.domain.auth.configuration;

import com.owl.payrit.domain.auth.provider.kakao.KakaoApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfiguration {

    @Bean
    public KakaoApiClient kakaoApiClient() {
        return createHttpInterface(KakaoApiClient.class);
    }

    private <T> T createHttpInterface(Class<T> clazz) {
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(clazz);
    }
}
