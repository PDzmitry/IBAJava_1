package by.protasovitski.calendariba.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NumberOfPersonsDto {
    private long id;
    private LocalDate date;
    private GroupDto groupDto;
    private int subgroup;
    private int quantity;
}
