package me.boot.minio.util;

import com.alibaba.fastjson.JSON;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.util.FileUtil;
import me.boot.minio.config.MinioConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Service;

/**
 * MinioService
 *
 * @since 2023/04/02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    /**
     * 查看bucket是否存在
     *
     * @param bucketName /
     * @return Boolean
     */
    @SneakyThrows
    public Boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder()
            .bucket(bucketName)
            .build());
    }

    /**
     * 创建存储bucket
     */
    @SneakyThrows
    public void makeBucket(String bucketName) {
        minioClient.makeBucket(MakeBucketArgs.builder()
            .bucket(bucketName)
            .build());
    }

    /**
     * 删除存储bucket
     */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        minioClient.removeBucket(RemoveBucketArgs.builder()
            .bucket(bucketName)
            .build());
    }

    /**
     * 获取全部bucket
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }


    /**
     * 获取文件的url
     *
     * @param fileName /
     * @return url
     */
    @SneakyThrows
    public String getPresignedObjectUrl(String fileName) {
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
            .bucket(minioConfig.getBucketName()).object(fileName).method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(build);
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    @SneakyThrows
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder().bucket(minioConfig.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        for (Result<Item> result : results) {
            items.add(result.get());
        }
        return items;
    }


    public void uploadObject(File file) {
        try (FileInputStream fileInputStream = FileUtils.openInputStream(file)) {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(file.getName())
                .stream(fileInputStream, FileUtils.sizeOf(file), -1)
                .build();

            //文件名称相同会覆盖
            ObjectWriteResponse response = minioClient.putObject(objectArgs);
            log.info("ObjectWriteResponse : {}", JSON.toJSONString(response));
        } catch (Exception e) {
            log.info("uploadObject error", e);
        }

    }


    /**
     * 文件下载
     *
     * @param bucketName 存储bucket名称
     * @param fileName   文件名称
     */
    public void download(String bucketName, String fileName) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName)
            .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            String path = SystemUtils.getJavaIoTmpDir().getCanonicalPath();
            FileUtil.buildFile(path + fileName, response);
        } catch (Exception e) {
            log.info("download file error", e);
        }
    }


    /**
     * 删除
     *
     * @param fileName /
     */
    @SneakyThrows
    public void remove(String fileName) {
        minioClient.removeObject(RemoveObjectArgs.builder().
            bucket(minioConfig.getBucketName())
            .object(fileName)
            .build());
    }

}

