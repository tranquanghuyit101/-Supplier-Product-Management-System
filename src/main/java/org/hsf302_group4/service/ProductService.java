package org.hsf302_group4.service;

import org.hsf302_group4.entity.Product;
import org.hsf302_group4.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo){ this.repo = repo; }

    public Page<Product> findAllPaged(Pageable pageable){ return repo.findAll(pageable); }
    public List<Product> top5ByPrice(){ return repo.findTop5ByOrderByPriceDesc(); }
    public Product save(Product p){ return repo.save(p); }
    public Optional<Product> findById(Integer id){ return repo.findById(id); }
    public void delete(Integer id){ repo.deleteById(id); }
}
