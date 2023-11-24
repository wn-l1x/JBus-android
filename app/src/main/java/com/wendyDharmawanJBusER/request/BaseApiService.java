package com.wendyDharmawanJBusER.request;
import com.wendyDharmawanJBusER.model.Account;
import com.wendyDharmawanJBusER.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);

    @POST("account/register") Call<BaseResponse<Account>> register(
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/login") Call<BaseResponse<Account>> login(
            @Query("email") String email,
            @Query("password") String password
    );

}

