package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.Role;
import by.protasovitski.calendariba.domain.entities.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}
