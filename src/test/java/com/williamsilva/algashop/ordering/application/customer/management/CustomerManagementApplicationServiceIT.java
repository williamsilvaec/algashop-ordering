package com.williamsilva.algashop.ordering.application.customer.management;

import com.williamsilva.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerOutput;
import com.williamsilva.algashop.ordering.application.customer.query.CustomerQueryService;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerArchivedException;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.williamsilva.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("ordering_test");

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("api.version", "1.44");
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    public static void configurePropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
        registry.add("spring.flyway.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private CustomerNotificationApplicationService notificationApplicationService;

    @Autowired
    private CustomerQueryService queryService;

    @Test
    public void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = queryService.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId,
                        "John",
                        "Doe",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7,5)
                );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

        Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));
        Mockito.verify(notificationApplicationService).notifyNewRegistration(
                Mockito.any(CustomerNotificationApplicationService.NotifyNewRegistrationInput.class)
        );
    }

    @Test
    public void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId, updateInput);

        CustomerOutput customerOutput = queryService.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId,
                        "Matt",
                        "Damon",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7,5)
                );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    public void shouldArchiveCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        CustomerOutput archivedCustomer = queryService.findById(customerId);

        Assertions.assertThat(archivedCustomer)
                .isNotNull()
                .extracting(
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getPhone,
                        CustomerOutput::getDocument,
                        CustomerOutput::getBirthDate,
                        CustomerOutput::getPromotionNotificationsAllowed
                ).containsExactly(
                        "Anonymous",
                        "Anonymous",
                        "000-000-0000",
                        "000-00-0000",
                        null,
                        false
                );

        Assertions.assertThat(archivedCustomer.getEmail()).endsWith("@anonymous.com");
        Assertions.assertThat(archivedCustomer.getArchived()).isTrue();
        Assertions.assertThat(archivedCustomer.getArchivedAt()).isNotNull();

        Assertions.assertThat(archivedCustomer.getAddress()).isNotNull();
        Assertions.assertThat(archivedCustomer.getAddress().getNumber()).isNotNull().isEqualTo("Anonymized");
        Assertions.assertThat(archivedCustomer.getAddress().getComplement()).isNull();
    }

    @Test
    public void shouldThrowCustomerNotFoundExceptionWhenArchivingNonExistingCustomer() {
        UUID nonExistingId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(nonExistingId));
    }

    @Test
    public void shouldThrowCustomerArchivedExceptionWhenArchivingAlreadyArchivedCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(customerId));
    }

    @Test
    public void shouldUpdateEmailCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        String newEmail = "testenovoemail@mail.com";

        customerManagementApplicationService.changeEmail(customerId, newEmail);

        CustomerOutput customerOutput = queryService.findById(customerId);

        Assertions.assertThat(customerOutput.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenChangeEmailForCustomerNotExisting() {
        UUID customerId = UUID.randomUUID();
        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "teste@mail.com"));
    }

    @Test
    void shouldThrowCustomerArchivedExceptionWhenChangeEmailForCustomerArchived() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "teste@email.com"));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenChangeEmailInvalid() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "email-invalido"));
    }

    @Test
    void shouldThrowCustomerEmailIsInUseExceptionWhenChangeEmailExisting() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        CustomerInput input1 = CustomerInputTestDataBuilder.aCustomer().email("teste@email.com").build();
        UUID customer2Id = customerManagementApplicationService.create(input1);

        Assertions.assertThatExceptionOfType(CustomerEmailIsInUseException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "teste@email.com"));
    }

}