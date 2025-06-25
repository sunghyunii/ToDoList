package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Entity.Todo;
import com.sunghyun.todoapp.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderScheduler {
    private final TodoService todoService;
    private final NotificationService notificationService;

    // 매일 오전 9시에 실행
    @Scheduled(cron="0 0 9 * * *", zone = "Asia/Seoul")
    public void sendUnfinishedTaskReminder(){
        List<User> users = todoService.getAllUsers();
        for(User user : users){
            List<Todo> unfinishedTodos = todoService.getUnfinishedTodosDueToday(user.getId());
            if(!unfinishedTodos.isEmpty()){
                notificationService.sendPushNotification(user, unfinishedTodos);
            }
        }

    }
}
