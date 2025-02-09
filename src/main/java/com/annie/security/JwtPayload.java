package com.annie.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtPayload {
    private String email;
    private String role;

    // Sửa lại phương thức toMap để trả về Map<String, Object> thay vì JwtPayload
    public Map<String, Object> toMap() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", role);
        return claims;
    }
}
