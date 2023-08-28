package com.elk.aspect.grpc.server;

import com.elk.config.ContextHolder;
import com.elk.service.GeneratorUtil;
import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

import static com.elk.config.CommonConst.TRACE_ID;

/**
 * gRPC server interceptor with ThreadLocal
 */
@GrpcGlobalServerInterceptor
public class GrpcServerInterceptor implements ServerInterceptor {

    private final GeneratorUtil generatorUtil;

    public GrpcServerInterceptor(GeneratorUtil generatorUtil) {
        this.generatorUtil = generatorUtil;
    }

    @Override
    public <R, S> ServerCall.Listener<R> interceptCall(
            ServerCall<R, S> serverCall, Metadata metadata, ServerCallHandler<R, S> next) {

        /**
         * For additional method name log
         * MethodDescriptor<R, S> methodDescriptor = serverCall.getMethodDescriptor();
         * GrpcMethod grpcMethod = GrpcMethod.of(methodDescriptor);
         * var target = grpcMethod.methodName();
         */

        var traceId = metadata.get(Metadata.Key.of("traceId", Metadata.ASCII_STRING_MARSHALLER));
        var spanId = "";
        if (traceId == null) {
            traceId = generatorUtil.traceIdGenerator();
            spanId = generatorUtil.getAsSpanId(traceId);
            metadata.put(Metadata.Key.of(TRACE_ID, Metadata.ASCII_STRING_MARSHALLER), traceId);
        } else {
            spanId = generatorUtil.spanIdGenerator();
        }
        ContextHolder.init(traceId, spanId);
        try {
            return new WrappingListener<>(next.startCall(serverCall, metadata), traceId, spanId);
        } finally {
            ContextHolder.clear();
        }
    }

    private static class WrappingListener<R>
            extends ForwardingServerCallListener.SimpleForwardingServerCallListener<R> {
        private final String traceId;

        private final String spanId;

        public WrappingListener(ServerCall.Listener<R> delegate, String traceId, String spanId) {
            super(delegate);
            this.traceId = traceId;
            this.spanId = spanId;
        }

        @Override
        public void onMessage(R message) {
            ContextHolder.init(traceId, spanId);
            super.onMessage(message);
        }

        @Override
        public void onHalfClose() {
            ContextHolder.init(traceId, spanId);
            super.onHalfClose();
        }

        @Override
        public void onCancel() {
            ContextHolder.init(traceId, spanId);
            super.onCancel();
        }

        @Override
        public void onComplete() {
            ContextHolder.init(traceId, spanId);
            super.onComplete();
        }

        @Override
        public void onReady() {
            ContextHolder.init(traceId, spanId);
            super.onReady();
        }
    }
}
