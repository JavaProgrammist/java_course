package ru.otus.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.NumberRangeMessage;
import ru.otus.protobuf.generated.RemoteNumberServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        RemoteNumberServiceGrpc.RemoteNumberServiceStub newStub = RemoteNumberServiceGrpc.newStub(channel);
        NumberRangeMessage clientRangeMessage = NumberRangeMessage.newBuilder().setStart(0).setEnd(30).build();
        final AtomicInteger[] lastAtomicNumberFromServer = new AtomicInteger[1];
        newStub.getNumbers(clientRangeMessage, new StreamObserver<>() {
            @Override
            public void onNext(NumberMessage nm) {
                lastAtomicNumberFromServer[0] = new AtomicInteger(nm.getNumber());
                System.out.println("   new value: " + nm.getNumber());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("\nrequest completed");
                latch.countDown();
            }
        });

        Integer prevLastNumberFromServer = null;
        int prevCurrentValue = 0;
        int currentValue;
        for (int i = 1; i <= 50; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Integer lastNumberFromServer = lastAtomicNumberFromServer[0] == null ? null :
                    lastAtomicNumberFromServer[0].get();
            currentValue = prevCurrentValue;
            if (lastNumberFromServer != null && (prevLastNumberFromServer == null ||
                    !prevLastNumberFromServer.equals(lastNumberFromServer))) {
                currentValue += lastNumberFromServer;
                prevLastNumberFromServer = lastNumberFromServer;
            }
            currentValue += 1;
            prevCurrentValue = currentValue;
            System.out.println("currentValue: " + currentValue);
        }
        latch.await();

        channel.shutdown();
    }
}
