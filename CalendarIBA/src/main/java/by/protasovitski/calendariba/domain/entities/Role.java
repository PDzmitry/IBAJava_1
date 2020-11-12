package by.protasovitski.calendariba.domain.entities;

import by.protasovitski.calendariba.domain.entities.enums.RoleName;
import by.protasovitski.calendariba.domain.entities.enums.Status;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@ToString(exclude = "users")
public class Role extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length  = 60)
    private RoleName name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

}
