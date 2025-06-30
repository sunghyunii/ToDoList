package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Entity.PushSubscription;
import com.sunghyun.todoapp.Entity.Todo;
import com.sunghyun.todoapp.Entity.User;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class NotificationService {
    @Value("${push.vapid.public-key}")
    private String publicKey;
    @Value("${push.vapid.private-key}")
    private String privateKey;
    @Value("${push.vapid.subject}")
    private String subject;


    public void sendPushNotification(User user, List<Todo> unfinishedTodos) {
        String message = "오늘 완료하지 않은 할일이 " + unfinishedTodos.size()
                + "개 있어요";
        sendWebPush(user, message);
    }
    public void sendWebPush(User user, String message) {
        try{
            PushSubscription subscription = user.getPushSubscription(); // DB에 저장된 정보
            PushService pushService = new PushService(publicKey, privateKey, subject);
            Notification notification = new Notification(
                    subscription.getEndpoint(),
                    subscription.getP256dhKey(),
                    subscription.getAuthKey(),
                    message
            );
            HttpResponse response = pushService.send(notification);
            System.out.println("push 응답: " + response.getStatusLine());

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
