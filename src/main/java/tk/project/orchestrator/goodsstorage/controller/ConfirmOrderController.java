package tk.project.orchestrator.goodsstorage.controller;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.project.orchestrator.goodsstorage.dto.ComplianceStatus;
import tk.project.orchestrator.goodsstorage.dto.OrchestratorConfirmOrderDto;

import java.util.UUID;

@RestController("/confirm-order")
@RequiredArgsConstructor
public class ConfirmOrderController {
    private static final String PROCESS_KEY = "ConfirmOrderProcessKey";
    private final RuntimeService runtimeService;

    @PostMapping
    public String confirmOrder(@RequestBody OrchestratorConfirmOrderDto orderDto) {
        return runtimeService
                .createProcessInstanceByKey(PROCESS_KEY)
                .businessKey(UUID.randomUUID().toString())
                .setVariable("deliveryAddress", orderDto.getDeliveryAddress())
                .setVariable("inn", orderDto.getCustomerInn())
                .setVariable("accountNumber", orderDto.getCustomerAccountNumber())
                .setVariable("price", orderDto.getPrice())
                .setVariable("login", orderDto.getCustomerLogin())
                .execute()
                .getBusinessKey();
    }

    @PostMapping("continue")
    public void messageFromCompliance(@RequestParam UUID processId, @RequestParam ComplianceStatus status) {
        runtimeService
                .createMessageCorrelation("continueProcessMsg")
                .processInstanceBusinessKey(processId.toString())
                .setVariable("complianceStatus", status.name())
                .correlate();
    }
}
