package by.protasovitski.calendariba.dto;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfAddWork;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddWorkOfTeacherDto {

    private Long id;
    private LocalDate date;
    private TeacherDto teacherDto;
    private GroupDto groupDto;
    private TypeOfAddWork typeOfAddWork;
    private Double hour;
}
