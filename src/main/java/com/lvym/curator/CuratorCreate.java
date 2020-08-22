package com.lvym.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class CuratorCreate {
    CuratorFramework client = null;

    @Before
    public void before() {
        //重连机制
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString("192.168.168.114:2181,192.168.168.114:2182,192.168.168.114:2183")
                .sessionTimeoutMs(5000)
                .retryPolicy(retry)
                .namespace("create")
                .build();
        client.start();


    }

    @Test
    public void create() throws Exception {
        client.create().withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/node1","213".getBytes());
        System.out.println("结束了");
    }
    @Test
    public void create2() throws Exception {
      List<ACL> list=new ArrayList<ACL>();
        Id id=new Id("ip","192.168.168.114");
        list.add(new ACL(ZooDefs.Perms.ALL,id));

        client.create().withMode(CreateMode.PERSISTENT)
                .withACL(list)
                .forPath("/node2","213".getBytes());
        System.out.println("结束了");
    }
    @Test
    public void create3() throws Exception {
        client.create().creatingParentsIfNeeded()//创建递归节点树
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/node3/node1","213".getBytes());
        System.out.println("结束了");
    }
    @Test
    public void create4() throws Exception {
        //异步
        client.create().creatingParentsIfNeeded()//创建递归节点树
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .inBackground(new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println(curatorEvent.getContext());
                        System.out.println(curatorEvent.getType());
                    }
                })
                .forPath("/node3/node3","213".getBytes());
        Thread.sleep(5000);
        System.out.println("结束了");
    }
    @After
    public void after() {
        client.close();
    }
}
