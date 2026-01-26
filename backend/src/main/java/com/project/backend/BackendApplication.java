package com.project.backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication  {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	
}


// 		Company companies = new Company(
//     "acme",
//     "Acme Corporation LLC",
//     "123 Main St",
//     "555-1234",
//     "info@acme.com",
//     "12-3456789",
//     new BigDecimal("7.50"),
//     "USD",
//     LocalDate.of(2025, 1, 1),
//     LocalDate.of(2025, 12, 31),
//     BusinessType.SERVICE
// );


