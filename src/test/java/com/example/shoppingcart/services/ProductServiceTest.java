package com.example.shoppingcart.services;

import com.example.shoppingcart.entities.Product;
import com.example.shoppingcart.repositories.IProductRepository;
import com.example.shoppingcart.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("FreshFit");
        product.setAvailableStock(20);
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertEquals("FreshFit", createdProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L).get();

        assertEquals("FreshFit", foundProduct.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateProductStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.updateProductStock(1L, 5);

        assertEquals(15, product.getAvailableStock());
        verify(productRepository, times(1)).save(product);
    }
}
