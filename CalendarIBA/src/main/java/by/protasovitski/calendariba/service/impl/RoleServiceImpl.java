package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.Role;
import by.protasovitski.calendariba.domain.entities.enums.RoleName;
import by.protasovitski.calendariba.domain.repository.RoleRepository;
import by.protasovitski.calendariba.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        log.info("IN findAll - {} roles found",roles.size());
        return roles;
    }

    @Override
    public List<Role> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        return null;
    }

    @Override
    public Role findById(Long id) throws NoSuchElementException {
        return roleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Role " + id + " not found"));
    }

    @Override
    public Role findByRolename(String rolename) throws NoSuchElementException {
        RoleName role;
        switch (rolename){
            case "ROLE_ADMIN" :{
                role=RoleName.ROLE_ADMIN;
                break;
            }
            case "ROLE_USER" :{
                role=RoleName.ROLE_USER;
                break;
            }
            case "ROLE_MANAGER" :{
                role=RoleName.ROLE_MANAGER;
                break;
            }
            case "ROLE_VIEW" :{
                role = RoleName.ROLE_VIEW;
                break;
            }
            case "ROLE_GROUP" :{
                role = RoleName.ROLE_GROUP;
                break;
            }
            default:
                role=null;
        }
        return roleRepository.findByName(role).orElseThrow(() -> new NoSuchElementException("Role " + rolename + " not found"));
    }
}
