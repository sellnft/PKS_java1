package javafxfrontend.apis;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiList {
    @POST("/register")
    Call<UserRegisterDTO> register(@Body UserRegisterDTO request);

}
