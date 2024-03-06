package finance.L21s.userdataimporter.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "systemuser")
public class SystemUser implements Serializable {
    @Id
    private Integer id;
    private String email;

    @OneToMany(mappedBy = "systemUser", cascade = CascadeType.ALL)
    private List<Role> roles;
}
