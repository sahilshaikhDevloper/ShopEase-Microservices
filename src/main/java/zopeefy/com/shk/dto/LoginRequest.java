package zopeefy.com.shk.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginRequest {

    public String email;
    private String password;
}
