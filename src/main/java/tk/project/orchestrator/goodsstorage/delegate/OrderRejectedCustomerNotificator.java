package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.CustomerNotificationDto;
import tk.project.orchestrator.goodsstorage.service.notification.NotificationClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRejectedCustomerNotificator implements JavaDelegate {
    private final NotificationClient notificationClient;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        log.info("Send notification to customer, business key: {}", delegateExecution.getBusinessKey());

        final CustomerNotificationDto dto = CustomerNotificationDto.builder()
                .login((String) delegateExecution.getVariable("login"))
                .message(String.format(
                        "Order with id=%s has been rejected",
                        delegateExecution.getVariable("id"))
                ).build();

        notificationClient.sendCustomerNotification(dto);
    }
}
