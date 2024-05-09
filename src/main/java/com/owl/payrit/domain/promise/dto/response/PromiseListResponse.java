package com.owl.payrit.domain.promise.dto.response;

import com.owl.payrit.domain.promise.entity.ParticipantsInfo;
import com.owl.payrit.domain.promise.entity.Promise;
import com.owl.payrit.domain.promise.entity.PromiseImageType;

import java.time.LocalDate;
import java.util.List;

public record PromiseListResponse(

        Long promiseId,
        long amount,
        LocalDate promiseStartDate,
        LocalDate promiseEndDate,
        String writerName,
        String contents,
        List<ParticipantsInfo> participants,
        PromiseImageType promiseImageType
) {
    public PromiseListResponse(Promise promise) {
        this(
                promise.getId(),
                promise.getAmount(),
                promise.getPromiseStartDate(),
                promise.getPromiseEndDate(),
                promise.getWriterName(),
                promise.getContents(),
                promise.getParticipants(),
                promise.getPromiseImageType()
        );
    }
}
