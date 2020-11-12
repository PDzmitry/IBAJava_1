package by.protasovitski.calendariba.dto;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfCourse;
import lombok.Data;

@Data
public class DescriptionOfPlanDto {
    private Long id;
    private String description;
    private TypeOfCourse typeOfCourse;
}
