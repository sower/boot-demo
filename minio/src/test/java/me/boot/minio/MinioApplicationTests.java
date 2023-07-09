package me.boot.minio;

import io.minio.messages.Bucket;
import io.minio.messages.Item;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.minio.util.MinioUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = MinioApplication.class)
class MinioApplicationTests {

    @Resource
    private MinioUtil minioUtil;

    @Test
    void minioTest() {
        Boolean exists = minioUtil.bucketExists("test1");
        log.info("adc bucket exist: " + exists.toString());

        List<Bucket> buckets = minioUtil.listBuckets();
        log.info("buckets: " + buckets);
        minioUtil.uploadObject(FileUtils.getFile("E:\\Download\\person.jpg"));

        List<Item> items = minioUtil.listObjects();
        log.info("Objects: " + items);

        String presignedObjectUrl = minioUtil.getPresignedObjectUrl("person.jpg");
        log.info("presignedObjectUrl: " + presignedObjectUrl);
    }

}
