package com._3line.gravity.core.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;


public class CustomJpaRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
        extends JpaRepositoryFactoryBean<T, S, ID> {

    public CustomJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new CustomJpaRepositoryFactory<T, ID>(entityManager);
    }

    private static class CustomJpaRepositoryFactory<T, ID extends Serializable> extends JpaRepositoryFactory {

        private final EntityManager entityManager;

        public CustomJpaRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }
//
//        @Override
//        @SuppressWarnings("unchecked")
//        protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
//            //final JpaEntityInformation<?, Serializable> entityInformation = this.getEntityInformation();
//
//            return new AppCommonRepositoryImpl(information.getDomainType(), entityManager);
//        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

            Class<?> repositoryInterface = metadata.getRepositoryInterface();
            if (DataTablesRepository.class.isAssignableFrom(repositoryInterface)) {
                return AppCommonRepositoryImpl.class;
            } else {
                return super.getRepositoryBaseClass(metadata);
            }
//            return AppCommonRepositoryImpl.class;
        }
    }
}
