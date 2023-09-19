package com.elk.service.grpc;

import com.elk.aspect.logger.RemoteLogger;
import com.elk.product.api.NotificationRequest;
import com.elk.product.api.NotificationResponse;
import com.elk.product.api.NotificationServiceGrpc;
import com.elk.service.grpc.client.HelloWorldGrpcClient;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class HelloWorldGrpcService extends NotificationServiceGrpc.NotificationServiceImplBase {

    public final HelloWorldGrpcClient client;

    @Override
    @RemoteLogger("[getNotified] endpoint called")
    public void getNotified(NotificationRequest request, StreamObserver<NotificationResponse> responseObserver) {
        responseObserver.onNext(
                NotificationResponse.newBuilder()
                        .setResponse("Hello")
                        .build()
        );
        client.callGoodLuckNotification(request.getQuestion());
        responseObserver.onCompleted();
    }

    @Override
    @RemoteLogger("[getGoodLuckNotify] endpoint called")
    public void getGoodLuckNotify(NotificationRequest request, StreamObserver<NotificationResponse> responseObserver) {
        responseObserver.onNext(
                NotificationResponse.newBuilder()
                        .setResponse("Good Luck")
                        .build()
        );
        responseObserver.onCompleted();
    }
}
