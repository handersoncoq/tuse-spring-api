package com.tuse.tuse.models;

import com.tuse.tuse.requests.create.NewUserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column
    private String username; //  = "Tu$eUser" + UUID.randomUUID().getLeastSignificantBits();
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private String password; // = String.valueOf(UUID.randomUUID());
    @Column(name = "registration_date")
    private Date registrationDate;
    @Column(name = "is_active")
    private boolean isActive = true;
    @Column(name = "is_admin")
    private boolean isAdmin = false;

    public User(NewUserRequest userRequest) {
        this.username = userRequest.getUsername();
        this.firstName = userRequest.getFirstName();
        this.lastName = userRequest.getLastName();
        this.password = userRequest.getPassword();
    }
}
