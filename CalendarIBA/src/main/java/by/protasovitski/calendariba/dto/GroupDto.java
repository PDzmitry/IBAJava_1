package by.protasovitski.calendariba.dto;


import by.protasovitski.calendariba.domain.entities.enums.TypeOfCourse;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfEducation;
import lombok.Data;

@Data
public class GroupDto {
    private long id;
    private String groupName;
    private short numberOfSubgroup;
    private TypeOfEducation typeOfEducation;
    private DescriptionOfPlanDto descriptionOfPlanDto;
    private String color;
}
