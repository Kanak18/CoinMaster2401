package com.coinmasterspinlink.cmspinmaster;

import com.google.gson.annotations.SerializedName;

public class SettingsResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("publisher_id")
    private String publisherId;
    @SerializedName("ad_network")
    private String adNetwork;
    @SerializedName("show_open_ads_admob")
    private String showOpenAdsAdmob;
    @SerializedName("show_banner")
    private String showBanner;
    @SerializedName("show_inter")
    private Integer showInter;
    @SerializedName("show_native")
    private String showNative;
    @SerializedName("show_reward")
    private String showReward;
    @SerializedName("banner_ad_id")
    private String bannerAdId;
    @SerializedName("interstitial_ad_id")
    private String interstitialAdId;
    @SerializedName("native_ad_id")
    private String nativeAdId;
    @SerializedName("reward_ad_id")
    private String rewardAdId;
    @SerializedName("open_ad_id")
    private String openAdId;
    @SerializedName("interstitial_show_count")
    private Integer interstitialShowCount;
    @SerializedName("native_show_count")
    private int nativeShowCount;
    @SerializedName("app_update_status")
    private Integer appUpdateStatus;
    @SerializedName("app_new_version")
    private Integer appNewVersion;
    @SerializedName("app_update_desc")
    private String appUpdateDesc;
    @SerializedName("app_redirect_url")
    private String appRedirectUrl;
    @SerializedName("cancel_update_status")
    private boolean cancelUpdateStatus;
    @SerializedName("app_privacy_policy")
    private String appPrivacyPolicy;
    @SerializedName("app_faq")
    private String appFaq;

    // Getters and setters for all fields
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getPublisherId() { return publisherId; }
    public void setPublisherId(String publisherId) { this.publisherId = publisherId; }
    public String getAdNetwork() { return adNetwork; }
    public void setAdNetwork(String adNetwork) { this.adNetwork = adNetwork; }
    public String getShowOpenAdsAdmob() { return showOpenAdsAdmob; }
    public void setShowOpenAdsAdmob(String showOpenAdsAdmob) { this.showOpenAdsAdmob = showOpenAdsAdmob; }
    public String getShowBanner() { return showBanner; }
    public void setShowBanner(String showBanner) { this.showBanner = showBanner; }
    public Integer getShowInter() { return showInter; }
    public void setShowInter(Integer showInter) { this.showInter = showInter; }
    public String getShowNative() { return showNative; }
    public void setShowNative(String showNative) { this.showNative = showNative; }

    public String getShowReward() { return showReward; }
    public void setShowReward(String showReward) { this.showReward = showReward; }

    public String getBannerAdId() { return bannerAdId; }
    public void setBannerAdId(String bannerAdId) { this.bannerAdId = bannerAdId; }
    public String getInterstitialAdId() { return interstitialAdId; }
    public void setInterstitialAdId(String interstitialAdId) { this.interstitialAdId = interstitialAdId; }
    public String getNativeAdId() { return nativeAdId; }
    public void setNativeAdId(String nativeAdId) { this.nativeAdId = nativeAdId; }
    public String getRewardAdId() { return rewardAdId; }
    public void setRewardAdId(String rewardAdId) { this.rewardAdId = rewardAdId; }
    public String getOpenAdId() { return openAdId; }
    public void setOpenAdId(String openAdId) { this.openAdId = openAdId; }
    public Integer getInterstitialShowCount() { return interstitialShowCount; }
    public void setInterstitialShowCount(Integer interstitialShowCount) { this.interstitialShowCount = interstitialShowCount; }
    public int getNativeShowCount() { return nativeShowCount; }
    public void setNativeShowCount(int nativeShowCount) { this.nativeShowCount = nativeShowCount; }
    public Integer getAppUpdateStatus() { return appUpdateStatus; }
    public void setAppUpdateStatus(Integer appUpdateStatus) { this.appUpdateStatus = appUpdateStatus; }
    public Integer getAppNewVersion() { return appNewVersion; }
    public void setAppNewVersion(Integer appNewVersion) { this.appNewVersion = appNewVersion; }
    public String getAppUpdateDesc() { return appUpdateDesc; }
    public void setAppUpdateDesc(String appUpdateDesc) { this.appUpdateDesc = appUpdateDesc; }
    public String getAppRedirectUrl() { return appRedirectUrl; }
    public void setAppRedirectUrl(String appRedirectUrl) { this.appRedirectUrl = appRedirectUrl; }
    public boolean getCancelUpdateStatus() { return cancelUpdateStatus; }
    public void setCancelUpdateStatus(boolean cancelUpdateStatus) { this.cancelUpdateStatus = cancelUpdateStatus; }
    public String getAppPrivacyPolicy() { return appPrivacyPolicy; }
    public void setAppPrivacyPolicy(String appPrivacyPolicy) { this.appPrivacyPolicy = appPrivacyPolicy; }

    public String getAppFaq() { return appFaq; }
    public void setAppFaq(String appFaq) { this.appFaq = appFaq; }
}
