package hsf302_group4.repository;

import hsf302_group4.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findTop5ByOrderByPriceDesc();

    boolean existsByProductId(String productId);

    boolean existsByProductIdAndIdNot(String productId, Integer id);

    boolean existsBySupplier_Id(Integer supplierId);
}
