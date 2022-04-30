package com.sparta.bluemoon.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    private int roomHashCode;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomUser> chatRoomUsers = new ArrayList<>();


    public ChatRoom(int roomUsers) {
        this.roomHashCode=roomUsers;
    }
}
