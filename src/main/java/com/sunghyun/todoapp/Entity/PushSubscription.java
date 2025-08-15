package com.sunghyun.todoapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PushSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String endpoint; // 어디로
    private String p256dhKey; // 인증키
    private String authKey; //인증키
    @OneToOne
    private User user;
}
