package tk.project.orchestrator.goodsstorage.dto.payment;

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
public class PayOrderDto {

    private final UUID orderId;

    private final BigDecimal price;

    private final String accountNumber;
}
