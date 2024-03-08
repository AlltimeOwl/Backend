package com.owl.payrit.domain.notification.entity;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

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

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isRead = false;
}
