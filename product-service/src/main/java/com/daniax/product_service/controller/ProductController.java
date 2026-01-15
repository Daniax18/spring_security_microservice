package com.daniax.product_service.controller;

import com.daniax.product_service.dto.ProductDto;
import com.daniax.product_service.entity.Product;
import com.daniax.product_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll(){
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDto productDto){
        try {
            Product toSave = new Product(productDto.getName(), productDto.getPrice());
            return new ResponseEntity<>(productService.create(toSave), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
