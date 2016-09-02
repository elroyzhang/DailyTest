package jstormWorker;
import java.util.Random;
import java.util.concurrent.*;


/**
 * Created by elroy on 16-8-29.
 */

public class PriorityBlockingQueueExample {

    public static void main(String[] args) {

        final BlockingQueue<Integer> queue = new PriorityBlockingQueue<Integer>();
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

    }
}

class Producer implements Runnable {
    BlockingQueue<Integer> queue;
    Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    public void run() {
        for (int i = 0; i<5; i++) {
            try {
                queue.put(new Random().nextInt(10000));
            } catch (Exception e) {
            }
        }
    }
}

class Consumer implements Runnable {
    BlockingQueue<Integer> queue;
    Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    public void run() {
        for (int i = 0; i<5; i++) {
            try {
                System.out.println(queue.take());
            } catch (Exception e) {
            }
        }
    }
}
