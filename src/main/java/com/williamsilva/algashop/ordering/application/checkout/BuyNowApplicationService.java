package com.williamsilva.algashop.ordering.application.checkout;

import com.williamsilva.algashop.ordering.domain.model.DomainException;
import com.williamsilva.algashop.ordering.domain.model.commons.Quantity;
import com.williamsilva.algashop.ordering.domain.model.commons.ZipCode;
import com.williamsilva.algashop.ordering.domain.model.customer.Customer;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.williamsilva.algashop.ordering.domain.model.customer.Customers;
import com.williamsilva.algashop.ordering.domain.model.order.Billing;
import com.williamsilva.algashop.ordering.domain.model.order.BuyNowService;
import com.williamsilva.algashop.ordering.domain.model.order.CreditCardId;
import com.williamsilva.algashop.ordering.domain.model.order.Order;
import com.williamsilva.algashop.ordering.domain.model.order.Orders;
import com.williamsilva.algashop.ordering.domain.model.order.PaymentMethod;
import com.williamsilva.algashop.ordering.domain.model.order.Shipping;
import com.williamsilva.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.williamsilva.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.williamsilva.algashop.ordering.domain.model.product.Product;
import com.williamsilva.algashop.ordering.domain.model.product.ProductCatalogService;
import com.williamsilva.algashop.ordering.domain.model.product.ProductId;
import com.williamsilva.algashop.ordering.domain.model.product.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BuyNowApplicationService {

    private final BuyNowService buyNowService;
    private final ProductCatalogService productCatalogService;

    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;

    private final Orders orders;
    private final Customers customers;

    private final ShippingInputDisassembler shippingInputDisassembler;
    private final BillingInputDisassembler billingInputDisassembler;

    @Transactional
    public String buyNow(BuyNowInput input) {
        Objects.requireNonNull(input);

        PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());
        CustomerId customerId = new CustomerId(input.getCustomerId());
        Quantity quantity = new Quantity(input.getQuantity());
        ProductId productId = new ProductId(input.getProductId());
        CreditCardId creditCardId = null;

        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            if (input.getCreditCardId() == null) {
                throw new DomainException("Credit card id is required");
            }

            creditCardId = new CreditCardId(input.getCreditCardId());
        }

        Customer customer = customers.ofId(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
        Product product = productCatalogService.ofId(productId).orElseThrow(() -> new ProductNotFoundException(productId));

        var calculationResult = calculateShippingCost(input.getShipping());

        Shipping shipping = shippingInputDisassembler.toDomainModel(input.getShipping(), calculationResult);
        Billing billing = billingInputDisassembler.toDomainModel(input.getBilling());

        Order order = buyNowService.buyNow(product, customer, billing, shipping, quantity, paymentMethod, creditCardId);

        orders.add(order);

        return order.id().toString();
    }

    private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shipping) {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode(shipping.getAddress().getZipCode());
        return shippingCostService.calculate(new ShippingCostService.CalculationRequest(origin, destination));
    }
}
