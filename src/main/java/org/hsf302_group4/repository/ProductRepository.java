package org.hsf302_group4.repository;

import org.hsf302_group4.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);

    // Top N highest price
    List<Product> findTop5ByOrderByPriceDesc();

    // find by productid if need unique check
    boolean existsByProductid(String productid);
}
