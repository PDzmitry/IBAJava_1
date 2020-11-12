package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.NumberOfPersons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NumberOfPersonsRepository  extends JpaRepository<NumberOfPersons,Long> {


    List<NumberOfPersons> findByGroup(Group group);


    /*@Query("SELECT n FROM NumberOfPersons n WHERE n.group= :group and n.subgroup= :subgroup"+
            " and n.date=(select MAX (date) from NumberOfPersons p where p.group= :group and p.subgroup= :subgroup and (p.date>=:sDate and p.date<=:fDate))")*/
    @Query("SELECT n FROM NumberOfPersons n WHERE n.group= :group and n.subgroup= :subgroup" +
            " and n.date=(select MAX (p.date)from NumberOfPersons p where p.group=:group and p.subgroup=:subgroup and p.date<=:date)")
    Optional<NumberOfPersons> findNumberOfPersons(
            @Param("group")Group group,
            @Param("subgroup") int subgroup,
            @Param("date") LocalDate date);


}
