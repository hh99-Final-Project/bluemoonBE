package com.sparta.bluemoon.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // Entity가 자동으로 컬럼으로 인식합니다.
@EntityListeners(AuditingEntityListener.class) // 생성/변경 시간을 자동으로 업데이트합니다.
public abstract class Timestamped {
//    @CreatedDate // 최초 생성 시점
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    private LocalDateTime createdAt;

    @CreatedDate // 생성시간
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

}