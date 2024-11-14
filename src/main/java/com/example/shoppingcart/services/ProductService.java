package com.example.shoppingcart.services;

import com.example.shoppingcart.entities.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getAvailableProducts();
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);

    void updateProductStock(Long id, int quantity);

    void deleteProduct(Long id);
}
