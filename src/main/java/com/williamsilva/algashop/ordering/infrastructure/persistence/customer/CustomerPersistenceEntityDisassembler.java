package com.williamsilva.algashop.ordering.infrastructure.persistence.customer;

import com.williamsilva.algashop.ordering.core.domain.model.customer.Customer;
import com.williamsilva.algashop.ordering.core.domain.model.commons.Address;
import com.williamsilva.algashop.ordering.core.domain.model.customer.BirthDate;
import com.williamsilva.algashop.ordering.core.domain.model.commons.Document;
import com.williamsilva.algashop.ordering.core.domain.model.commons.Email;
import com.williamsilva.algashop.ordering.core.domain.model.commons.FullName;
import com.williamsilva.algashop.ordering.core.domain.model.customer.LoyaltyPoints;
import com.williamsilva.algashop.ordering.core.domain.model.commons.Phone;
import com.williamsilva.algashop.ordering.core.domain.model.commons.ZipCode;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityDisassembler {

    public Customer toDomainEntity(CustomerPersistenceEntity entity) {
        return Customer.existing()
                .id(new CustomerId(entity.getId()))
                .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
                .birthDate(entity.getBirthDate() != null ? new BirthDate(entity.getBirthDate()) : null)
                .email(new Email(entity.getEmail()))
                .phone(new Phone(entity.getPhone()))
                .document(new Document(entity.getDocument()))
                .promotionNotificationsAllowed(entity.getPromotionNotificationsAllowed())
                .archived(entity.getArchived())
                .registeredAt(entity.getRegisteredAt())
                .archivedAt(entity.getArchivedAt())
                .address(toAddressValueObject(entity.getAddress()))
                .loyaltyPoints(new LoyaltyPoints(entity.getLoyaltyPoints()))
                .version(entity.getVersion())
                .build();
    }

    private Address toAddressValueObject(AddressEmbeddable address) {
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }
}
