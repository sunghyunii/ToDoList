package com.sunghyun.todoapp.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {
    @Id
    @Column(name = "user_id")
    private String id;
    private String password;
    private String email;
    private String nickname;
    private String image;
    private String introduction;
    @OneToMany(mappedBy = "user")
    private List<Todo> todo = new ArrayList();
    @OneToMany(mappedBy = "user")
    private List<Category> categories = new ArrayList();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id")
    private Recommendation rec;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "push_subscription_id")
    private PushSubscription pushSubscription;

    /** jwt 토큰 추가 */
    @Column(length = 1000)
    private String refreshToken;

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public void destroyRefreshToken(){
        this.refreshToken = null;
    }
}
