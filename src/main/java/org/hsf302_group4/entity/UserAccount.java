package org.hsf302_group4.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    /** 1=Manager,2=Staff,3=Guest */
    @Column(nullable = false)
    private Integer role;
}
