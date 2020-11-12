package by.protasovitski.calendariba.domain.entities;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@Table(name = "timetable")
public class TimetableOfClasses extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDate classDate;

    private LocalTime beginTime;

    private LocalTime finishTime;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "subgroup")
    private int subgroup;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Enumerated(value = EnumType.STRING)
    private TypeOfWork typeOfWork;

    @NotNull
    private Boolean status;
    @NotNull
    private Boolean reserved;
}
