package com.williamsilva.algashop.ordering.infrastructure.persistence.repository;

import com.williamsilva.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {

    @Query("""
            select o from OrderPersistenceEntity o
                    where o.customer.id = :customerId
                    and Year(o.placedAt) = :year
            """
    )
    List<OrderPersistenceEntity> placedByCustomerInYear(
            @Param("customerId") UUID customerId,
            @Param("year") Integer year
    );

    @Query("""
        select count(o)
            from OrderPersistenceEntity o
            where o.customer.id = :customerId
            and Year(o.placedAt) = :year
            and o.paidAt is not null
            and o.canceledAt is null
        """)
    long salesQuantityByCustomerInYear(@Param("customerId") UUID customerId, @Param("year") Integer year);

    @Query("""
        select coalesce(sum(o.totalAmount), 0)
            from OrderPersistenceEntity o
            where o.customer.id = :customerId
            and o.canceledAt is null
            and o.paidAt is not null
    """)
    BigDecimal totalSoldByCustomer(@Param("customerId") UUID customerId);

}
