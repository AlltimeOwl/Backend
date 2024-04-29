package com.owl.payrit.domain.promise.dto.response;

import com.owl.payrit.domain.promise.entity.ParticipantsInfo;
import com.owl.payrit.domain.promise.entity.Promise;
import com.owl.payrit.domain.promise.entity.PromiseImageType;

import java.time.LocalDate;
import java.util.List;

public record PromiseDetailResponse(

        Long promiseId,
        long amount,
        LocalDate promiseStartDate,
        LocalDate promiseEndDate,
        String writerName,
        List<ParticipantsInfo> participants,
        String contents,
        PromiseImageType promiseImageType
) {
    public PromiseDetailResponse(Promise promise) {
        this(
                promise.getId(),
                promise.getAmount(),
                promise.getPromiseStartDate(),
                promise.getPromiseEndDate(),
                promise.getWriterName(),
                promise.getParticipants(),
                promise.getContents(),
                promise.getPromiseImageType()
        );
    }
}
