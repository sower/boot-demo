package me.boot.minio;

import io.minio.messages.Bucket;
import io.minio.messages.Item;
import java.util.List;
import javax.annotation.Resource;
import me.boot.minio.util.MinioUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MinioApplication.class)
class MinioApplicationTests {

  @Resource
  private MinioUtil minioUtil;

  @Test
  void minioTest() {
    Boolean exists = minioUtil.bucketExists("test1");
    System.out.println("adc bucket exist: " + exists.toString());

    List<Bucket> buckets = minioUtil.listBuckets();
    System.out.println("buckets: " + buckets);
    minioUtil.uploadObject(FileUtils.getFile("E:\\Download\\person.jpg"));

    List<Item> items = minioUtil.listObjects();
    System.out.println("Objects: " + items);

    String presignedObjectUrl = minioUtil.getPresignedObjectUrl("person.jpg");
    System.out.println("presignedObjectUrl: " + presignedObjectUrl);
  }

}
