package tk.project.orchestrator.goodsstorage.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SetOrderStatusRequest {

    private UUID orderId;

    private OrderStatus status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deliveryDate;
}
