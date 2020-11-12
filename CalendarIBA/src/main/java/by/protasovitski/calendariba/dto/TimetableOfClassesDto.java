package by.protasovitski.calendariba.dto;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfWorkSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimetableOfClassesDto {

    private Long id;
    private LocalDate classDate;
    private LocalTime beginTime;
    private LocalTime finishTime;
    private int pairNumber;
    private int lessonNumber;
    private GroupDto groupDto;
    private int subgroup;
    private DisciplineDto disciplineDto;
    private TypeOfWork typeOfWork;
    private ClassroomDto classroomDto;
    private TeacherDto teacherDto;
    private Boolean status;
    private Boolean reserved;
}
