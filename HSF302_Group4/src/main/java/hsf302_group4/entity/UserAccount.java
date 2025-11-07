package hsf302_group4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter; import lombok.Setter;

@Entity @Table(name = "user_accounts")
@Getter @Setter
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, unique=true) @NotBlank
    private String username;

    @Column(nullable=false) @NotBlank
    private String password;

    @Column(nullable=false)
    private Integer role;
}
