package com.project.backend.web;

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

import com.project.backend.entity.Company;
import com.project.backend.service.CompanyService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/companies")
public class CompanyController {
    
    private final CompanyService companyService;
    

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getCompanies(){
        return new ResponseEntity<>(companyService.getCompanies(), HttpStatus.OK);
    }
     
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable long id) {
        return new ResponseEntity<>(companyService.getCompany(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return new ResponseEntity<>(companyService.saveCompany(company),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable long id, @RequestBody Company company) {
        Company updateCompany = companyService.updateCompany(id,company);  
        return new ResponseEntity<>(updateCompany, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id){
        companyService.deleteCompany(id);
        return  ResponseEntity.noContent().build();
    }
    
    
    }
    


    
    

