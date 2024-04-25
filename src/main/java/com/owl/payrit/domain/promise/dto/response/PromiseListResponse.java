package com.owl.payrit.domain.promise.dto.response;

import com.owl.payrit.domain.promise.entity.ParticipantsInfo;
import com.owl.payrit.domain.promise.entity.Promise;

import java.time.LocalDate;
import java.util.List;

public record PromiseListResponse(
        LocalDate promiseEndDate,
        String writerName,
        List<ParticipantsInfo> participants
) {
    public PromiseListResponse(Promise promise, String myName) {
        this(
                promise.getPromiseEndDate(),
                myName,
                promise.getParticipants()
        );
    }
}
