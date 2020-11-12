package by.protasovitski.calendariba.domain.entities;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfEmployment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "disciplines")
public class Teacher extends AbstractAuditingEntity {
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
    private TypeOfEmployment typeOfEmployment;



    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "teacher_discipline",
    joinColumns = @JoinColumn(name = "teacher_id",referencedColumnName = "id"),
    inverseJoinColumns =@JoinColumn(name = "discipline_id",referencedColumnName = "id"))
    private Set<Discipline> disciplines = new HashSet<>();

    private String color;

}
