package ir.maktab.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends  User{
    private String specialty;

    @OneToMany(mappedBy = "teacher")
    private List<Course> teachingCourses = new ArrayList<>();


    public Teacher(String firstName, String number, String lastName, Role role, boolean isActive) {
        super(firstName, number, lastName, role, isActive);
    }

}
