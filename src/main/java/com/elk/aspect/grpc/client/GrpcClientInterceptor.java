package com.elk.aspect.grpc.client;

import com.elk.config.ContextHolder;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;

import static com.elk.config.CommonConst.SPAN_ID;
import static com.elk.config.CommonConst.TRACE_ID;

@Slf4j
@GrpcGlobalClientInterceptor
public class GrpcClientInterceptor implements ClientInterceptor {

    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            final MethodDescriptor<ReqT, RespT> methodDescriptor,
            final CallOptions callOptions,
            final Channel channel) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                channel.newCall(methodDescriptor, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                var traceId = ContextHolder.getTraceId();
                var spanId = ContextHolder.getSpanId();
                headers.put(Metadata.Key.of(TRACE_ID, Metadata.ASCII_STRING_MARSHALLER), traceId);
                headers.put(Metadata.Key.of(SPAN_ID, Metadata.ASCII_STRING_MARSHALLER), spanId);
                super.start(responseListener, headers);
            }
        };
    }
}
