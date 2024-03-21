package com.example.demo.bootstrap;

import com.example.demo.dao.CustomerRepository;
import com.example.demo.dao.DivisionRepository;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Division;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final DivisionRepository divisionRepository;

    public BootStrapData(CustomerRepository customerRepository, DivisionRepository divisionRepository) {
        this.customerRepository = customerRepository;
        this.divisionRepository = divisionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(customerRepository.count() < 6){
            Division division = new Division();
            division.setId(16L);
            Customer john = new Customer("John", "Smith", "123 1st Ave", "12345", "1234567890");
            john.setDivision(division);
            customerRepository.save(john);
            Customer jane = new Customer("Jane", "Smith", "123 1st Ave", "12345", "1234567890");
            jane.setDivision(division);
            customerRepository.save(jane);
            Customer josh = new Customer("Josh", "Smith", "123 1st Ave", "12345", "1234567890");
            josh.setDivision(division);
            customerRepository.save(josh);
            Customer jessie = new Customer("Jessie", "Smith", "123 1st Ave", "12345", "1234567890");
            jessie.setDivision(division);
            customerRepository.save(jessie);
            Customer jordan = new Customer("Jordan", "Smith", "123 1st Ave", "12345", "1234567890");
            jordan.setDivision(division);
            customerRepository.save(jordan);
        }
    }
}
