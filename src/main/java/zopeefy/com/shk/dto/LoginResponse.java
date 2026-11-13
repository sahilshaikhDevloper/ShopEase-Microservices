package zopeefy.com.shk.dto;

import lombok.Getter;
import lombok.Setter;
import zopeefy.com.shk.entity.UserModule;


@Getter
@Setter

public class LoginResponse {


    private UserModule UserDetail;
    private String Token;

    public LoginResponse(UserModule userModule , String token){
        this.UserDetail = userModule;
        this.Token = token;
    }
}
