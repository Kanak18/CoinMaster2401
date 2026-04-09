package com.coinmasterspinlink.cmspinmaster;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.io.IOException;

public class MyNotificationHandler implements OneSignal.OSNotificationOpenedHandler {

    private Context context;
    private MediaPlayer mediaPlayer;

    public MyNotificationHandler(Context context) {
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        JSONObject additionalData = result.getNotification().getAdditionalData();
        String audioUrl = additionalData != null ? additionalData.optString("audio_url", "") : "";

        if (!audioUrl.isEmpty()) {
            playAudio(audioUrl);
        } else {
            Toast.makeText(context, "No audio URL found", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("extra_data", additionalData != null ? additionalData.toString() : "No additional data");
        context.startActivity(intent);
    }

    private void playAudio(String url) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(context, Uri.parse(url));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to play audio", Toast.LENGTH_SHORT).show();
        }
    }
}
