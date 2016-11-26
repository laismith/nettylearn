package Lock;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ryan on 2016/11/26.
 */
public class TestLock {
    @Test
    public void test() throws Exception {
        Lock l = new ReentrantLock();


        l.lock();
        l.unlock();




    }
}
