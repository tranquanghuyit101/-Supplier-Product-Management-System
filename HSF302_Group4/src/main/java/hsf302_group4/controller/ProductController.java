package hsf302_group4.controller;

import hsf302_group4.entity.Product;
import hsf302_group4.entity.UserAccount;
import hsf302_group4.service.ProductService;
import hsf302_group4.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final SupplierService supplierService;

    private boolean isManager(UserAccount u){ return u != null && u.getRole() == 1; }
    private boolean isStaff(UserAccount u){ return u != null && u.getRole() == 2; }

    @GetMapping
    public String index(@RequestParam(defaultValue = "0") int page,
                        HttpSession session,
                        Model model) {
        UserAccount u = (UserAccount) session.getAttribute("loginUser");
        if (u == null) return "redirect:/";

        model.addAttribute("username", u.getUsername());

        if (isManager(u)) {
            model.addAttribute("mode", "MANAGER_VIEW");
            model.addAttribute("top5", productService.top5ByPriceDesc());
            return "product";
        }

        if (isStaff(u)) {
            model.addAttribute("mode", "STAFF_VIEW");
            Page<Product> pageData = productService.pageAll(PageRequest.of(Math.max(page, 0), 5));
            model.addAttribute("pageData", pageData);
            model.addAttribute("productForm", new Product());
            model.addAttribute("suppliers", supplierService.findAll());
            model.addAttribute("currentPage", Math.max(page, 0));
            return "product";
        }
        return "redirect:/";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("productForm") @Valid Product form,
                      BindingResult br,
                      @RequestParam(defaultValue = "0") int page,
                      HttpSession session,
                      Model model) {
        UserAccount u = (UserAccount) session.getAttribute("loginUser");
        if (u == null) return "redirect:/";
        if (!isStaff(u)) return "redirect:/products";

        if (br.hasErrors()) {
            return renderStaffPage(model, page, u.getUsername(), form);
        }
        form.setId(null);
        form.setCreatedAt(LocalDate.now());
        form.setUpdatedAt(LocalDate.now());
        form.setCreatedBy(u.getUsername());

        try {
            productService.save(form);
            return "redirect:/products?page=" + Math.max(page, 0);
        } catch (DuplicateKeyException ex) {
            br.rejectValue("productId", "duplicate", "Product ID already exists");
            return renderStaffPage(model, page, u.getUsername(), form);
        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("productId", "duplicate", "Product ID already exists");
            return renderStaffPage(model, page, u.getUsername(), form);
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
                       @RequestParam(defaultValue = "0") int page,
                       HttpSession session,
                       Model model) {
        UserAccount u = (UserAccount) session.getAttribute("loginUser");
        if (u == null) return "redirect:/";
        if (!isStaff(u)) return "redirect:/products";

        return productService.findById(id).map(p -> {
            if (!u.getUsername().equals(p.getCreatedBy())) {
                return "redirect:/products?page=" + Math.max(page, 0);
            }
            model.addAttribute("mode", "STAFF_VIEW");
            model.addAttribute("productForm", p);
            model.addAttribute("suppliers", supplierService.findAll());
            model.addAttribute("pageData", productService.pageAll(PageRequest.of(Math.max(page, 0), 5)));
            model.addAttribute("currentPage", Math.max(page, 0));
            model.addAttribute("username", u.getUsername());
            return "product";
        }).orElse("redirect:/products?page=" + Math.max(page, 0));
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @ModelAttribute("productForm") @Valid Product form,
                         BindingResult br,
                         @RequestParam(defaultValue = "0") int page,
                         HttpSession session,
                         Model model) {
        UserAccount u = (UserAccount) session.getAttribute("loginUser");
        if (u == null) return "redirect:/";
        if (!isStaff(u)) return "redirect:/products";

        var opt = productService.findById(id);
        if (opt.isEmpty()) return "redirect:/products?page=" + Math.max(page, 0);

        Product db = opt.get();
        if (!u.getUsername().equals(db.getCreatedBy())) {
            return "redirect:/products?page=" + Math.max(page, 0);
        }

        if (br.hasErrors()) {
            return renderStaffPage(model, page, u.getUsername(), form);
        }

        db.setProductId(form.getProductId());
        db.setName(form.getName());
        db.setPrice(form.getPrice());
        db.setSupplier(form.getSupplier());
        db.setUpdatedAt(LocalDate.now());

        try {
            productService.save(db);
            return "redirect:/products?page=" + Math.max(page, 0);
        } catch (DuplicateKeyException ex) {
            br.rejectValue("productId", "duplicate", "Product ID already exists");
            return renderStaffPage(model, page, u.getUsername(), form);
        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("productId", "duplicate", "Product ID already exists");
            return renderStaffPage(model, page, u.getUsername(), form);
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id,
                         @RequestParam(defaultValue = "0") int page,
                         HttpSession session) {
        UserAccount u = (UserAccount) session.getAttribute("loginUser");
        if (u == null) return "redirect:/";
        if (!isStaff(u)) return "redirect:/products";

        return productService.findById(id).map(p -> {
            if (!u.getUsername().equals(p.getCreatedBy())) {
                return "redirect:/products?page=" + Math.max(page, 0);
            }
            productService.delete(p);
            return "redirect:/products?page=" + Math.max(page, 0);
        }).orElse("redirect:/products?page=" + Math.max(page, 0));
    }

    private String renderStaffPage(Model model, int page, String username, Product form) {
        model.addAttribute("mode", "STAFF_VIEW");
        model.addAttribute("username", username);
        model.addAttribute("productForm", form == null ? new Product() : form);
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("pageData", productService.pageAll(PageRequest.of(Math.max(page, 0), 5)));
        model.addAttribute("currentPage", Math.max(page, 0));
        return "product";
    }
}
