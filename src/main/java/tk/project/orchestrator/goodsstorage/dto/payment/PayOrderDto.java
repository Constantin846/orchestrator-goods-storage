package tk.project.orchestrator.goodsstorage.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PayOrderDto {

    private UUID orderId;

    private BigDecimal price;

    private String accountNumber;
}
