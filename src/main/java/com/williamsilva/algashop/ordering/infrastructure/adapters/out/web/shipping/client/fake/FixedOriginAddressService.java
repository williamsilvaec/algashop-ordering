package com.williamsilva.algashop.ordering.infrastructure.adapters.out.web.shipping.client.fake;

import com.williamsilva.algashop.ordering.core.domain.model.commons.Address;
import com.williamsilva.algashop.ordering.core.domain.model.commons.ZipCode;
import com.williamsilva.algashop.ordering.core.domain.model.order.shipping.OriginAddressService;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressService implements OriginAddressService {

    @Override
    public Address originAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1134")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .build();
    }
}
