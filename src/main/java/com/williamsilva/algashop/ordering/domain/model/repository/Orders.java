package com.williamsilva.algashop.ordering.domain.model.repository;

import com.williamsilva.algashop.ordering.domain.model.entity.Order;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.OrderId;

import java.time.Year;
import java.util.List;

public interface Orders extends Repository<Order, OrderId> {

    List<Order> placedByCustomerInYear(CustomerId customerId, Year year);
}
