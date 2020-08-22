package com.lvym.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Watch {
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
    @Test
    public void watch() throws Exception {
        final NodeCache nodeCache=new NodeCache(client,"/watch1");
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("监听回调"+nodeCache.getCurrentData().getPath());
            }
        });
        Thread.sleep(10000);
        System.out.println("ending");
        nodeCache.close();
    }
    @Test
    public void watch1() throws Exception {
         PathChildrenCache pathChildrenCache=new PathChildrenCache(client,"/watch1",true);
        pathChildrenCache.start();
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println("监听回调"+new String(pathChildrenCacheEvent.getData().getData()));
                System.out.println(pathChildrenCacheEvent.getType());
            }
        });
        Thread.sleep(30000);
        System.out.println("ending");
        pathChildrenCache.close();
    }

    @Test
    public void inTransaction1() throws Exception {

        client.inTransaction().create().forPath("/node","123".getBytes())
                .and()
                .setData().forPath("/node2","0".getBytes())
                .and()
                .commit();
        System.out.println("ending");
    }


    @After
    public void after() {
        client.close();
    }
}
