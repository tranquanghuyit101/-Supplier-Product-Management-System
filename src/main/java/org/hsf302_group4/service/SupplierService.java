package org.hsf302_group4.service;

import org.hsf302_group4.entity.Supplier;
import org.hsf302_group4.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    private final SupplierRepository repo;
    public SupplierService(SupplierRepository repo){ this.repo = repo; }

    public List<Supplier> findAll(){ return repo.findAll(); }
    public Supplier findById(Integer id){ return repo.findById(id).orElse(null); }
    public Supplier save(Supplier s){ return repo.save(s); }
    public void delete(Integer id){ repo.deleteById(id); }
}