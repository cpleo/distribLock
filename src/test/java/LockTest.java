import cn.chen.distribLock;

public class LockTest {

    public static void main(String[] args) {
        try {

            for(int i=0;i<5;i++){
                new TestThread().start();
                new SleepTestThread().start();
                new Sleep2TestThread().start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class TestThread extends Thread  {

        @Override
        public void run() {
            distribLock.lock("test");
            System.out.println(this.getName()+"获得锁");

            System.out.println(this.getName()+"释放锁");
            distribLock.unlock("test");

        }
    }

    static class SleepTestThread extends Thread  {

        @Override
        public void run() {
            distribLock.lock("test",11);
            System.out.println(this.getName()+"获得锁,休息10秒");
            try {
                sleep(10000);
                System.out.println("10秒过去，再去获取锁，锁持有时间延长");
                distribLock.lock("test",11);
                sleep(10000);
                System.out.println("10秒过去，再去获取锁，锁持有时间延长");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(this.getName()+"释放锁");
            distribLock.unlock("test");

        }
    }

    static class Sleep2TestThread extends Thread  {

        @Override
        public void run() {
            distribLock.lock("test",5L);
            System.out.println(this.getName()+"获得锁,休息10秒,5秒自动失效");
            try {
                sleep(4000);
                System.out.println(this.getName()+" 4秒过去自动释放锁");
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(this.getName()+"释放锁");
            distribLock.unlock("test");

        }
    }
}
