package worker;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by elroy on 16-8-29.
 */

public class AtomicBooleanTest implements Runnable{

        private static AtomicBoolean exists = new AtomicBoolean(false);

        private String name;

        public AtomicBooleanTest(String name) {
            this.name = name;
        }

        public void run() {
            if (exists.compareAndSet(false,true)) {
                //同时会执行这块指令
                System.out.println(name + " enter");
                System.out.println(name + " working");
                System.out.println(name + " leave");
            } else {
                System.out.println(name + " give up");
            }
        }

        public static void main(String[] args)
        {
            AtomicBooleanTest at1 = new AtomicBooleanTest("at1");
            new Thread(at1).start();
            AtomicBooleanTest at2 = new AtomicBooleanTest("at2");
            new Thread(at2).start();
        }
}
