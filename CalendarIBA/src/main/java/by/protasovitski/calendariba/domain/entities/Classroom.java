package by.protasovitski.calendariba.domain.entities;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfClassroom;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Classroom extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "number")
    private String number;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TypeOfClassroom typeOfClassroom;

    private String color;
}
