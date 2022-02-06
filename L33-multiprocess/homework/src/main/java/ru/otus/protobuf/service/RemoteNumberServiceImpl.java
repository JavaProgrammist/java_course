package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.NumberRangeMessage;
import ru.otus.protobuf.generated.RemoteNumberServiceGrpc;

public class RemoteNumberServiceImpl extends RemoteNumberServiceGrpc.RemoteNumberServiceImplBase {

    @Override
    public void getNumbers(NumberRangeMessage request, StreamObserver<NumberMessage> responseObserver) {

        for (int i = request.getStart(); i <= request.getEnd(); i++) {
            responseObserver.onNext(user2UserMessage(i));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }

    private NumberMessage user2UserMessage(int number) {
        return NumberMessage.newBuilder()
                .setNumber(number)
                .build();
    }
}
