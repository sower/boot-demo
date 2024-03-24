import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.data.redisson.DataRedissonApplication;
import me.boot.data.redisson.util.RedissonUtils;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBoundedBlockingQueue;
import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(classes = DataRedissonApplication.class)
public class RedissonTest {

    @Resource
    private RedissonClient client;


    @Test
    public void objectTest() {

        RedissonUtils.setStringIfAbsent("adc","mate", Duration.ofMinutes(5));
        String adc = RedissonUtils.getString("adc");
        System.err.println(adc);
        System.err.println(RedissonUtils.ttl("adc"));
        System.err.println(RedissonUtils.ttl("Rain20240114"));

        for (int i = 0; i < 3; i++) {
            System.err.println(RedissonUtils.generateId("Rain"));
        }

    }

    private void printKeys() {
        RKeys keys = client.getKeys();
        keys.getKeys().forEach(System.err::println);
    }

    @Test
    public void queueTest() {
        printKeys();

        RBoundedBlockingQueue<String> queue = client.getBoundedBlockingQueue("king");
//        if (queue.isExists()) {
//            queue.delete();
//        }
//        System.err.println(queue.trySetCapacity(100000));
//        System.err.println(queue.remainingCapacity());
        List<Integer> integers = new ArrayList<>(1000);
        for (int i = 0; i < 99; i++) {
            integers.add(i);
        }
        ArrayList<List<Integer>> lists = Lists.newArrayList();
        for (int i = 0; i < 24; i++) {
            lists.add(ImmutableList.copyOf(integers));
        }
        lists.parallelStream().forEach(ints -> ints.forEach(i -> queue.poll()));
        System.err.println(queue.size());


    }

    @Test
    public void lockTest() throws InterruptedException {
        String key = "lock-test";
        tryLock(key);
        // async exec tryLock
        CompletableFuture.runAsync(() -> {
            try {
                tryLock(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(3000);
    }

    private void tryLock(String key) throws InterruptedException {
        RLock lock = client.getLock(key);
        boolean res = lock.tryLock(3, 100, TimeUnit.SECONDS);
        if (!res) {
            System.err.println("not get lock");
            return;
        }
        try {
            System.err.println("get lock");

        } finally {
//            lock.unlock();
        }
    }


}
