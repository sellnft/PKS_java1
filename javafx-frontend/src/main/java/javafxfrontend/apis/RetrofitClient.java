package javafxfrontend.apis;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    public static ApiList getApi() {
        return retrofit.create(ApiList.class);
    }
}