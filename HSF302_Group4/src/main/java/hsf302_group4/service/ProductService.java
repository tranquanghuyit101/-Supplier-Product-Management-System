package hsf302_group4.service;

import hsf302_group4.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Page<Product> pageAll(Pageable pageable);
    List<Product> top5ByPriceDesc();

    Product save(Product p);
    Optional<Product> findById(Integer id);
    void delete(Product p);
}
