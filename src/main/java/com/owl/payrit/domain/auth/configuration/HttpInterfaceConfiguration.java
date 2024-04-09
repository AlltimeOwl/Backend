package com.owl.payrit.domain.auth.configuration;

import com.owl.payrit.domain.auth.provider.apple.AppleApiClient;
import com.owl.payrit.domain.auth.provider.portone.PortOneApiClient;
import com.owl.payrit.domain.auth.provider.kakao.KakaoApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class HttpInterfaceConfiguration {

    @Bean
    public KakaoApiClient kakaoApiClient() {
        return createHttpInterface(KakaoApiClient.class);
    }

    @Bean
    public AppleApiClient appleApiClient() {
        return createHttpInterface(AppleApiClient.class);
    }

    @Bean
    public PortOneApiClient portOneApiClient() {
        return createHttpInterface(PortOneApiClient.class);
    }

    private <T> T createHttpInterface(Class<T> clazz) {
        WebClient webClient = WebClient.builder()
                                       .filter(logRequest())
                                       .filter(logResponse())
                                       .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(clazz);
    }

    // 요청 로그
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Sending Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("Request Header: {}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    // 응답 로그
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Received Response: Status={} Headers={}", clientResponse.statusCode(), clientResponse.headers().asHttpHeaders());
            return Mono.just(clientResponse);
        });
    }
}
