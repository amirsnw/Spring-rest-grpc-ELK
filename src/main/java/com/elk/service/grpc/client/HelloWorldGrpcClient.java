package com.elk.service.grpc.client;

import com.elk.product.api.NotificationRequest;
import com.elk.product.api.NotificationServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldGrpcClient {

    @GrpcClient("notification-service")
    private NotificationServiceGrpc.NotificationServiceBlockingStub notificationService;

    public String callNotification(String question) {
        return notificationService
                .getNotified(NotificationRequest.newBuilder().setQuestion(question).build()).getResponse();
    }

    public String callGoodLuckNotification(String question) {
        return notificationService
                .getGoodLuckNotify(NotificationRequest.newBuilder().setQuestion(question).build()).getResponse();
    }
}
