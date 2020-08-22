package com.lvym.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;

public class CuratorConnection {
    public static void main(String[] args) {
        CuratorFramework client= CuratorFrameworkFactory.builder()
                .connectString("192.168.168.114:2181,192.168.168.114:2182,192.168.168.114:2183")
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryOneTime(3000))
                .namespace("create")
                .build();
        client.start();
        System.out.println(client.isStarted());
        client.close();
    }
}
