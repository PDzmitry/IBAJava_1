package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.entities.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {

    List<User> findAll();

    List<User> findAll(Integer pageNo, Integer pageSize, String sortBy);

    User findById(Long id) throws NoSuchElementException;

    User findByUsername(String username) throws NoSuchElementException;

    User save(User user);

    void delete(Long id);

    User addUserRole(Long userId, Long roleId);

    void delUserRole(Long userId, Long roleId);
}
