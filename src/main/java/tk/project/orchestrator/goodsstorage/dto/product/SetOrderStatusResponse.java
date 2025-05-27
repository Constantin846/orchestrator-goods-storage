package tk.project.orchestrator.goodsstorage.dto.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public SetOrderStatusResponse(@JsonProperty("status") final String status) {
        this.status = status;
    }
}
