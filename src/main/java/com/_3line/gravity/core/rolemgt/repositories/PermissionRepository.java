package com._3line.gravity.core.rolemgt.repositories;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.core.rolemgt.models.Permission;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends AppCommonRepository<Permission, Long> {
    Permission findFirstByNameIgnoreCase(String s);

    Iterable<Permission> findByIdNotIn(Long[] permissions);

    Permission findFirstByName(String operation);
    boolean existsByName(String operation);
}
