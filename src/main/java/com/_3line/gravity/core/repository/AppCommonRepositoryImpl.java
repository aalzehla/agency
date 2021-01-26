package com._3line.gravity.core.repository;

import com._3line.gravity.core.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Transactional
public class AppCommonRepositoryImpl<T extends AbstractEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AppCommonRepository<T, ID> {
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;



    AppCommonRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                             EntityManager entityManager) {

        super(entityInformation, entityManager);
        this.entityInformation = entityInformation ;
        this.em = entityManager ;

    }

    @Override
    @Transactional
    public void delete(ID id) {
        T t = getOne(id);
        t.setDelFlag("Y");
        t.setDeletedOn(new Date());

        super.save(t);
    }

    @Override
    @Transactional
    public void delete(T entity) {

        entity.setDelFlag("Y");
        entity.setDeletedOn(new Date());
        super.save(entity);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities can  not be null!");
        Iterator<? extends T> var2 = entities.iterator();

        while (var2.hasNext()) {
            T entity = var2.next();
            entity.setDelFlag("Y");
            entity.setDeletedOn(new Date());
            super.save(entity);
        }

    }

    @Override
    public T findOne(ID id) {
        return getOne(id);
    }


    @Override
    @Transactional
    public void deleteAll() {
        Iterator<T> var1 = this.findAll().iterator();

        while (var1.hasNext()) {
            T entity = var1.next();
            entity.setDelFlag("Y");
            entity.setDeletedOn(new Date());
            super.save(entity);
        }
    }

    @Override
    @Transactional
    public void deleteAllInBatch() {
//        super.deleteAllInBatch();
    }

    @Override
    public Page<T> findUsingPattern(String pattern, Pageable details) {
        String lpattern = pattern.toLowerCase();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityInformation.getJavaType());
        Root<T> c = q.from(entityInformation.getJavaType());
        Predicate[] predicates = null;
        try {
            predicates = new Predicate[getFields().size()];
            int cnt = 0;
            for (String field : getFields()) {
                Predicate predicate = cb.like(cb.lower(c.get(field)), "%" + lpattern + "%");
                predicates[cnt] = predicate;
                cnt++;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<T> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class);
        CriteriaQuery<Long> countQuery = null;
        if (predicates.length > 0) {
            Predicate or = cb.or(predicates);
            baseQuery = q.select(c).where(or);
            countQuery = qc.select(cb.count(qc.from(entityInformation.getJavaType()))).where(or);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(entityInformation.getJavaType())));
        }

        TypedQuery<T> query = em.createQuery(baseQuery);
        Long count = em.createQuery(countQuery).getSingleResult();
        query.setFirstResult(Long.valueOf(details.getOffset()).intValue());
        query.setMaxResults(details.getPageSize());
        List<T> list = query.getResultList();
        return new PageImpl<T>(list, details, count);
    }


    private List<String> getFields() throws InstantiationException, IllegalAccessException {
        Class<T> type = entityInformation.getJavaType();
        AbstractEntity en = (AbstractEntity) type.newInstance();
        return en.getDefaultSearchFields();

    }


    @Override
    public DataTablesOutput<T> findAll(DataTablesInput input) {
        return findAll(input, null, null, null);
    }

    @Override
    public DataTablesOutput<T> findAll(DataTablesInput input,
                                       Specification<T> additionalSpecification) {
        return findAll(input, additionalSpecification, null, null);
    }

    @Override
    public DataTablesOutput<T> findAll(DataTablesInput input,
                                       Specification<T> additionalSpecification, Specification<T> preFilteringSpecification) {
        return findAll(input, additionalSpecification, preFilteringSpecification, null);
    }

    @Override
    public <R> DataTablesOutput<R> findAll(DataTablesInput input, Function<T, R> converter) {
        return findAll(input, null, null, converter);
    }

    @Override
    public <R> DataTablesOutput<R> findAll(DataTablesInput input,
                                           Specification<T> additionalSpecification, Specification<T> preFilteringSpecification,
                                           Function<T, R> converter) {
        DataTablesOutput<R> output = new DataTablesOutput<>();
        output.setDraw(input.getDraw());
        if (input.getLength() == 0) {
            return output;
        }

        try {
            long recordsTotal =
                    preFilteringSpecification == null ? count() : count(preFilteringSpecification);
            if (recordsTotal == 0) {
                return output;
            }
            output.setRecordsTotal(recordsTotal);

            SpecificationBuilder<T> specificationBuilder = new SpecificationBuilder<>(input);
            Page<T> data = findAll(
                    Specification.where(specificationBuilder.build())
                            .and(additionalSpecification)
                            .and(preFilteringSpecification),
                    specificationBuilder.createPageable());

            @SuppressWarnings("unchecked")
            List<R> content =
                    converter == null ? (List<R>) data.getContent() : data.map(converter).getContent();
            output.setData(content);
            output.setRecordsFiltered(data.getTotalElements());

        } catch (Exception e) {
            e.printStackTrace();
            output.setError(e.toString());
        }

        return output;
    }


}
