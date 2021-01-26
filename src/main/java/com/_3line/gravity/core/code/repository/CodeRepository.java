package com._3line.gravity.core.code.repository;

import com._3line.gravity.core.code.model.Code;
import com._3line.gravity.core.repository.AppCommonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author FortunatusE
 * @date 11/6/2018
 */

@Repository
public interface CodeRepository extends AppCommonRepository<Code, Long> {

    Iterable<Code> findByType(String type);
    Page<Code> findByTypeIgnoreCase(String type, Pageable pageable);
    Page<Code> findByType(String type, Pageable pageable);
    Code findByTypeAndCode(String type, String code);
    @Query("select distinct c.type from Code c")
    Iterable<String> findAllTypes();
    @Query("select distinct c.type from Code c")
    Page<String> findAllTypes(Pageable pageable);
    @Query("select distinct c.type from Code c where c.type LIKE CONCAT('%',:type,'%')")
    Page<String> findByCodeType(@Param("type") String codeType, Pageable pageable);
    @Query("select c from Code c where c.type =:type and c.code LIKE CONCAT('%',:value,'%') or c.description LIKE CONCAT('%',:value,'%')")
    Page<Code> findByTypeAndValue(@Param("type") String codeType, @Param("value") String value, Pageable pageable);
}
