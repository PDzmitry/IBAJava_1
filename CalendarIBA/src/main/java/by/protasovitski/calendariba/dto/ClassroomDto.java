package by.protasovitski.calendariba.dto;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfClassroom;
import lombok.Data;

@Data
public class ClassroomDto {

    private Long id;
    private String number;
    private TypeOfClassroom typeOfClassroom;
    private String color;

}
