package me.boot.base.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

/**
 * CommonUtils
 *
 * @since 2023/07/31
 */
@Slf4j
public class CommonUtils {

    public static List<String> getAllClassNames() throws IOException {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(
            ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "me/boot/**/*.class");
        return Arrays.stream(resources).map(resource -> {
            try {
                return new SimpleMetadataReaderFactory().getMetadataReader(resource)
                    .getClassMetadata().getClassName();
            } catch (IOException e) {
                log.warn("Failed to get class for resource {}", resource, e);
            }
            return null;
        }).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    public static boolean isJavaBean(Class<?> clazz) {
        if (BeanUtils.isSimpleProperty(clazz)) {
            return false;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            return ArrayUtils.isNotEmpty(beanInfo.getPropertyDescriptors());
        } catch (Exception e) {
            log.warn("Failed to get bean info for class {}", clazz, e);
        }
        return false;
    }

    public static boolean isAllJavaBean(Object... bean) {
        for (Object object : bean) {
            if (!isJavaBean(object.getClass())) {
                return false;
            }
        }
        return true;
    }

}
