package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Role;
import by.protasovitski.calendariba.domain.entities.enums.RoleName;

import java.util.List;
import java.util.NoSuchElementException;

public interface RoleService {
    List<Role> findAll();

    List<Role> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Role findById(Long id) throws NoSuchElementException;

    Role findByRolename(String roleName) throws NoSuchElementException;

}
