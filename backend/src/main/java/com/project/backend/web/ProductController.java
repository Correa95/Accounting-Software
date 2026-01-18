package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.Product;
import com.project.backend.service.ProductService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/product")
public class ProductController {

    private final ProductService productService;



    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@PathVariable long companyId) {
        return new ResponseEntity<>(productService.getProducts(long companyId), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable long companyId, @PathVariable long productId) {
        return new ResponseEntity<>(productService.getProduct(productId, companyId), HttpStatus.OK);
    }

    @PostMapping("/product{id}")
    public ResponseEntity Product createProduct(@RequestBody Product product, @PathVariable long companyId) {
        
        return new ResponseEntity<>(productService.saveProduct(product, companyId), HttpStatus.CREATED);
    }
    
    
    

    
}
