package com.sunghyun.todoapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Todo {
    @Id @GeneratedValue
    @Column(name = "todo_id")
    private Long id;
    private String content;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

}
