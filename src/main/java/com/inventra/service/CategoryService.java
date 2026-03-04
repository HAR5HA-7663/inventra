package com.inventra.service;

import com.inventra.dto.CategoryRequest;
import com.inventra.dto.CategoryResponse;
import com.inventra.exception.DuplicateResourceException;
import com.inventra.exception.ResourceNotFoundException;
import com.inventra.model.Category;
import com.inventra.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public CategoryResponse findById(Long id) {
        return toResponse(findCategoryOrThrow(id));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException(
                "Category with name '" + request.name() + "' already exists");
        }
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findCategoryOrThrow(id);
        if (!category.getName().equalsIgnoreCase(request.name()) &&
                categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException(
                "Category with name '" + request.name() + "' already exists");
        }
        category.setName(request.name());
        category.setDescription(request.description());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        findCategoryOrThrow(id);
        categoryRepository.deleteById(id);
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(
            c.getId(),
            c.getName(),
            c.getDescription(),
            c.getProducts().size(),
            c.getCreatedAt()
        );
    }
}
