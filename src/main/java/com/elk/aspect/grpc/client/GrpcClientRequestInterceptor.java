package com.elk.aspect.grpc.client;

import com.elk.config.CorrelationIDHandler;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;

@Slf4j
@GrpcGlobalClientInterceptor
public class GrpcClientRequestInterceptor implements ClientInterceptor {

  private final CorrelationIDHandler correlationIDHandler;

  public GrpcClientRequestInterceptor(CorrelationIDHandler correlationIDHandler) {
    this.correlationIDHandler = correlationIDHandler;
  }

  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
          final MethodDescriptor<ReqT, RespT> methodDescriptor,
          final CallOptions callOptions,
          final Channel channel) {

    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
            channel.newCall(methodDescriptor, callOptions)) {

      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        var correlationId = correlationIDHandler.getCorrelationId();
        var spanId = correlationIDHandler.getSpanId();
        var serviceName = correlationIDHandler.getServiceName();
        log.info("Setting userToken {} in header", correlationId);
        headers.put(Metadata.Key.of("traceId", Metadata.ASCII_STRING_MARSHALLER), correlationId);
        headers.put(Metadata.Key.of("spanId", Metadata.ASCII_STRING_MARSHALLER), spanId);
        headers.put(Metadata.Key.of("service", Metadata.ASCII_STRING_MARSHALLER), serviceName);
        super.start(responseListener, headers);
      }
    };
  }
}
