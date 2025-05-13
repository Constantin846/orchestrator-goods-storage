package tk.project.orchestrator.goodsstorage.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ComplianceDto {

    private final String login;

    private final String inn;

    private final String businessKey;
}
