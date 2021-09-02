package com.wayapaychat.bank.auth.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestUtil {

    private String username;
    private String password;
}
