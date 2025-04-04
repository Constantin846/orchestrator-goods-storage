package tk.project.orchestrator.goodsstorage.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.CustomerNotificationDto;

@Slf4j
@Service
@ConditionalOnExpression("'${app.notification-service.type}'.equals('mock')")
public class NotificationServiceMock implements NotificationService {

    @Override
    public void sendCustomerNotification(CustomerNotificationDto notificationDto) {
    }
}
