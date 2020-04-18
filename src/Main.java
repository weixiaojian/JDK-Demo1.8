/**
 * volatile防止指令重排
 * synchronized：保证原子性
 */
class Singleton{
    public static volatile Singleton singleton = null;

    public static Singleton getSingleton(){
        //双端检锁机制
        if(singleton == null){
            synchronized (Singleton.class){
                if(singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                System.out.println(Singleton.getSingleton() == Singleton.getSingleton());
            }, String.valueOf(i)).start();
        }
    }
}
