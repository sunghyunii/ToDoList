package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Entity.Todo;
import com.sunghyun.todoapp.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    public void sendPushNotification(User user, List<Todo> unfinishedTodos) {
        StringBuilder message = new StringBuilder();
        message.append("오늘 완료하지 않은 할일이 ")
                .append(unfinishedTodos.size())
                .append("개 있어요\n\n");

        int maxTasksToShow = Math.min(3, unfinishedTodos.size());

        for(int i = 0; i<maxTasksToShow; i++){
            Todo todo = unfinishedTodos.get(i);
            message.append("- ").append(todo.getContent()).append("\n");
        }
    }
}
