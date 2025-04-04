package tk.project.orchestrator.goodsstorage.dto.product;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SetOrderStatusDto {

    private UUID orderId;

    private OrderStatus status;

    private LocalDate deliveryDate;
}
