package by.protasovitski.calendariba.dto;

import by.protasovitski.calendariba.domain.entities.enums.RoleName;
import lombok.Data;

@Data
public class RoleDto {
    private Long id;
    private RoleName name;
}
