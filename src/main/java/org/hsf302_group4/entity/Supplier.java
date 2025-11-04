package org.hsf302_group4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "suppliername", length = 50, nullable = false)
    @Size(min = 5, max = 50, message = "Supplier name must be 5-50 characters")
    private String suppliername;
}
