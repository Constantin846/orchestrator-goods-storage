package tk.project.orchestrator.goodsstorage.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrchestratorConfirmOrderDto {

    private UUID id;

    private String deliveryAddress;

    private String customerInn;

    private String customerAccountNumber;

    private BigDecimal price;

    private String customerLogin;
}
