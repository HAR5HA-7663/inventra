package com.inventra.service;

import com.inventra.dto.PagedResponse;
import com.inventra.dto.ProductRequest;
import com.inventra.dto.ProductResponse;
import com.inventra.exception.DuplicateResourceException;
import com.inventra.exception.ResourceNotFoundException;
import com.inventra.model.Category;
import com.inventra.model.Product;
import com.inventra.repository.CategoryRepository;
import com.inventra.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public PagedResponse<ProductResponse> search(String name, Long categoryId, Pageable pageable) {
        Page<Product> page = productRepository.search(name, categoryId, pageable);
        return toPagedResponse(page);
    }

    public ProductResponse findById(Long id) {
        return toResponse(findProductOrThrow(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new DuplicateResourceException(
                "Product with SKU '" + request.sku() + "' already exists");
        }
        return toResponse(productRepository.save(buildProduct(new Product(), request)));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProductOrThrow(id);
        if (!product.getSku().equals(request.sku()) && productRepository.existsBySku(request.sku())) {
            throw new DuplicateResourceException(
                "Product with SKU '" + request.sku() + "' already exists");
        }
        return toResponse(productRepository.save(buildProduct(product, request)));
    }

    @Transactional
    public void delete(Long id) {
        findProductOrThrow(id);
        productRepository.deleteById(id);
    }

    private Product buildProduct(Product product, ProductRequest request) {
        product.setName(request.name());
        product.setSku(request.sku());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        return product;
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    private PagedResponse<ProductResponse> toPagedResponse(Page<Product> page) {
        return new PagedResponse<>(
            page.getContent().stream().map(this::toResponse).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
    }

    private ProductResponse toResponse(Product p) {
        String status = p.getQuantity() == 0 ? "OUT_OF_STOCK"
                      : p.getQuantity() <= 10 ? "LOW_STOCK"
                      : "IN_STOCK";
        return new ProductResponse(
            p.getId(),
            p.getName(),
            p.getSku(),
            p.getDescription(),
            p.getPrice(),
            p.getQuantity(),
            status,
            p.getCategory() != null ? p.getCategory().getId() : null,
            p.getCategory() != null ? p.getCategory().getName() : null,
            p.getCreatedAt(),
            p.getUpdatedAt()
        );
    }
}
