package com.owl.payrit.domain.promise.dto.response;

import com.owl.payrit.domain.promise.entity.Promise;

import java.time.LocalDate;
import java.util.List;

public record PromiseDetailResponse(

        long amount,
        LocalDate promiseEndDate,
        String myName,
        List<String> participants,
        String contents
) {
    public PromiseDetailResponse(Promise promise, String myName) {
        this(
                promise.getAmount(),
                promise.getPromiseEndDate(),
                myName,
                promise.getParticipants(),
                promise.getContents()
        );
    }
}
