package by.protasovitski.calendariba.dto;


import by.protasovitski.calendariba.domain.entities.enums.Status;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String email;
    private Status status;
}
