package hsf302_group4.service.impl;

import hsf302_group4.entity.Product;
import hsf302_group4.repository.ProductRepository;
import hsf302_group4.service.ProductService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Page<Product> pageAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public List<Product> top5ByPriceDesc() {
        return repo.findTop5ByOrderByPriceDesc();
    }

    @Override
    public Product save(Product p) {
        if (p.getProductId() == null || p.getProductId().isBlank()) {
            throw new IllegalArgumentException("Product ID must not be blank");
        }

        if (p.getId() == null) {
            if (repo.existsByProductId(p.getProductId())) {
                throw new DuplicateKeyException("Product ID already exists: " + p.getProductId());
            }
            LocalDate now = LocalDate.now();
            if (p.getCreatedAt() == null) p.setCreatedAt(now);
            p.setUpdatedAt(now);
            return repo.save(p);
        } else {
            if (repo.existsByProductIdAndIdNot(p.getProductId(), p.getId())) {
                throw new DuplicateKeyException("Product ID already exists: " + p.getProductId());
            }
            if (p.getCreatedAt() == null) {
                Product db = repo.findById(p.getId()).orElseThrow();
                p.setCreatedAt(db.getCreatedAt());
                if (p.getCreatedBy() == null) p.setCreatedBy(db.getCreatedBy());
            }
            p.setUpdatedAt(LocalDate.now());
            return repo.save(p);
        }
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public void delete(Product p) {
        repo.delete(p);
    }
}
