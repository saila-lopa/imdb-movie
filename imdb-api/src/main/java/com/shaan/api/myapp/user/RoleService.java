package com.shaan.api.myapp.user;

import com.shaan.api.myapp.BaseComponent;
import com.shaan.api.myapp.domain.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService extends BaseComponent {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleDao;

    public Roles getRoleByName(String name) {
        return roleDao.findTopByRole(name);
    }

    public Roles getRoleById(Integer roleId) {
        Optional<Roles> role =  roleDao.findById(roleId);
        if(role.isPresent())
            return role.get();
        return null;
    }

}
