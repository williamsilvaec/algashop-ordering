package com.williamsilva.algashop.ordering.domain.model.service;

import com.williamsilva.algashop.ordering.domain.model.entity.Customer;
import com.williamsilva.algashop.ordering.domain.model.exception.CustomerEmailIsInUseException;
import com.williamsilva.algashop.ordering.domain.model.repository.Customers;
import com.williamsilva.algashop.ordering.domain.model.utility.DomainService;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Address;
import com.williamsilva.algashop.ordering.domain.model.valueobject.BirthDate;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Document;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Email;
import com.williamsilva.algashop.ordering.domain.model.valueobject.FullName;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Phone;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.CustomerId;
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
