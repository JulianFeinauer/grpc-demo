package org.pragmaticminds.grpc;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws SSLException, InterruptedException {
        final ManagedChannel channel = NettyChannelBuilder
            .forAddress("localhost", 8080)
            .usePlaintext()
            .enableRetry()
            .build();

        // Async
        final StreamObserver<HelloRequest> hello = GreeterGrpc.newStub(channel).sayHello(new StreamObserver<HelloReply>() {
            @Override
            public void onNext(HelloReply helloReply) {
                System.out.println("Reply (from Stream): " + helloReply.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                // do nothing
            }

            @Override
            public void onCompleted() {
                System.out.println("Finished!");
                channel.shutdown();
            }
        });

        hello.onNext(HelloRequest.newBuilder().setName("Julian").build());
        hello.onNext(HelloRequest.newBuilder().setName("Paul").build());
        hello.onCompleted();

        channel.awaitTermination(1, TimeUnit.DAYS);
    }
}
