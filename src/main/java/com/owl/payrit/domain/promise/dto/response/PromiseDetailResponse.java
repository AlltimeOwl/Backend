package com.owl.payrit.domain.promise.dto.response;

import com.owl.payrit.domain.promise.entity.ParticipantsInfo;
import com.owl.payrit.domain.promise.entity.Promise;

import java.time.LocalDate;
import java.util.List;

public record PromiseDetailResponse(

        long amount,
        LocalDate promiseStartDate,
        LocalDate promiseEndDate,
        String writerName,
        List<ParticipantsInfo> participants,
        String contents,
        String promiseImageUrl
) {
    public PromiseDetailResponse(Promise promise, String writerName) {
        this(
                promise.getAmount(),
                promise.getPromiseStartDate(),
                promise.getPromiseEndDate(),
                writerName,
                promise.getParticipants(),
                promise.getContents(),
                promise.getPromiseImageUrl()
        );
    }
}
