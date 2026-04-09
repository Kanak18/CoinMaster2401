package com.coinmasterspinlink.cmspinmaster;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("web_service/get_service.php")
    Call<RewardsResponse> getRewards(@Body RewardRequest request);

    @POST("web_service/get_service.php")
    Call<SettingsResponse> getSettings(@Body SettingRequest request);
}