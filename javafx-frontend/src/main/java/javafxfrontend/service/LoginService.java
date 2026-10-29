package javafxfrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafxfrontend.apis.ApiList;
import javafxfrontend.apis.RetrofitClient;
import javafxfrontend.apis.SecureTokenStorage;
import javafxfrontend.dto.ErrorResponse;
import javafxfrontend.dto.UserLoginDto;
import javafxfrontend.dto.UserResponseLoginDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginService {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final ApiList api = RetrofitClient.getApi();

    public interface CheckCallBack {
        void OnSuccess();
        void OnError(String Message);
    }

    private String parseError(Response<?> response) {
        int code = response.code();

        if (response.errorBody() != null) {
            try {
                ErrorResponse err = mapper.readValue(response.errorBody().string(), ErrorResponse.class);
                if (err != null && err.message() != null && !err.message().isEmpty()) {
                    return err.message();
                }
            } catch (Exception e) {
            }
        }

        if (code == 401 || code == 403) {
            SecureTokenStorage.clearToken();
            return "Доступ запрещен.";
        }

        return "Ошибка сервера: " + code;
    }

    public void login(UserLoginDto request, CheckCallBack callback) {
        api.login(request).enqueue(new Callback<UserResponseLoginDto>() {
            @Override
            public void onResponse(Call<UserResponseLoginDto> call, Response<UserResponseLoginDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SecureTokenStorage.saveToken(response.body().token());
                    callback.OnSuccess();
                } else {
                    callback.OnError(parseError(response));
                }
            }

            @Override
            public void onFailure(Call<UserResponseLoginDto> call, Throwable t) {
                callback.OnError("Сервер недоступен: " + t.getMessage());
            }
        });
    }
}