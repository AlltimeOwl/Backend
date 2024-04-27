package com.owl.payrit.domain.promise.dto.response;

import com.owl.payrit.domain.promise.entity.ParticipantsInfo;
import com.owl.payrit.domain.promise.entity.Promise;
import com.owl.payrit.domain.promise.entity.PromiseImageType;

import java.time.LocalDate;
import java.util.List;

public record PromiseListResponse(

        long amount,
        LocalDate promiseStartDate,
        LocalDate promiseEndDate,
        String writerName,
        List<ParticipantsInfo> participants,
        PromiseImageType promiseImageType
) {
    public PromiseListResponse(Promise promise, String writerName) {
        this(
                promise.getAmount(),
                promise.getPromiseStartDate(),
                promise.getPromiseEndDate(),
                writerName,
                promise.getParticipants(),
                promise.getPromiseImageType()
        );
    }
}
