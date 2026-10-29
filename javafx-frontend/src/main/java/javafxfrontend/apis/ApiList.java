package javafxfrontend.apis;

import javafxfrontend.dto.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiList {
    @POST("/api/users/register/validate-existance")
    Call<Void> pre_register_val(@Body UserValExistanceDto request);

    @POST("/api/users/register")
    Call<UserResponseRegisterDto> register(@Body UserRegisterRequestDto  request);

    @POST("/api/users/login")
    Call<UserResponseLoginDto> login(@Body UserLoginDto request);

    @PATCH("/api/announcements/{id}/status")
    Call<AnnouncementResponseDto> updateStatusAndComment(
            @Path("id") Integer id,
            @Query("status") String status,
            @Query("comment") String comment
    );

    @GET("/api/announcements/all")
    Call<List<AnnouncementResponseDto>> getAllAnnouncements();

    @POST("/api/announcements/create")
    Call<AnnouncementResponseDto> createAnnouncement(@Body AnnouncementAddedRequestDto request);

    @PATCH("/api/announcements/setDoneStatus/{id}")
    Call<AnnouncementDoneStatusDto> setDoneStatus(
            @Path("id") Integer id,
            @Query("comment") String comment // <--- Передаем комментарий
    );
    @PATCH("/api/announcements/setCancelledStatus/{id}")
    Call<AnnouncementCancelledStatusDto> cancelAnnouncement(@Path("id") Integer id);
}
