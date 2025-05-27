package tk.project.orchestrator.goodsstorage.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import tk.project.orchestrator.goodsstorage.enums.ComplianceStatus;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ComplianceResultDto {

    private final UUID businessKey;

    private final ComplianceStatus complianceStatus;

    @JsonCreator
    public ComplianceResultDto(@JsonProperty("businessKey") UUID businessKey,
                               @JsonProperty("complianceStatus") ComplianceStatus complianceStatus) {
        this.businessKey = businessKey;
        this.complianceStatus = complianceStatus;
    }
}
