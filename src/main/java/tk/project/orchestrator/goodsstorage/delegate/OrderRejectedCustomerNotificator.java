package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.CustomerNotificationDto;
import tk.project.orchestrator.goodsstorage.service.notification.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRejectedCustomerNotificator implements JavaDelegate {
    private final NotificationService notificationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Send notification to customer, business key: {}", delegateExecution.getBusinessKey());

        CustomerNotificationDto dto = new CustomerNotificationDto();
        dto.setLogin((String) delegateExecution.getVariable("login"));
        dto.setMessage(String.format(
                "Order with id=%s has been rejected",
                delegateExecution.getVariable("id")
        ));

        notificationService.sendCustomerNotification(dto);
    }
}
