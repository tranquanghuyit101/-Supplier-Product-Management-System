package hsf302_group4.controller;

import hsf302_group4.entity.Supplier;
import hsf302_group4.entity.UserAccount;
import hsf302_group4.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loginUser");
        if (user == null) return "redirect:/";
        if (user.getRole() != 1) {
            model.addAttribute("error", "You have no permission to access this function!");
            return "redirect:/products";
        }
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("supplierForm", new Supplier());
        return "suppliers";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("supplierForm") Supplier form,
                      BindingResult br, HttpSession session, Model model, RedirectAttributes ra) {
        UserAccount user = (UserAccount) session.getAttribute("loginUser");
        if (user == null) return "redirect:/";
        if (user.getRole() != 1) {
            ra.addFlashAttribute("error", "You have no permission to access this function!");
            return "redirect:/products";
        }
        // --- Kiểm tra lỗi form ---
        if (br.hasErrors()) {
            model.addAttribute("suppliers", supplierService.findAll());
            return "suppliers";
        }
        // --- Kiểm tra trùng tên supplier ---
        if (supplierService.existsBySuppliername(form.getSuppliername())) {
            ra.addFlashAttribute("error", "Supplier name already exists! Please choose another name.");
            return "redirect:/suppliers";
        }
        // --- Lưu dữ liệu ---
        supplierService.create(form);
        ra.addFlashAttribute("success", "Created supplier successfully.");
        return "redirect:/suppliers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, HttpSession session, Model model, RedirectAttributes ra) {
        UserAccount user = (UserAccount) session.getAttribute("loginUser");
        if (user == null) return "redirect:/";
        if (user.getRole() != 1) {
            ra.addFlashAttribute("error", "You have no permission to access this function!");
            return "redirect:/products";
        }
        model.addAttribute("supplierForm", supplierService.findById(id));
        model.addAttribute("suppliers", supplierService.findAll());
        return "suppliers";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("supplierForm") Supplier form,
                         BindingResult br, HttpSession session, Model model, RedirectAttributes ra) {
        UserAccount user = (UserAccount) session.getAttribute("loginUser");
        if (user == null) return "redirect:/";
        if (user.getRole() != 1) {
            ra.addFlashAttribute("error", "You have no permission to access this function!");
            return "redirect:/products";
        }
        if (br.hasErrors()) {
            model.addAttribute("suppliers", supplierService.findAll());
            return "suppliers";
        }
        supplierService.update(id, form);
        ra.addFlashAttribute("success", "Updated supplier successfully.");
        return "redirect:/suppliers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        UserAccount user = (UserAccount) session.getAttribute("loginUser");
        if (user == null) return "redirect:/";
        if (user.getRole() != 1) {
            ra.addFlashAttribute("error", "You have no permission to access this function!");
            return "redirect:/products";
        }
        try {
            supplierService.delete(id);
            ra.addFlashAttribute("success", "Deleted supplier successfully.");
        } catch (IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Delete failed: " + ex.getMessage());
        }
        return "redirect:/suppliers";
    }
}
