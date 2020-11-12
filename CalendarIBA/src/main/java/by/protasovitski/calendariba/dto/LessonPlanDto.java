package by.protasovitski.calendariba.dto;

import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import lombok.Data;


@Data
public class LessonPlanDto {
    private Long id;
    private int numberOfHours;
    private TypeOfWork typeOfWork;
    private DisciplineDto disciplineDto;
    private TeacherDto teacherDto;
    private DescriptionOfPlanDto descriptionOfPlanDto;
}
