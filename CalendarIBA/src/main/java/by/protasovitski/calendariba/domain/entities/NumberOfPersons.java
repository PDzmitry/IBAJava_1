package by.protasovitski.calendariba.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "num_persons")
public class NumberOfPersons extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Min(value = 1)
    @Max(value = 2)
    @NotNull
    private int subgroup;

    @NotNull
    private int quantity;
}
