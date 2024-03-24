import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.mq.rabbit.RabbitmqApplication;
import me.boot.mq.rabbit.service.MessagingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(classes = RabbitmqApplication.class)
public class RabbitmqTest {

    @Resource
    private MessagingService messagingService;

    @Test
    public void test() {
        messagingService.senMsg("test-mq");
        messagingService.senMsg("2024-mq");
    }

}
