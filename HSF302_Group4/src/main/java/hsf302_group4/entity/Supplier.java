package hsf302_group4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter; import lombok.Setter;

@Entity @Table(name = "suppliers")
@Getter @Setter
public class Supplier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name="suppliername", nullable=false, length=50)
    private String suppliername;
}

