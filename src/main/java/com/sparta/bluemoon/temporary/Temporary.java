package com.sparta.bluemoon.temporary;

import com.sparta.bluemoon.util.Timestamped;
import com.sparta.bluemoon.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor

public class Temporary extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    @ManyToOne
    private User user;

    public Temporary(TemporaryRequestDto requestDto, User user) {
        this.title= requestDto.getTitle();;
        this.content= requestDto.getContent();;
        this.user=user;
    }

    public void update(TemporaryRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
