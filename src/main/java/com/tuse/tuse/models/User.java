package com.tuse.tuse.models;

import com.tuse.tuse.requests.NewUserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String username; //= "Tu$eUser" + UUID.randomUUID().getLeastSignificantBits();
    @Column
    private String password; //= String.valueOf(UUID.randomUUID());
    @Column(name = "is_admin")
    private boolean isAdmin = false;
    @Column(name = "is_active")
    private boolean isActive = true;

    public User(NewUserRequest newUserRequest) {
        this.username = newUserRequest.getUsername();
        this.password = newUserRequest.getPassword();
    }
}
