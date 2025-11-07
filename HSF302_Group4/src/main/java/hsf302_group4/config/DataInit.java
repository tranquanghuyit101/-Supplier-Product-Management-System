package hsf302_group4.config;

import hsf302_group4.entity.Product;
import hsf302_group4.entity.Supplier;
import hsf302_group4.entity.UserAccount;
import hsf302_group4.repository.ProductRepository;
import hsf302_group4.repository.SupplierRepository;
import hsf302_group4.repository.UserAccountRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class DataInit implements ApplicationRunner {

    private final UserAccountRepository userRepo;
    private final SupplierRepository supplierRepo;
    private final ProductRepository productRepo;

    public DataInit(UserAccountRepository userRepo,
                           SupplierRepository supplierRepo,
                           ProductRepository productRepo) {
        this.userRepo = userRepo;
        this.supplierRepo = supplierRepo;
        this.productRepo = productRepo;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedUsers();
        seedSuppliers();
        seedProducts();
    }

    private void seedUsers() {
        ensureUser("manager1", "123456", 1); // Manager
        ensureUser("staff1",   "123456", 2); // Staff
        ensureUser("staff2",   "123456", 2); // Staff
        ensureUser("guest1",   "123456", 3); // Guest
    }

    private void ensureUser(String username, String password, int role) {
        Optional<UserAccount> u = userRepo.findByUsername(username);
        if (u.isEmpty()) {
            UserAccount ua = new UserAccount();
            ua.setUsername(username);
            ua.setPassword(password);
            ua.setRole(role);
            userRepo.save(ua);
        }
    }

    private void seedSuppliers() {
        if (supplierRepo.count() == 0) {
            supplierRepo.saveAll(List.of(
                    mkSupplier("Acme Supplies"),
                    mkSupplier("Global Source Co"),
                    mkSupplier("Blue Ocean Ltd")
            ));
        }
    }

    private Supplier mkSupplier(String name) {
        Supplier s = new Supplier();
        s.setSuppliername(name);
        return s;
    }

    private void seedProducts() {
        if (productRepo.count() > 0) return;

        List<Supplier> suppliers = supplierRepo.findAll();
        if (suppliers.isEmpty()) return;

        LocalDate today = LocalDate.now();

        Product p1 = new Product();
        p1.setProductId("P001");
        p1.setName("Paper A4");
        p1.setPrice(2.50f);
        p1.setSupplier(suppliers.get(0));
        p1.setCreatedAt(today);
        p1.setUpdatedAt(today);
        p1.setCreatedBy("staff1");

        Product p2 = new Product();
        p2.setProductId("P002");
        p2.setName("Ink Black");
        p2.setPrice(8.00f);
        p2.setSupplier(suppliers.get(Math.min(1, suppliers.size()-1)));
        p2.setCreatedAt(today);
        p2.setUpdatedAt(today);
        p2.setCreatedBy("staff1");

        Product p3 = new Product();
        p3.setProductId("P003");
        p3.setName("Stapler Pro");
        p3.setPrice(4.20f);
        p3.setSupplier(suppliers.get(0));
        p3.setCreatedAt(today);
        p3.setUpdatedAt(today);
        p3.setCreatedBy("staff1");

        productRepo.saveAll(List.of(p1, p2, p3));
    }
}
