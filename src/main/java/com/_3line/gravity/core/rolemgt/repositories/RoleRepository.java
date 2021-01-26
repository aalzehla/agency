package com._3line.gravity.core.rolemgt.repositories;


import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.core.rolemgt.models.Role;
import com._3line.gravity.core.rolemgt.models.RoleType;

import java.util.List;

public interface RoleRepository extends AppCommonRepository<Role, Long> {

    Role findFirstByNameIgnoreCase(String s);
    Role findFirstByRoleType(RoleType roleType);
    Role findByNameAndRoleType(String name, RoleType type);
    List<Role> findByRoleType(RoleType type);


}
