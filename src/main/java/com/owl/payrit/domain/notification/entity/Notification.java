package com.owl.payrit.domain.notification.entity;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String contents;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead = false;

    public Notification(Member member, String contents, NotificationType notificationType) {
        this.member = member;
        this.contents = contents;
        this.notificationType = notificationType;
    }
}
