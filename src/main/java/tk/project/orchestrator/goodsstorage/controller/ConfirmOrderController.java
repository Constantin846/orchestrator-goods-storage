package tk.project.orchestrator.goodsstorage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.project.orchestrator.goodsstorage.dto.ComplianceStatus;
import tk.project.orchestrator.goodsstorage.dto.OrchestratorConfirmOrderDto;

import java.util.UUID;

@Slf4j
@RestController("/confirm-order")
@RequiredArgsConstructor
public class ConfirmOrderController {
    private static final String PROCESS_KEY = "ConfirmOrderProcessKey";
    private final RuntimeService runtimeService;

    @PostMapping
    public String confirmOrder(@RequestBody OrchestratorConfirmOrderDto orderDto) {
        log.info("Request to confirm order with id: {}", orderDto.getId());
        return runtimeService
                .createProcessInstanceByKey(PROCESS_KEY)
                .businessKey(UUID.randomUUID().toString())
                .setVariable("id", orderDto.getId())
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
        log.info("Continue after message from compliance with status: {}", status);
        runtimeService
                .createMessageCorrelation("continueProcessMsg")
                .processInstanceBusinessKey(processId.toString())
                .setVariable("complianceStatus", status.name())
                .correlate();
    }
}
