package com.owl.payrit.domain.promise.dto.request;

import com.owl.payrit.domain.promise.entity.PromiseImageType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PromiseWriteRequest(

        @Schema(minimum = "0")
        long amount,

        @Schema(description = "약속 시작일")
        LocalDate promiseStartDate,

        @Schema(description = "약속 마감일")
        LocalDate promiseEndDate,

        @Schema(description = "약속 상세 내용")
        String contents,

        @Schema(description = "작성자 이름")
        String writerName,

        @Schema(description = "참가자들의 이름을 공백 없이, 쉼표로 구분하여 작성 해 주세요. ex)\"김이름,박이름\"")
        String participantsName,

        @Schema(description = "참가자들의 전화번호를 공백 없이, 쉼표로 구분하여 작성 해 주세요. (이름 순서와 같게)")
        String participantsPhone,

        @Schema(description = "이미지 종류")
        PromiseImageType promiseImageType
) {
}
