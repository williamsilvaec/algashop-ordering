package com.williamsilva.algashop.ordering.presentation;

import com.williamsilva.algashop.ordering.application.customer.management.CustomerInput;
import com.williamsilva.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerFilter;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerOutput;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerQueryService;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManagementApplicationService customerManagementApplicationService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOutput create(@RequestBody @Valid CustomerInput input) {
        UUID customerId = customerManagementApplicationService.create(input);
        return customerQueryService.findById(customerId);
    }

    @GetMapping
    public PageModel<CustomerSummaryOutput> findAll(CustomerFilter filter) {
        Page<CustomerSummaryOutput> page = customerQueryService.filter(filter);
        return PageModel.of(page);
    }
}