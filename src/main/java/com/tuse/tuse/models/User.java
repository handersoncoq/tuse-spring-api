package com.tuse.tuse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User {

    @Id
    @Column
    private String username = "Tu$eUser" + UUID.randomUUID().getLeastSignificantBits();
    @Column
    private String password = String.valueOf(UUID.randomUUID());
    @Column(name = "is_admin")
    private boolean isAdmin = false;

}
