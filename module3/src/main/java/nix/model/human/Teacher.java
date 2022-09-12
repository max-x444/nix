package nix.model.human;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nix.model.Subject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Teacher extends Human {
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "subject_id")
    private Subject subject;
}