/**
 * Deadlock.
 * See JLS 12.4.2
 * See JVM Specs 5.5
 * See http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4891511
 *
 *
 Thread Dump

 "ChildClassThread" #11 prio=5 os_prio=31 tid=0x00007f95a311d000 nid=0x5103 in Object.wait() [0x000070000bfe7000]
 java.lang.Thread.State: RUNNABLE
 at com.example.InitDeadLockExample$2.run(InitDeadLockExample.java:40)

 "ParentClassThread" #10 prio=5 os_prio=31 tid=0x00007f95a311c800 nid=0x4f03 in Object.wait() [0x000070000bee4000]
 java.lang.Thread.State: RUNNABLE
 at com.example.ParentClass.<clinit>(InitDeadLockExample.java:57)
 at com.example.InitDeadLockExample$1.run(InitDeadLockExample.java:34)

 PayAttention, both thread has RUNNABLE state;
 From Thread.State docs:
 A thread in the runnable
 state is executing in the Java virtual machine but it may
 be waiting for other resources from the operating system
 such as processor.

*/
public class InitDeadLockExample {
    public static void main(String[] args) {
        new Thread("ParentClassThread") {
            @Override
            public void run() {
                System.out.println(ParentClass.NAME);
            }
        }.start();
        new Thread("ChildClassThread") {
            @Override
            public void run() {
                System.out.println(ChildClass.NAME);
            }
        }.start();
    }
}


abstract class ParentClass {

    public static String NAME = "";
    static {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static final ParentClass PARENT_CLASS = new ChildClass();


}

class ChildClass extends ParentClass {
    public static String NAME = "";
}
