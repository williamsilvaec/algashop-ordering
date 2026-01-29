package com.williamsilva.algashop.ordering.core.application.customer;

import com.williamsilva.algashop.ordering.core.domain.model.customer.Customer;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerLoyaltyPointsService;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.williamsilva.algashop.ordering.core.domain.model.customer.Customers;
import com.williamsilva.algashop.ordering.core.domain.model.order.Order;
import com.williamsilva.algashop.ordering.core.domain.model.order.OrderId;
import com.williamsilva.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.williamsilva.algashop.ordering.core.domain.model.order.Orders;
import com.williamsilva.algashop.ordering.core.ports.in.customer.ForAddingLoyaltyPoints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerLoyaltyPointsApplicationService implements ForAddingLoyaltyPoints {

	private final CustomerLoyaltyPointsService customerLoyaltyPointsService;
	private final Orders orders;
	private final Customers customers;

	@Transactional
	public void addLoyaltyPoints(UUID rawCustomerId, String rawOrderId) {
		CustomerId customerId = new CustomerId(rawCustomerId);
		OrderId orderId = new OrderId(rawOrderId);

		Order order = orders.ofId(orderId)
				.orElseThrow(() -> new OrderNotFoundException());
		Customer customer = customers.ofId(customerId)
				.orElseThrow(() -> new CustomerNotFoundException());
		
		customerLoyaltyPointsService.addPoints(customer, order);
		
		customers.add(customer);
	}

}