package hsf302_group4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter; import lombok.Setter;
import java.time.LocalDate;

@Entity @Table(name = "products")
@Getter @Setter
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "productid", nullable=false, unique=true, length=50)
    private String productId;

    @NotBlank
    @Size(min = 5, max = 50)
    @Column(nullable=false, length=50)
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "price ≥ 0")
    @Column(nullable=false)
    private Float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable=false)
    @NotNull
    private Supplier supplier;

    @Column(name = "created_at", nullable=false)
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable=false)
    private LocalDate updatedAt;

    @Column(name = "created_by", nullable=false, length=50)
    private String createdBy;
}
