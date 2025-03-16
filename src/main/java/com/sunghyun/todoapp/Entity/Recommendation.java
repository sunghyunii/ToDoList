package com.sunghyun.todoapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Recommendation {
    @Id @GeneratedValue
    @Column(name = "recommendation_id")
    private Long id;
    @OneToMany(mappedBy = "rec")
    private List<User> user = new ArrayList<>();
    @OneToMany
    private List<Todo> todo = new ArrayList<>();

}
