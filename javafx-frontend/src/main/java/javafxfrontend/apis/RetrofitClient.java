package javafxfrontend.apis;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {
    private static final Retrofit retrofit;

    static {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }


    public static ApiList getApi() {
        return retrofit.create(ApiList.class);
    }
}