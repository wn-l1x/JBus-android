package com.wendyDharmawanJBusER.request;
public class UtilsApi {
    public static final String BASE_URL_API = "http://192.168.103.28:5009/";

    public static BaseApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
