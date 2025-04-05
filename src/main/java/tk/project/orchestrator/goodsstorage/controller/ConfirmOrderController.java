package tk.project.orchestrator.goodsstorage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.project.orchestrator.goodsstorage.dto.ComplianceResultDto;
import tk.project.orchestrator.goodsstorage.dto.OrchestratorConfirmOrderDto;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/confirm-order")
@RequiredArgsConstructor
public class ConfirmOrderController {
    private static final String PROCESS_KEY = "ConfirmOrderProcessKey";
    private static final String BUSINESS_KEY = "businessKey";
    private final RuntimeService runtimeService;

    @PostMapping
    public Map<String, String> confirmOrder(@RequestBody OrchestratorConfirmOrderDto orderDto) {
        log.info("Request to confirm order with id: {}", orderDto.getId());
        return Map.of(BUSINESS_KEY,
                runtimeService
                        .createProcessInstanceByKey(PROCESS_KEY)
                        .businessKey(UUID.randomUUID().toString())
                        .setVariable("id", orderDto.getId())
                        .setVariable("deliveryAddress", orderDto.getDeliveryAddress())
                        .setVariable("inn", orderDto.getCustomerInn())
                        .setVariable("accountNumber", orderDto.getCustomerAccountNumber())
                        .setVariable("price", orderDto.getPrice())
                        .setVariable("login", orderDto.getCustomerLogin())
                        .setVariable("errorFlag", false)
                        .execute()
                        .getBusinessKey()
        );
    }

    @PostMapping("/continue")
    public void messageFromCompliance(@RequestBody ComplianceResultDto complianceResultDto) {
        log.info("Continue after message from compliance with status: {}", complianceResultDto);
        runtimeService
                .createMessageCorrelation("continueProcessMsg")
                .processInstanceBusinessKey(complianceResultDto.getBusinessKey().toString())
                .setVariable("complianceStatus", complianceResultDto.getComplianceStatus().name())
                .correlate();
    }
}
