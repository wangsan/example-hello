package com.wangsan.study.ignite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.Test;

import com.google.common.base.Stopwatch;

/**
 * created by wangsan on 2019-06-04 in project of example .
 *
 * @author wangsan
 * @date 2019-06-04
 */
public class HelloIgniteTest {

    @Test
    public void testMapReduce() {
        Ignite ignite = HelloIgnite.testServer(1);

        IgniteCompute compute = ignite.compute();
        int cnt = compute.execute(CharacterCountTask.class, "Hello Grid Enabled World!");

        System.out.println(">>> Total number of characters in the phrase is '" + cnt + "'.");
    }

    @Test
    public void testHighLoad() {
        Ignite ignite = HelloIgnite.testServer(1);
        Integer result = ignite.compute().execute(HighLoadTask.class, 5);
        System.out.println(result);
    }

    @Test
    public void testHighLoadPerf() throws InterruptedException {
        Ignite ignite = HelloIgnite.testServer(1);

        int size = 10;
        CountDownLatch latch = new CountDownLatch(size);

        Stopwatch stopwatch = Stopwatch.createStarted();
        // 和外部线程无关，依赖于内部线程
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, size).forEach(i -> {
            executorService.execute(() -> {
                Integer result = ignite.compute().execute(HighLoadTask.class, 4);
                System.out.println(result);
                latch.countDown();
            });
        });

        latch.await();
        stopwatch.stop();

        // 耗时稳定10s
        System.err.println(stopwatch);
    }

    @Test
    public void testHighLoadPerf2() throws InterruptedException {
        IgniteConfiguration configuration = HelloIgnite.igniteConfiguration(1);
        configuration.setPublicThreadPoolSize(20);
        Ignite ignite = Ignition.start(configuration);

        int size = 10;
        CountDownLatch latch = new CountDownLatch(size);

        Stopwatch stopwatch = Stopwatch.createStarted();
        // 和外部线程无关，依赖于内部线程
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, size).forEach(i -> {
            executorService.execute(() -> {
                Integer result = ignite.compute().execute(HighLoadTask.class, 4);
                System.out.println(result);
                latch.countDown();
            });
        });

        latch.await();
        stopwatch.stop();

        // 耗时稳定4s
        System.err.println(stopwatch);
    }

    /**
     * Task to count non-white-space characters in a phrase.
     */
    private static class CharacterCountTask extends ComputeTaskSplitAdapter<String, Integer> {
        // 1. Splits the received string into to words
        // 2. Creates a child job for each word
        // 3. Sends created jobs to other nodes for processing.
        @Override
        public List<ComputeJob> split(int gridSize, String arg) {
            String[] words = arg.split(" ");

            List<ComputeJob> jobs = new ArrayList<>(words.length);

            for (final String word : arg.split(" ")) {
                jobs.add(new ComputeJobAdapter() {
                    @Override
                    public Object execute() {
                        System.out.println(">>> Printing '" + word + "' on from compute job.");

                        // Return number of letters in the word.
                        return word.length();
                    }
                });
            }

            return jobs;
        }

        @Override
        public Integer reduce(List<ComputeJobResult> results) {
            int sum = 0;

            for (ComputeJobResult res : results) {
                sum += res.<Integer>getData();
            }

            return sum;
        }
    }

    /**
     * Task to count non-white-space characters in a phrase.
     */
    private static class CharacterCountTask2 extends ComputeTaskAdapter<String, Integer> {
        // 1. Splits the received string into to words
        // 2. Creates a child job for each word
        // 3. Sends created jobs to other nodes for processing.
        @Override
        public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> subgrid, String arg) {
            String[] words = arg.split(" ");

            Map<ComputeJob, ClusterNode> map = new HashMap<>(words.length);

            Iterator<ClusterNode> it = subgrid.iterator();

            for (final String word : arg.split(" ")) {
                // If we used all nodes, restart the iterator.
                if (!it.hasNext()) {
                    it = subgrid.iterator();
                }

                ClusterNode node = it.next();

                map.put(new ComputeJobAdapter() {
                    @Override
                    public Object execute() {
                        System.out.println(">>> Printing '" + word + "' on this node from grid job.");
                        return word.length();
                    }
                }, node);
            }

            return map;
        }

        @Override
        public Integer reduce(List<ComputeJobResult> results) {
            int sum = 0;

            for (ComputeJobResult res : results) {
                sum += res.<Integer>getData();
            }

            return sum;
        }
    }

    private static class HighLoadTask extends ComputeTaskSplitAdapter<Integer, Integer> {
        @Override
        public List<ComputeJob> split(int gridSize, Integer arg) {
            List<ComputeJob> jobs = new ArrayList<>();
            for (int i = 0; i < arg; i++) {
                int finalI = i;
                jobs.add(new ComputeJobAdapter() {
                    @Override
                    public Object execute() {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return finalI * 2;
                    }
                });
            }

            return jobs;
        }

        @Override
        public Integer reduce(List<ComputeJobResult> results) {
            int sum = 0;

            for (ComputeJobResult res : results) {
                sum += res.<Integer>getData();
            }

            return sum;
        }
    }
}