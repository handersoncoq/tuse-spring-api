package com.tuse.tuse.requests.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateUserRequest {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
}
