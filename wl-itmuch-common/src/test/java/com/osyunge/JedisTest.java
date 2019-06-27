package com.osyunge;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashSet;


public class JedisTest {



    @Test
    public void testJedisSingle() {
        //创建一个jedis的对象。
        Jedis jedis = new Jedis("47.103.16.213", 6379);
        //调用jedis对象的方法，方法名称和redis的命令一致。
        jedis.set("key1", "jedis test");
        String string = jedis.get("key1");
        System.out.println(string);
        //关闭jedis。
        jedis.close();
    }

    @Test
    public void testJedisPool(){
        JedisPool pool = new JedisPool("47.103.16.213", 6379);
        Jedis jedis = pool.getResource();

        String s = jedis.get("key1");

        System.out.println(s);

        jedis.close();
        pool.close();
    }

   /* @Test
    public void testJedisCluster() throws IOException {
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("47.103.16.213", 7000));
        nodes.add(new HostAndPort("47.103.16.213", 7001));
        nodes.add(new HostAndPort("47.103.16.213", 7002));
        nodes.add(new HostAndPort("47.103.16.213", 7003));
        nodes.add(new HostAndPort("47.103.16.213", 7004));
        nodes.add(new HostAndPort("47.103.16.213", 7005));

        JedisCluster cluster = new JedisCluster(nodes);

        cluster.set("key1","1000");
        String s = cluster.get("key1");
        System.out.println(s);

        cluster.close();

    }*/

}
