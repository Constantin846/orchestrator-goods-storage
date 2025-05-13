package tk.project.orchestrator.goodsstorage.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.CustomerNotificationDto;

@Slf4j
@Service
@ConditionalOnExpression("'${app.notification-client.type}'.equals('mock')")
public class NotificationClientMock implements NotificationClient {

    @Override
    public void sendCustomerNotification(final CustomerNotificationDto notificationDto) {
        log.info("NotificationServiceMock send customer notification");
    }
}
