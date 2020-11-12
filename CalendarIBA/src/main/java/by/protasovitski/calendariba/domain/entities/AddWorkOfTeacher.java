package by.protasovitski.calendariba.domain.entities;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfAddWork;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "addwork")
public class AddWorkOfTeacher extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @NotNull
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @NotNull
    private Group group;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TypeOfAddWork typeOfAddWork;

    @NotNull
    private Double hour;

}
