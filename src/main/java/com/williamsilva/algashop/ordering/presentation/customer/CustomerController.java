package com.williamsilva.algashop.ordering.presentation.customer;

import com.williamsilva.algashop.ordering.application.customer.management.CustomerInput;
import com.williamsilva.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.williamsilva.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerFilter;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerOutput;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerQueryService;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import com.williamsilva.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.williamsilva.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.williamsilva.algashop.ordering.presentation.PageModel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManagementApplicationService customerManagementApplicationService;
    private final CustomerQueryService customerQueryService;
    private final ShoppingCartQueryService shoppingCartQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOutput create(@RequestBody @Valid CustomerInput input, HttpServletResponse response) {
        UUID customerId = customerManagementApplicationService.create(input);

        UriComponentsBuilder builder = MvcUriComponentsBuilder.fromMethodCall(
                MvcUriComponentsBuilder.on(CustomerController.class).findById(customerId)
        );
        response.addHeader("Location", builder.toUriString());

        return customerQueryService.findById(customerId);
    }

    @GetMapping
    public PageModel<CustomerSummaryOutput> findAll(CustomerFilter filter) {
        Page<CustomerSummaryOutput> page = customerQueryService.filter(filter);
        return PageModel.of(page);
    }

    @GetMapping("/{customerId}")
    public CustomerOutput findById(@PathVariable UUID customerId) {
        return customerQueryService.findById(customerId);
    }

    @GetMapping("/{customerId}/shopping-cart")
    public ShoppingCartOutput findShoppingCartByCustomerId(@PathVariable UUID customerId) {
        return shoppingCartQueryService.findByCustomerId(customerId);
    }

    @PutMapping("/{customerId}")
    public CustomerOutput update(@PathVariable UUID customerId,
                                 @RequestBody @Valid CustomerUpdateInput input) {
        customerManagementApplicationService.update(customerId, input);
        return customerQueryService.findById(customerId);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID customerId) {
        customerManagementApplicationService.archive(customerId);
    }
}