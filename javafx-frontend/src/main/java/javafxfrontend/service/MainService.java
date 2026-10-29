package javafxfrontend.service;

import javafxfrontend.apis.ApiList;
import javafxfrontend.apis.RetrofitClient;
import javafxfrontend.dto.AnnouncementAddedRequestDto;
import javafxfrontend.dto.AnnouncementCancelledStatusDto;
import javafxfrontend.dto.AnnouncementDoneStatusDto;
import javafxfrontend.dto.AnnouncementResponseDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainService {
    private final ApiList api = RetrofitClient.getApi();

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    public void loadAllAnnouncements(DataCallback<List<AnnouncementResponseDto>> callback) {
        api.getAllAnnouncements().enqueue(new Callback<List<AnnouncementResponseDto>>() {
            @Override
            public void onResponse(Call<List<AnnouncementResponseDto>> call, Response<List<AnnouncementResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка загрузки: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AnnouncementResponseDto>> call, Throwable t) {
                callback.onError("Сеть недоступна: " + t.getMessage());
            }
        });
    }

    public void createAnnouncement(AnnouncementAddedRequestDto request, DataCallback<AnnouncementResponseDto> callback) {
        api.createAnnouncement(request).enqueue(new Callback<AnnouncementResponseDto>() {
            @Override
            public void onResponse(Call<AnnouncementResponseDto> call, Response<AnnouncementResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка создания: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementResponseDto> call, Throwable t) {
                callback.onError("Сеть недоступна: " + t.getMessage());
            }
        });
    }

    public void cancelAnnouncement(Integer id, DataCallback<AnnouncementCancelledStatusDto> callback) {
        api.cancelAnnouncement(id).enqueue(new Callback<AnnouncementCancelledStatusDto>() {
            @Override
            public void onResponse(Call<AnnouncementCancelledStatusDto> call, Response<AnnouncementCancelledStatusDto> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка отмены: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementCancelledStatusDto> call, Throwable t) {
                callback.onError("Сеть недоступна: " + t.getMessage());
            }
        });
    }
    public void setDoneStatus(Integer id, String comment, DataCallback<AnnouncementDoneStatusDto> callback) {
        api.setDoneStatus(id, comment).enqueue(new Callback<AnnouncementDoneStatusDto>() {
            @Override
            public void onResponse(Call<AnnouncementDoneStatusDto> call, Response<AnnouncementDoneStatusDto> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    // Если сервер вернул ошибку (например, 400 из-за пустого комментария)
                    callback.onError("Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementDoneStatusDto> call, Throwable t) {
                callback.onError("Сеть недоступна: " + t.getMessage());
            }
        });
    }
    public void updateStatusAndComment(Integer id, String status, String comment,
                                       DataCallback<AnnouncementResponseDto> callback) {
        api.updateStatusAndComment(id, status, comment).enqueue(new Callback<AnnouncementResponseDto>() {
            @Override
            public void onResponse(Call<AnnouncementResponseDto> call, Response<AnnouncementResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String msg = "Ошибка: " + response.code();
                    if (response.code() == 400) {
                        msg = "Проверьте данные (комментарий обязателен для DONE/CANCELLED)";
                    }
                    callback.onError(msg);
                }
            }

            @Override
            public void onFailure(Call<AnnouncementResponseDto> call, Throwable t) {
                callback.onError("Сеть недоступна: " + t.getMessage());
            }
        });
    }
}