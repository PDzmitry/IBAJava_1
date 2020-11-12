package by.protasovitski.calendariba.domain.entities;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfCourse;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfEducation;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "group_educ")
public class Group extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @Column(name = "num_subgroup", length = 1)
    @Min(value = 1)
    @Max(value = 2)
    @NotNull
    private int numberOfSubgroup;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TypeOfEducation typeOfEducation;

    @OneToOne
//    @ManyToOne
//            (fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "descriptionOfPlan_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
   /* @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("descriptionOfPlan_id")*/
    private DescriptionOfPlan descriptionOfPlan;

    private String color;


}
