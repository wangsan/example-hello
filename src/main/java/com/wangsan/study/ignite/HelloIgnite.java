package com.wangsan.study.ignite;

import java.util.Arrays;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSystemProperties;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ConnectorConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.managers.discovery.IgniteDiscoverySpi;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

/**
 * created by wangsan on 2019-06-04 in project of example .
 *
 * @author wangsan
 * @date 2019-06-04
 */
public class HelloIgnite {
    public static void main(String[] args) {
        Ignite ignite = testServer(1);

        CacheConfiguration<String, String> cacheConfiguration = new CacheConfiguration<>();
        cacheConfiguration.setName("hellowangsan");
        cacheConfiguration.setCacheMode(CacheMode.REPLICATED);

        IgniteCache<String, String> cacheOne = ignite.getOrCreateCache(cacheConfiguration);
        String testKey = "test1";
        cacheOne.put(testKey, testKey);
        System.out.println("cache size " + cacheOne.size());
        System.out.println(cacheOne.get(testKey));

        ignite.close();
    }

    public static Ignite testServer(int index) {
        IgniteConfiguration cfg = igniteConfiguration(index);

        return Ignition.start(cfg);
    }

    public static IgniteConfiguration igniteConfiguration(int index) {
        String name = "test_wangsan_" + index;
        System.out.println(name);

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(false);
        cfg.setDiscoverySpi(tcpDiscoverySpi());
        cfg.setGridLogger(new Slf4jLogger());
        cfg.setIgniteInstanceName(name);
        cfg.setConsistentId(name);
        cfg.setGridLogger(new Slf4jLogger());

        cfg.setConnectorConfiguration(new ConnectorConfiguration());
        System.setProperty(IgniteSystemProperties.IGNITE_JETTY_PORT, "8371");
        return cfg;
    }

    public static IgniteDiscoverySpi tcpDiscoverySpi() {
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
        spi.setIpFinder(ipFinder);
        return spi;
    }
}
