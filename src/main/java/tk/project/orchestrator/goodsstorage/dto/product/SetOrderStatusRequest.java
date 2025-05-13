package tk.project.orchestrator.goodsstorage.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import tk.project.orchestrator.goodsstorage.enums.OrderStatus;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SetOrderStatusRequest {

    private final UUID orderId;

    private final OrderStatus status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private final LocalDate deliveryDate;
}
