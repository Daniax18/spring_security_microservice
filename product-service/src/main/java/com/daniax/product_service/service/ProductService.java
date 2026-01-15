package com.daniax.product_service.service;

import com.daniax.product_service.entity.Product;
import com.daniax.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Product create(Product product){
        return productRepository.save(product);
    }
}
