package com.owl.payrit.domain.auth.provider.portone;

import com.owl.payrit.domain.auth.dto.response.PortOneCertificationResponse;
import com.owl.payrit.domain.auth.dto.response.PortOneTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface PortOneApiClient {

    @Deprecated
    @PostExchange("https://api.iamport.kr/users/getToken")
    PortOneTokenResponse getAccessToken(@RequestPart("imp_key") String impKey, @RequestPart("imp_secret") String impSecret);

    @GetExchange("https://api.iamport.kr/certifications/{impUid}")
    PortOneCertificationResponse getCertificationInformation(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String bearerToken,  @PathVariable("impUid") String impUid);
}
