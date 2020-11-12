package by.protasovitski.calendariba.dto;

import lombok.Data;

@Data
public class NewUserDto {
    private Long id;
    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String email;
    private String status;
}
