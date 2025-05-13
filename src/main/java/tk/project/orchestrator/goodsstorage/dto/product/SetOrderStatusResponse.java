package tk.project.orchestrator.goodsstorage.dto.product;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SetOrderStatusResponse {

    private final String status;
}
