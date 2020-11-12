package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Role;
import by.protasovitski.calendariba.domain.entities.User;
import by.protasovitski.calendariba.domain.entities.enums.RoleName;
import by.protasovitski.calendariba.domain.entities.enums.Status;
import by.protasovitski.calendariba.domain.repository.RoleRepository;
import by.protasovitski.calendariba.domain.repository.UserRepository;
import by.protasovitski.calendariba.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        log.info("IN findAll - {} users found",users.size());
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) throws NoSuchElementException {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) throws NoSuchElementException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User " + username + " not found"));
    }

    @Override
    public User save(User user) {
//        Role roleUser = roleRepository.findByName(meRoleNa.ROLE_USER).orElseThrow(() -> new NoSuchElementException("Role  not found"));
//        Set<Role> userRoles = new HashSet<>();
//        userRoles.add(roleUser);
//сделать проверку если пароль null
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(userRoles);
//        user.setStatus(Status.ACTIVE);
        if (user.getId()==null){

        }

        User registeredUser = userRepository.save(user);

        return registeredUser;
    }

    @Override
    public void delete(Long id) {
        final Optional<User> user =
                this.userRepository.findById(id);
        user.ifPresent(u->{
            u.setStatus(Status.DELETED);
            u.getRoles().forEach(r->r.getUsers().remove(u));
            u.getRoles().removeAll(u.getRoles());
            userRepository.save(u);
        });
    }

    @Override
    public User addUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NoSuchElementException("User " + userId +" not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()-> new NoSuchElementException("Role " + roleId + " not found"));
        user.getRoles().add(role);
        role.getUsers().add(user);
        return user;
    }

    @Override
    public void delUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NoSuchElementException("User " + userId +" not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()-> new NoSuchElementException("Role " + roleId + " not found"));
        user.getRoles().remove(role);
        role.getUsers().remove(user);
    }
}
