package com.example.appbannon.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.utils.Utils;

import java.io.IOException;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    protected static ApiBanHang apiBanHang;

    public static Retrofit getInstance(String baseUrl, Context context) {


        if (instance == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        String accessToken = sharedPreferences.getString("accessToken", null);
                        String refreshToken = sharedPreferences.getString("refreshToken", null);
                        Request.Builder requestBuilder = original.newBuilder()
                                .method(original.method(), original.body());

                        requestBuilder.addHeader("Authorization", "Bearer " + accessToken);

                        Request request = requestBuilder.build();
                        Response response = chain.proceed(request);

                        if (response.code() == 401 && refreshToken != null) {
                            synchronized (RetrofitClient.class) {
                                Log.d("myLog", "Attempting to refresh token");
                                try {
                                    ResponseObject<String> newTokenResponse = refreshToken(refreshToken);
                                    if (newTokenResponse != null && newTokenResponse.getStatus() == 200) {
                                        accessToken = newTokenResponse.getResult();
                                        sharedPreferences.edit().putString("accessToken", accessToken).apply();

                                        // Retry the request with the new token
                                        Request newRequest = original.newBuilder()
                                                .header("Authorization", "Bearer " + accessToken)
                                                .build();
                                        return chain.proceed(newRequest);
                                    } else {
                                        Log.e("myLog", "Failed to refresh token, response: " + newTokenResponse);
                                    }
                                } catch (IOException e) {
                                    Log.e("myLog", "IOException while refreshing token", e);
                                }
                            }
                        }
                        return response;
                    }).build();
            instance = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }

    private static ResponseObject<String> refreshToken(String refreshToken) throws IOException {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL, null).create(ApiBanHang.class);
        Call<ResponseObject<String>> call = apiBanHang.refreshToken(refreshToken);
        retrofit2.Response<ResponseObject<String>> response = call.execute();
        if (!response.isSuccessful()) {
            Log.e("myLog", "Unsuccessful response: " + response);
        }
        return response.body();
    }


}
