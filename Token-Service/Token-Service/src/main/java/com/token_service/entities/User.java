package com.token_service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String user_id;
    String username;
    String password;
    String address;
    String email;
    String phone;
}
