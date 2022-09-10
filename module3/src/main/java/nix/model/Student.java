package nix.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Student extends Human {
    @Column(name = "date_of_admission")
    private LocalDateTime dateOfAdmission;
    @ManyToOne
    @JoinColumn(name = "gang_id")
    private Gang gang;

    @Override
    public String toString() {
        return "Student{" +
                "dateOfAdmission=" + dateOfAdmission +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }
}