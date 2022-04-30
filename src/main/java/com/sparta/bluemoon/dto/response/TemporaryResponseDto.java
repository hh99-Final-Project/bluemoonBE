package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Temporary;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemporaryResponseDto {
    private Long tempId;
    private Long userId;
    private String nickname;
    private String title;
    private String content;

    public TemporaryResponseDto(Temporary temp) {
        this.tempId = temp.getId();
        this.userId = temp.getUser().getId();
        this.nickname = temp.getUser().getNickname();
        this.title = temp.getTitle();
        this.content = temp.getContent();
    }
    //private CommentListDto comments;
    //private boolean isShow;
}
