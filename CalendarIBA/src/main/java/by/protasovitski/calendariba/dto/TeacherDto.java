package by.protasovitski.calendariba.dto;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfEmployment;
import lombok.Data;

@Data
public class TeacherDto {

    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private TypeOfEmployment typeOfEmployment;
    private String color;

}
