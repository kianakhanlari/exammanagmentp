package ir.maktab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "exammanagment.user_seq",
            allocationSize = 1
    )

    private long userID;
    @Column(unique = true)
    private String userName;
    private String password;
    private String fullName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isApproved;
    public User(String userName,
                String password,
                String fullName,
                Role role,
                boolean isApproved) {

        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.isApproved = isApproved;
    }

}
