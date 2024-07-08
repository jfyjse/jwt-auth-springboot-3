package com.jwt.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "accounts_user")
@Data
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_staff")
    private boolean isStaff;

    @Column(name = "is_superuser")
    private boolean isSuperuser;

    @CreationTimestamp
    @Column(name = "date_joined")
    private Timestamp dateJoined;

    public ERole getRole() {
        return isSuperuser ? ERole.ROLE_ADMIN : ERole.ROLE_USER;
    }

//    @Enumerated(value = EnumType.STRING)
//    private Role role;


    @OneToMany(mappedBy = "users")
    private List<Token> tokens;

}
