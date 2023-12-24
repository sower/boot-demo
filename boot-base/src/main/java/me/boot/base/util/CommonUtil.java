package me.boot.base.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

/**
 * @description
 * @date 2023/07/31
 **/
@Slf4j
public class CommonUtil {

    public static List<String> getAllClassNames() throws IOException {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(
            ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "me/boot/**/*.class");
        return Arrays.stream(resources).map(
            resource -> {
                try {
                    return new SimpleMetadataReaderFactory().getMetadataReader(resource)
                        .getClassMetadata()
                        .getClassName();
                } catch (IOException e) {
                    log.warn("Failed to get class for resource {}", resource, e);
                }
                return null;
            }
        ).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

}
