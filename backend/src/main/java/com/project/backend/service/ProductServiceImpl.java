package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.common.enums.ProductType;
import com.project.backend.entity.Company;
import com.project.backend.entity.Product;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public  class ProductServiceImpl implements ProductService{

    public final ProductRepository productRepository;
    public final CompanyRepository companyRepository;

    @Override
    public List<Product> getProducts(long companyId){
        // This find the customer associated with the company
        return productRepository.findByCompanyId(companyId);
    }

    @Override
    public Product getProduct(long id, long companyId){
        // find the customer that is only associated with this company
        return productRepository.findByIdAndCompanyId(id, companyId).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product saveProduct(Product product, long companyId){
        validateProductType(product.getProductType());
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        product.setCompany(company);
        product.setAvailable(true);
        return productRepository.save(product);

    }

    @Override
    public Product updateProduct(long productId, long companyId, Product product){
        Product existingProduct = getProduct(productId, companyId);

        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setUnitPrice(product.getUnitPrice());
        existingProduct.setCost(product.getCost());

        if(product.getProductType() != null){
            existingProduct.setProductType(product.getProductType());
        }
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(long productId, long companyId){
        Product product = getProduct(productId, companyId);
        product.setAvailable(false);
        productRepository.save(product);
    }

    private void validateProductType(ProductType productType){
        if(productType == null){
            throw new RuntimeException("Product Type must be Selected");
        }
    }
}
