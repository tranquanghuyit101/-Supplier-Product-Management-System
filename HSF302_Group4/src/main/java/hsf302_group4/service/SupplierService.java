package hsf302_group4.service;

import hsf302_group4.entity.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> findAll();
    Supplier findById(Integer id);
    Supplier create(Supplier s);
    Supplier update(Integer id, Supplier s);
    void delete(Integer id);
    boolean existsBySuppliername(String suppliername);
}
