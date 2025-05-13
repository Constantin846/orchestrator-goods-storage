package tk.project.orchestrator.goodsstorage.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OrchestratorConfirmOrderDto {

    private final UUID id;

    private final String deliveryAddress;

    private final String customerInn;

    private final String customerAccountNumber;

    private final BigDecimal price;

    private final String customerLogin;
}
