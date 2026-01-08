package com.project.backend.company;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;





@RestController
@RequestMapping("/companies")
public class CompanyController {
    
    private final CompanyService companyService;
    
    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getCompanies(){
        List<Company> companies = companyService.getCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }
        
    @GetMapping("/company/{id}")
    public ResponseEntity<Company> getCompany             (@PathVariable String id) {
        Company company = companyService.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping("/company")
    public ResponseEntity<HttpStatus> createCompany(@RequestBody Company company) {
        companyService.saveCompany(company);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("company/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable String id, @RequestBody Company company) {
        companyService.updateCompany(id,company);  
        return new ResponseEntity<>(companyService.getCompanyById(id), HttpStatus.OK);
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<HttpStatus> deleteCompany(@PathVariable String id){
        companyService.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    
    }
    


    
    

