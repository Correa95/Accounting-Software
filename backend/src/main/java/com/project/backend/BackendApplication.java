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

// companyRepository.save(company);

	// Company[] companies = new Company[]{
	// 	new Company("acme", "Acme Corporation LLC", "123 Main St","555-1234", "info@acme.com", "12-3456789", new BigDecimal("7.50"), "USD", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), BusinessType.SERVICE, true)
	// };
	// for (int i = 0; i < companies.length; i++) {
	// 	companyRepository.save(companies[i]);
	// }
	// Company acme = new Company();
	// acme.setName("Acme Corp");
	// acme.setLegalName("Acme Corporation LLC");
	// acme.setAddress("123 Main St");
	// acme.setPhone("555-1234");
	// acme.setEmail("info@acme.com");
	// acme.setTaxId("12-3456789");
	// acme.setTaxRate(new BigDecimal("7.50"));
	// acme.setCurrencyCode("USD");
	// acme.setFiscalYearStart(LocalDate.of(2025, 1, 1));
	// acme.setFiscalYearEnd(LocalDate.of(2025, 12, 31));
	// acme.setBusinessType(BusinessType.SERVICE);
	// acme.setActive(true);

	// companyRepository.save(acme);


	// Company[] companies = new Company[]{
	// 	new Company("Acme Corp","Acme Corporation LLC", "123 Main St", "555-1234", "info@acme.com", "12-3456789",new BigDecimal("7.50"), LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), LocalDate.of(2025, 12, 31) , BusinessType.SERVICE );
	// };
	// for (int i = 0; i < companies.length; i++) {
	// 	companyRepository.save(companies[i]);
	// }

	// Customer[] customers = new Customer[]{};
	// for (int i = 0; i < customers.length; i++) {
	// 	customerRepository.save(customers[i]);
	// }
