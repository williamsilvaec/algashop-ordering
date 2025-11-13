package com.williamsilva.algashop.ordering.infrastructure.persistence.disassembler;

import com.williamsilva.algashop.ordering.domain.model.entity.Customer;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Address;
import com.williamsilva.algashop.ordering.domain.model.valueobject.BirthDate;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Document;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Email;
import com.williamsilva.algashop.ordering.domain.model.valueobject.FullName;
import com.williamsilva.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Phone;
import com.williamsilva.algashop.ordering.domain.model.valueobject.ZipCode;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.williamsilva.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.williamsilva.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
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
