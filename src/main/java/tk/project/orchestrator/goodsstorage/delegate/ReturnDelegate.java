package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReturnDelegate implements JavaDelegate {
    private final RuntimeService runtimeService;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        runtimeService.createProcessInstanceModification(delegateExecution.getProcessInstanceId())
                .startAfterActivity("Activity_PayForOrder")
                .execute();
    }
}
