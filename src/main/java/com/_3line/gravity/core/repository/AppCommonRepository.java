package com._3line.gravity.core.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AppCommonRepository<T, ID extends Serializable> extends  DataTablesRepository<T,ID> {

    Page<T> findUsingPattern(String pattern, Pageable details);

    public void delete(ID id) ;

    public void delete(Iterable<? extends T> entities);

    T findOne(ID id);

}