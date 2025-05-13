package tk.project.orchestrator.goodsstorage.dto.payment;

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
public class PaymentResultDto {

    private final Boolean isSuccessful;

    @JsonCreator
    public PaymentResultDto(@JsonProperty("isSuccessful") final Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
}
