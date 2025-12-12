package com.spencer.payments.repository;

import com.spencer.payments.customer.entity.Customer;
import com.spencer.payments.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();

        Customer testCustomer = new Customer();

        testCustomer.setName("Alice Wonderland");
        testCustomer.setEmail("alice@test.com");

        customerRepository.save(testCustomer);
    }

    @Test
    void testCreateCustomer() {
        Customer testCustomer = new Customer();

        testCustomer.setName("John Deere");
        testCustomer.setEmail("John@test.com");

        Customer saved = customerRepository.save(testCustomer);
        assertNotNull(saved.getId());
    }

    @Test
    void testReadCustomer() {
        List<Customer> customers = customerRepository.findAll();
        assertFalse(customers.isEmpty());
        assertEquals("Alice Wonderland", customers.get(0).getName());
    }
}