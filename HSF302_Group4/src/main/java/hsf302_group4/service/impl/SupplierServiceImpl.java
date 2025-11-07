package hsf302_group4.service.impl;

import hsf302_group4.entity.Supplier;
import hsf302_group4.repository.ProductRepository;
import hsf302_group4.repository.SupplierRepository;
import hsf302_group4.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository repo;
    private final ProductRepository productRepo; // <<-- MỚI

    public SupplierServiceImpl(SupplierRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    @Override
    public List<Supplier> findAll() {
        return repo.findAll();
    }

    @Override
    public Supplier findById(Integer id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public Supplier create(Supplier s) {
        return repo.save(s);
    }

    @Override
    public Supplier update(Integer id, Supplier s) {
        Supplier old = repo.findById(id).orElseThrow();
        old.setSuppliername(s.getSuppliername());
        return repo.save(old);
    }

    @Override
    public void delete(Integer id) {
        if (productRepo.existsBySupplier_Id(id)) {
            throw new IllegalStateException(
                    "Cannot delete supplier because there are products referencing it. " +
                            "Please reassign or delete those products first."
            );
        }
        repo.deleteById(id);
    }

    @Override
    public boolean existsBySuppliername(String suppliername) {
        return repo.existsBySuppliername(suppliername);
    }
}
