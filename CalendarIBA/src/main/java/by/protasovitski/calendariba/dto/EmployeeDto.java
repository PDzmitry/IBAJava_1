package by.protasovitski.calendariba.dto;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfPosition;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDto {

    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private TypeOfPosition typeOfPosition;
    private LocalDate startDate;
    private LocalDate endDate;
    private DepartmentDto departmentDto;
}
