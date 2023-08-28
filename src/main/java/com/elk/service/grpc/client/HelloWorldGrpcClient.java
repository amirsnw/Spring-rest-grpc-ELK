package com.elk.service.grpc.client;

import com.elk.product.api.NotificationRequest;
import com.elk.product.api.NotificationServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;

public class HelloWorldGrpcClient {

    @GrpcClient("notification-server")
    private NotificationServiceGrpc.NotificationServiceBlockingStub notificationService;

    public String callNotification(String question) {
        return notificationService
                .getNotified(NotificationRequest.newBuilder().setQuestion(question).build()).getResponse();
    }
}
