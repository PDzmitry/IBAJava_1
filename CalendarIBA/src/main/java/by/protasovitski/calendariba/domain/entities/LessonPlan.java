package by.protasovitski.calendariba.domain.entities;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
public class LessonPlan extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "num_hours")
    private int numberOfHours;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TypeOfWork typeOfWork;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "discipline_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
   /* @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("discipline_id")*/
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "teacher_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Teacher teacher;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "descriptionOfPlan_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
   /* @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("descriptionOfPlan_id")*/
    private DescriptionOfPlan descriptionOfPlan;
}
