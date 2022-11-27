package com.tuse.tuse.requests.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class NewUserRequest {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
}
