package com.owl.payrit.domain.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record PortOneTokenRequest(
    String impKey,
    String impSecret
) {

}
