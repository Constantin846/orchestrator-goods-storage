package tk.project.orchestrator.goodsstorage.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDateDto {

    private LocalDate date;
}
