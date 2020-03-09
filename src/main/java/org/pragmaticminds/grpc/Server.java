package org.pragmaticminds.grpc;

import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class Server extends GreeterGrpc.GreeterImplBase {

    private static io.grpc.Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        server = NettyServerBuilder.forPort(8080)
            .addService(new Server())
            .build()
            .start();

        System.out.println("Server up and Running...");

        server.awaitTermination();
    }


    @Override
    public StreamObserver<HelloRequest> sayHello(final StreamObserver<HelloReply> responseObserver) {
        return new StreamObserver<HelloRequest>() {
            @Override
            public void onNext(HelloRequest helloRequest) {
                responseObserver.onNext(HelloReply.newBuilder().setMessage("Hello " + helloRequest.getName()).build());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

//    @Override
//    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
//        System.out.println("Got Request...");
//        responseObserver.onNext(HelloReply.newBuilder().setMessage("Hello " + request.getName()).build());
//        responseObserver.onNext(HelloReply.newBuilder().setMessage("Hello " + request.getName()).build());
//        responseObserver.onCompleted();
//    }
}
