package com.lvym.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyLock {

    CuratorFramework client = null;

    @Before
    public void before() {
        //重连机制
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString("192.168.168.114:2181,192.168.168.114:2182,192.168.168.114:2183")
                .sessionTimeoutMs(5000)
                .retryPolicy(retry)
                .build();
        client.start();


    }

    @After
    public void after() {
        client.close();
    }

    @Test
    public void getLock() throws Exception {
      //排他锁
        InterProcessMutex interProcessMutex = new InterProcessMutex(client, "/lock1");
       //获取锁
        interProcessMutex.acquire();
        for (int i=0;i<6;i++){
            Thread.sleep(3000);
            System.out.println("获得锁:"+i);
        }
        //释放锁
        interProcessMutex.release();
        System.out.println("释放锁");
    }
    @Test
    public void getLock2() throws Exception {
        //读写锁
        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(client, "/lock1");
        //获取读锁
        InterProcessMutex interProcessMutex =interProcessReadWriteLock.readLock();

        interProcessMutex.acquire();
        for (int i=0;i<6;i++){
            Thread.sleep(3000);
            System.out.println("获得读锁:"+i);
        }
        //释放锁
        interProcessMutex.release();
        System.out.println("释放读锁");
    }
    @Test
    public void getLock3() throws Exception {
        //读写锁
        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(client, "/lock1");
        //获取写锁
        InterProcessMutex interProcessMutex = interProcessReadWriteLock.writeLock();
        interProcessMutex.acquire();
        for (int i=0;i<6;i++){
            Thread.sleep(3000);
            System.out.println("获得写锁:"+i);
        }
        //释放锁
        interProcessMutex.release();
        System.out.println("释放写锁");
    }

}
