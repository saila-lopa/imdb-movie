package com.shaan.api.myapp.user;

import com.shaan.api.myapp.domain.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Roles, Integer> {

    @Override
    Roles save(Roles roles);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Roles role);

    public Roles findTopByRole(String role);
}
