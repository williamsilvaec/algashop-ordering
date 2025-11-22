package com.williamsilva.algashop.ordering.domain.model.customer;

import com.williamsilva.algashop.ordering.domain.model.DomainService;
import com.williamsilva.algashop.ordering.domain.model.commons.Address;
import com.williamsilva.algashop.ordering.domain.model.commons.Document;
import com.williamsilva.algashop.ordering.domain.model.commons.Email;
import com.williamsilva.algashop.ordering.domain.model.commons.FullName;
import com.williamsilva.algashop.ordering.domain.model.commons.Phone;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class CustomerRegistrationService {

    private final Customers customers;

    public Customer register(
            FullName fullName, BirthDate birthDate, Email email,
            Phone phone, Document document, Boolean promotionNotificationsAllowed,
            Address address
    ) {
        Customer customer = Customer.brandNew()
                .fullName(fullName)
                .birthDate(birthDate)
                .email(email)
                .phone(phone)
                .document(document)
                .promotionNotificationsAllowed(promotionNotificationsAllowed)
                .address(address)
                .build();

        verifyEmailUniqueness(customer.email(), customer.id());

        return customer;
    }

    public void changeEmail(Customer customer, Email email) {
        verifyEmailUniqueness(email, customer.id());
        customer.changeEmail(email);
    }

    private void verifyEmailUniqueness(Email email, CustomerId id) {
        if (!customers.isEmailUnique(email, id)) {
            throw new CustomerEmailIsInUseException();
        }
    }
}
