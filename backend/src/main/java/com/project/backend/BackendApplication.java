package com.project.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.backend.entity.Company;
import com.project.backend.entity.Customer;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.CustomerRepository;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	CustomerRepository customerRepository;
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{

		Company[] companies = new Company[]{};
		for (int i = 0; i < companies.length; i++) {
			companyRepository.save(companies[i]);
		}

		Customer[] customers = new Customer[]{};
		for (int i = 0; i < customers.length; i++) {
			customerRepository.save(customers[i]);
		}
	}

}
