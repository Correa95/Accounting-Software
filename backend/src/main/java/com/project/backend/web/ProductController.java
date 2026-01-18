package com.project.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/product")
public class ProductController {

    @GetMapping()
    public ResponseEntity List<Product> getProducts(@PathVariable long companyId) {
        return new ResponseEntity<>(productService.getProducts(long productId, long companyId))
    }
    

    
}
