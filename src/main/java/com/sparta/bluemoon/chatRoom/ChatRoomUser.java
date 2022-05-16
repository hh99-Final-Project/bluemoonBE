package com.sparta.bluemoon.chatRoom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sparta.bluemoon.util.Timestamped;
import com.sparta.bluemoon.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoomUser extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 채팅방 주인
    @ManyToOne
    private User user;

    // 채팅방 이름
    private String name;


    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;


    public ChatRoomUser(User user, User anotherUser, ChatRoom room) {

        this.user = user;
        this.name = anotherUser.getNickname();
        this.chatRoom = room;
    }


}
