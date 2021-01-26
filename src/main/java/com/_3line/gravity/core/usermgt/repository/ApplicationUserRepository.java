package com._3line.gravity.core.usermgt.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.core.rolemgt.models.Role;
import com._3line.gravity.core.rolemgt.models.RoleType;
import com._3line.gravity.core.usermgt.model.SystemUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationUserRepository extends AppCommonRepository<SystemUser, Long> {

    SystemUser findByUserName(String userName);

//    boolean existsByUserName(String username);

    SystemUser findByEmail(String email);

    Page<SystemUser> findByRole(Role role , Pageable pageable);

    Page<SystemUser> findByRole_RoleType(RoleType roleType, Pageable pageable);

    Integer countByRole(Role role);
}
