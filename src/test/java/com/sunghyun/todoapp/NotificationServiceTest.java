package com.sunghyun.todoapp;

import com.sunghyun.todoapp.Entity.PushSubscription;
import com.sunghyun.todoapp.Entity.Status;
import com.sunghyun.todoapp.Entity.Todo;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.repository.TodoRepository;
import com.sunghyun.todoapp.repository.UserRepository;
import com.sunghyun.todoapp.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NotificationServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TodoRepository todoRepository;
    @Autowired
    NotificationService notificationService;
    @Test
    public void testSendPushNotificationToUser(){
        // 푸시 구독 정보 설정
        PushSubscription sub = new PushSubscription();
        sub.setEndpoint("https://fcm.googleapis.com/fcm/send/xxx");
        sub.setP256dhKey("BASE64_p256dhKey");
        sub.setAuthKey("BASE64_authKey");

        User user = new User();
        user.setId("test");
        user.setPushSubscription(sub);
        userRepository.save(user);
        userRepository.flush();

        Todo todo = new Todo();
        todo.setUser(user);
        todo.setContent("푸시 테스트 할일");
        todo.setStatus(Status.TODO);
        todo.setDate(LocalDate.now());
        todoRepository.save(todo);

        List<Todo> todos = todoRepository.findByUserIdAndStatusAndDate(user.getId(),Status.TODO, todo.getDate());
        notificationService.sendPushNotification(user, todos);
    }
}
