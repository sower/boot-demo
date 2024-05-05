package me.boot.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boot.datajpa.repository.WebSiteDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * 初始化任务
 *
 * @since 2023/02/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitTask implements CommandLineRunner {

    private final WebSiteDao webSiteDao;

    @Override
    public void run(String... args) {
        log.info("=== Init task done ===");
        ClassPathResource yml = new ClassPathResource("application.yml");
        log.info("Config yml at {}", yml.getPath());
        log.info("Current websites : {}", webSiteDao.findAll());
    }

}
