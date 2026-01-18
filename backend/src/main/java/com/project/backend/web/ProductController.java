package com.project.backend.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.Product;
import com.project.backend.service.ProductService;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/product")
public class ProductController {

    private final ProductService productService;



    @GetMapping
    public ResponseEntity <List<Product>> getProducts(@PathVariable long companyId) {
        return new ResponseEntity<>(productService.getProducts(long companyId));
    }

    @GetMapping("/{id}")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    

    
}
