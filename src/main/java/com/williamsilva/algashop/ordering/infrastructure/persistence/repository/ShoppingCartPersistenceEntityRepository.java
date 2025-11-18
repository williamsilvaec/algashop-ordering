package com.williamsilva.algashop.ordering.infrastructure.persistence.repository;

import com.williamsilva.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartPersistenceEntityRepository extends JpaRepository<ShoppingCartPersistenceEntity, UUID> {

    @Query("select s from ShoppingCartPersistenceEntity s where s.customer.id = : customerId")
    Optional<ShoppingCartPersistenceEntity> findByCustomerId(@Param("customerId") UUID customerId);
}
