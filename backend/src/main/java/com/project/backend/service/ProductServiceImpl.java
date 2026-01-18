package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.common.enums.ProductType;
import com.project.backend.entity.Company;
import com.project.backend.entity.Product;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public  class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<Product> getProducts(long companyId){
        // Return only active products for this company
        return productRepository.findByCompanyIdAndAvailableTrue(companyId);
    }

    @Override
    public Product getProduct(long productId, long companyId){
        // Ensure the product belongs to the given company
        return productRepository.findByIdAndCompanyId(productId, companyId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public Product saveProduct(Product product, long companyId){
        validateProductType(product.getProductType());
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        product.setCompany(company);
        product.setAvailable(true);
        return productRepository.save(product);

    }

    @Override
    public Product updateProduct(long productId, long companyId, Product product){
        Product existingProduct = getProduct(productId, companyId);

        // Partial update protection
        if (product.getProductName() != null) {
            existingProduct.setProductName(product.getProductName());
        }

        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }

        if (product.getUnitPrice() != null) {
            existingProduct.setUnitPrice(product.getUnitPrice());
        }

        if (product.getCost() != null) {
            existingProduct.setCost(product.getCost());
        }

        if (product.getProductType() != null) {
            validateProductType(product.getProductType());
            existingProduct.setProductType(product.getProductType());
        }
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(long productId, long companyId){
        Product product = getProduct(productId, companyId);
        // Soft delete for audit safety
        product.setAvailable(false);
        productRepository.save(product);
    }

    private void validateProductType(ProductType productType){
        if(productType == null){
            throw new RuntimeException("Product Type must be Selected");
        }
    }
}
