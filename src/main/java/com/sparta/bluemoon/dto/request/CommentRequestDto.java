package com.sparta.bluemoon.dto.request;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private String postUuid;
    private String content;
    private String parentUuid;
    private boolean isLock;
}
