package hsf302_group4.repository;

import hsf302_group4.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    boolean existsBySuppliername(String suppliername);
}
