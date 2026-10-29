package javafxfrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafxfrontend.apis.ApiList;
import javafxfrontend.apis.RetrofitClient;
import javafxfrontend.dto.ErrorResponse;
import javafxfrontend.dto.UserRegisterRequestDto;
import javafxfrontend.dto.UserResponseRegisterDto;
import javafxfrontend.dto.UserValExistanceDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.Callable;

public class RegistrationService {
    ApiList api = RetrofitClient.getApi();

    private static final ObjectMapper mapper = new ObjectMapper();

    public interface CheckCallBack {
        void OnSuccess();

        void OnError(String Message);
    }

    private String parseError(Response<?> response) {
        if (response.errorBody() == null) {
            return "Ошибка сервиса.";
        }
        try {
            ErrorResponse err = mapper.readValue(response.errorBody().string(), ErrorResponse.class);
            return err.message();
        } catch (Exception e) {
            return "Ошибка сервера.";
        }
    }

    public void checkExistance(UserValExistanceDto request, CheckCallBack callback) {
        System.out.println("Отправляю запрос на проверку...");
        api.pre_register_val(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("ПРИШЕЛ ОТВЕТ! Код: " + response.code());
                if (response.isSuccessful()) {
                    callback.OnSuccess();
                } else {
                    String errorMessage = parseError(response);
                    callback.OnError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                System.out.println("ОШИБКА СЕТИ: " + throwable.getMessage());
                throwable.printStackTrace();
                callback.OnError("Сервер не доступен.");
            }
        });
    }

    public void registerUser(UserRegisterRequestDto request, CheckCallBack callback) {
        System.out.println("Отправляю: login=" + request.login() + ", email=" + request.email() + ", fio=" + request.fio());
        api.register(request).enqueue(new Callback<UserResponseRegisterDto>() {
            @Override
            public void onResponse(Call<UserResponseRegisterDto> call, Response<UserResponseRegisterDto> response) {
                if (response.isSuccessful()) {
                    callback.OnSuccess();
                } else {
                    callback.OnError(parseError(response));
                }
            }

            @Override
            public void onFailure(Call<UserResponseRegisterDto> call, Throwable throwable) {
                callback.OnError("Сервер не доступен.");
            }
        });
    }
}
