package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*Поток 1:1  2  3  4  5  6  7  8  9  10    9  8  7   6  5  4  3   2  1  2   3  4....
  Поток 2:  1  2  3  4  5  6  7  8  9    10  9  8  7  6   5   4  3   2  1  2   3....*/
public class NumberWriter {
    private static final Logger logger = LoggerFactory.getLogger(NumberWriter.class);
    private static final int minNumber = 1;
    private static final int maxNumber = 10;
    private int currentThreadNumber = 2;

    private synchronized void action(ThreadInfo threadInfo) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (currentThreadNumber == threadInfo.getThreadNumber()) {
                    this.wait();
                }
                String msg = String.valueOf(threadInfo.getLastNumber());
                //logger.info(msg);
                logger.info("поток " + threadInfo.getThreadNumber() + ", " + msg);

                if (threadInfo.isFlowDirectionPositive()) {
                    if (threadInfo.getLastNumber() == maxNumber) {
                        threadInfo.setFlowDirectionPositive(false);
                    }
                } else {
                    if (threadInfo.getLastNumber() == minNumber) {
                        threadInfo.setFlowDirectionPositive(true);
                    }
                }
                threadInfo.incrementOrDecreaseLastNumber();
                currentThreadNumber = threadInfo.getThreadNumber() == 1 ? 1 : 2;

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        NumberWriter pingPong = new NumberWriter();
        new Thread(() -> pingPong.action(new ThreadInfo(1,true, minNumber))).start();
        new Thread(() -> pingPong.action(new ThreadInfo(2, true, minNumber))).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static class ThreadInfo {
        private final int threadNumber;
        private boolean isFlowDirectionPositive;
        private int lastNumber;

        public ThreadInfo(int threadNumber, boolean isFlowDirectionPositive, int lastNumber) {
            this.threadNumber = threadNumber;
            this.isFlowDirectionPositive = isFlowDirectionPositive;
            this.lastNumber = lastNumber;
        }

        public int getThreadNumber() {
            return threadNumber;
        }

        public boolean isFlowDirectionPositive() {
            return isFlowDirectionPositive;
        }

        public void setFlowDirectionPositive(boolean flowDirectionPositive) {
            isFlowDirectionPositive = flowDirectionPositive;
        }

        public int getLastNumber() {
            return lastNumber;
        }

        public void incrementOrDecreaseLastNumber() {
            if (isFlowDirectionPositive) {
                lastNumber = lastNumber + 1;
            } else {
                lastNumber = lastNumber - 1;
            }
        }
    }
}
