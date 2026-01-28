package com.williamsilva.algashop.ordering.core.application.customer.query;

import com.williamsilva.algashop.ordering.core.application.utility.SortablePageFilter;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class CustomerFilter extends SortablePageFilter<CustomerFilter.SortType> {

    private String email;
    private String firstName;

    public CustomerFilter() {
    }

    public CustomerFilter(int size, int page) {
        super(size, page);
    }

    public CustomerFilter(int size, int page, String email, String firstName) {
        super(size, page);
        this.email = email;
        this.firstName = firstName;
    }

    @Override
    public SortType getSortByPropertyOrDefault() {
        return getSortByProperty() == null ? SortType.REGISTERED_AT : getSortByProperty();
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return getSortDirection() == null ? Sort.Direction.ASC : getSortDirection();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerFilter that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, firstName);
    }

    @Override
    public String toString() {
        return "CustomerFilter{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                "} " + super.toString();
    }

    public enum SortType {
        REGISTERED_AT("registeredAt"),
        FIRST_NAME("firstName");

        private final String propertyName;

        SortType(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }
    }
}