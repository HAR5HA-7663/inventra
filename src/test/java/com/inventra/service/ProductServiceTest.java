package com.inventra.service;

import com.inventra.dto.ProductRequest;
import com.inventra.dto.ProductResponse;
import com.inventra.exception.DuplicateResourceException;
import com.inventra.exception.ResourceNotFoundException;
import com.inventra.model.Product;
import com.inventra.repository.CategoryRepository;
import com.inventra.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Wireless Keyboard");
        sampleProduct.setSku("ELEC-KB-001");
        sampleProduct.setPrice(new BigDecimal("49.99"));
        sampleProduct.setQuantity(50);

        sampleRequest = new ProductRequest(
            "Wireless Keyboard", "ELEC-KB-001", "Compact wireless keyboard",
            new BigDecimal("49.99"), 50, null
        );
    }

    @Test
    void findById_existingProduct_returnsResponse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Wireless Keyboard");
        assertThat(response.sku()).isEqualTo("ELEC-KB-001");
        assertThat(response.status()).isEqualTo("IN_STOCK");
    }

    @Test
    void findById_nonExistingProduct_throwsNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Product not found with id: 99");
    }

    @Test
    void create_newProduct_savesAndReturnsResponse() {
        when(productRepository.existsBySku("ELEC-KB-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.create(sampleRequest);

        assertThat(response.name()).isEqualTo("Wireless Keyboard");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void create_duplicateSku_throwsDuplicateException() {
        when(productRepository.existsBySku("ELEC-KB-001")).thenReturn(true);

        assertThatThrownBy(() -> productService.create(sampleRequest))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("ELEC-KB-001");
    }

    @Test
    void delete_existingProduct_deletesSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        productService.delete(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void stockStatus_zeroQuantity_returnsOutOfStock() {
        sampleProduct.setQuantity(0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.findById(1L);

        assertThat(response.status()).isEqualTo("OUT_OF_STOCK");
    }

    @Test
    void stockStatus_lowQuantity_returnsLowStock() {
        sampleProduct.setQuantity(5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.findById(1L);

        assertThat(response.status()).isEqualTo("LOW_STOCK");
    }
}
