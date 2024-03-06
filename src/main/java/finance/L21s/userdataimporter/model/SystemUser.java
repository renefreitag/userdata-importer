package finance.L21s.userdataimporter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Builder
@Table(name = "systemuser")
public class SystemUser implements Serializable {
    @Id
    private Integer id;
    private String email;

    @OneToMany(mappedBy = "systemUser", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Role> roles;
}
