package nix.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
public class Gang {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "gang_id")
    private String id;
    private String name;
    @OneToMany(mappedBy = "gang", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Student> students = new LinkedHashSet<>();
}