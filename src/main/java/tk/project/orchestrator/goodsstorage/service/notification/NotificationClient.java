package tk.project.orchestrator.goodsstorage.service.notification;

import tk.project.orchestrator.goodsstorage.dto.CustomerNotificationDto;

public interface NotificationClient {
    void sendCustomerNotification(final CustomerNotificationDto notificationDto);
}
