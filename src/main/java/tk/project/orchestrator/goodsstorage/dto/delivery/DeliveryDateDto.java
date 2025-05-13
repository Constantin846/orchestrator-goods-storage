package tk.project.orchestrator.goodsstorage.dto.delivery;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DeliveryDateDto {

    private final LocalDate date;

    @JsonCreator
    public DeliveryDateDto(@JsonProperty("date") final LocalDate date) {
        this.date = date;
    }
}
