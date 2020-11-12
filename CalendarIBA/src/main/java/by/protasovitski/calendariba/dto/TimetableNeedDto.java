package by.protasovitski.calendariba.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimetableNeedDto {
    private TimetableOfClassesDto timetableOfClassesDto;
    private Integer need;
}
