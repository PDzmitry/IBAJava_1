package by.protasovitski.calendariba.domain.entities;


import by.protasovitski.calendariba.domain.entities.enums.TypeOfPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "lastname")
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "firstname")
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "patronymic")
    private String patronymic;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TypeOfPosition typeOfPosition;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
