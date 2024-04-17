package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.promissorypaper.entity.PaperFormInfo;
import com.owl.payrit.domain.promissorypaper.entity.PaperProfile;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.repaymenthistory.dto.RepaymentHistoryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record PaperDetailResponse(

        @Schema(description = "차용증 id")
        Long paperId,

        @Schema(description = "나의 역할")
        PaperRole memberRole,

        @Schema(description = "차용증 폼 데이터")
        PaperFormInfo paperFormInfo,

        @Schema(description = "상환 진행률")
        double repaymentRate,

        @Schema(description = "남은 일 수")
        long dueDate,

        @Schema(description = "채권자 정보")
        PaperProfile creditorProfile,

        @Schema(description = "채무자 정보")
        PaperProfile debtorProfile,

        @Schema(description = "해당 차용증에 내가 작성한 메모들")
        List<MemoListResponse> memoListResponses,

        @Schema(description = "상환내역 객체 리스트")
        List<RepaymentHistoryDto> repaymentHistories,

        @Schema(description = "수정 요청 사항")
        String modifyRequest
) {
    public PaperDetailResponse(PromissoryPaper promissoryPaper, PaperRole memberRole,
                               double repaymentRate, long dueDate, List<MemoListResponse> memoListResponses) {
        this(
                promissoryPaper.getId(),
                memberRole,
                promissoryPaper.getPaperFormInfo(),
                repaymentRate,
                dueDate,
                promissoryPaper.getCreditorProfile(),
                promissoryPaper.getDebtorProfile(),
                memoListResponses,
                promissoryPaper.getRepaymentHistory()
                        .stream()
                        .map(history -> new RepaymentHistoryDto(history.getId(), history.getRepaymentDate(),
                                history.getRepaymentAmount()))
                        .collect(Collectors.toList()),
                promissoryPaper.getModifyRequest()
        );
    }
}
