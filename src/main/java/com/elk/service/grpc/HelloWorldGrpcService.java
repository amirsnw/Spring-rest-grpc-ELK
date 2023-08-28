package com.elk.service.grpc;

import com.elk.product.api.NotificationRequest;
import com.elk.product.api.NotificationResponse;
import com.elk.product.api.NotificationServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class HelloWorldGrpcService extends NotificationServiceGrpc.NotificationServiceImplBase {
    @Override
    public void getNotified(NotificationRequest request, StreamObserver<NotificationResponse> responseObserver) {
        responseObserver.onNext(
                NotificationResponse.newBuilder()
                        .setResponse("Hello")
                        .build()
        );
        responseObserver.onCompleted();
    }
}
