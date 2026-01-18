package com.project.backend.service;

import java.util.List;



import com.project.backend.entity.Product;


public interface ProductService {

    List<Product> getProducts(long companyId);
    Product getProduct(long productId, long companyId);
    Product saveProduct(Product product, long companyId);
    Product updateProduct(long productId, long companyId, Product product);
    void deleteProduct(long productId, long companyId);
    
}
