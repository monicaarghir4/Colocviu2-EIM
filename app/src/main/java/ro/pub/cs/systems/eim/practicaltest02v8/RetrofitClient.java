package ro.pub.cs.systems.eim.practicaltest02v8;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String DEFAULT_BASE_URL = "https://api.coindesk.com/"; // Fixed base URL for the first activity
    private static Retrofit defaultRetrofit = null;
    private static Retrofit dynamicRetrofit = null;

    // Get the default Retrofit instance (no arguments)
    public static Retrofit getClient() {
        if (defaultRetrofit == null) {
            defaultRetrofit = new Retrofit.Builder()
                    .baseUrl(DEFAULT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return defaultRetrofit;
    }

    // Get a Retrofit instance with a dynamic base URL (with arguments)
    public static Retrofit getClient(String baseUrl) {
        if (dynamicRetrofit == null || !dynamicRetrofit.baseUrl().toString().equals(baseUrl)) {
            dynamicRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return dynamicRetrofit;
    }
}