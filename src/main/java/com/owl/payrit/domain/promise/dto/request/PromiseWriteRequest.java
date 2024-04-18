package com.owl.payrit.domain.promise.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PromiseWriteRequest(

        @Schema(minimum = "0")
        long amount,
        LocalDate promiseEndDate,
        String contents,
        @Schema(description = "참가자들의 이름을 공백 없이, 쉼표로 구분하여 작성 해 주세요. ex)\"김이름,박이름\"")
        String participants
) {
}
