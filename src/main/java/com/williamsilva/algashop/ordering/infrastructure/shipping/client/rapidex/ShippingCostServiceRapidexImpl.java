package com.williamsilva.algashop.ordering.infrastructure.shipping.client.rapidex;

import com.williamsilva.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import com.williamsilva.algashop.ordering.core.domain.model.commons.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "algashop.integrations.shipping.provider", havingValue = "RAPIDEX")
public class ShippingCostServiceRapidexImpl implements ShippingCostService {

    private final RapiDexAPIClient rapiDexAPIClient;

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        var deliveryCostRequest = new DeliveryCostRequest(request.origin().value(), request.destination().value());

        DeliveryCostResponse response = rapiDexAPIClient.calculate(deliveryCostRequest);

        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(response.getEstimatedDaysToDeliver());

        return CalculationResult.builder()
                .cost(new Money(response.getDeliveryCost()))
                .expectedDate(expectedDeliveryDate)
                .build();
    }
}
