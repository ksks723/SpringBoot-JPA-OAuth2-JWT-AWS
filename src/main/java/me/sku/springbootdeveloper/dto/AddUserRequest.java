package me.sku.springbootdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class AddUserRequest {//사용자 정보를 담고 있는 객체
    private String email;
    private String password;
}
