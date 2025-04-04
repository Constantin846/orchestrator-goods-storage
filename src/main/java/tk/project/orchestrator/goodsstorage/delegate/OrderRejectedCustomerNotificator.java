package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.CustomerNotificationDto;
import tk.project.orchestrator.goodsstorage.service.notification.NotificationService;

@Component
@RequiredArgsConstructor
public class OrderRejectedCustomerNotificator implements JavaDelegate {
    private final NotificationService notificationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        CustomerNotificationDto dto = new CustomerNotificationDto();
        dto.setLogin((String) delegateExecution.getVariable("login"));
        dto.setMessage(String.format(
                "Order with id=%s has been rejected",
                delegateExecution.getVariable("id")
        ));

        notificationService.sendCustomerNotification(dto);
    }
}
