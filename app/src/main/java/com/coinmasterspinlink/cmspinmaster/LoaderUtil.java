package com.coinmasterspinlink.cmspinmaster;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoaderUtil {
    private static Dialog loaderDialog;

    // Show loader
    public static void showLoader(Context context, String message) {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            return; // Prevent multiple loaders
        }

        // Initialize dialog
        loaderDialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.loader_layout, null);
        loaderDialog.setContentView(view);

        // Set message if provided
        TextView messageTextView = view.findViewById(R.id.loader_message);
        if (message != null && !message.isEmpty()) {
            messageTextView.setText(message);
            messageTextView.setVisibility(View.VISIBLE);
        } else {
            messageTextView.setVisibility(View.GONE);
        }

        // Configure dialog
        loaderDialog.setCancelable(false); // Prevent dismissing by back button
        loaderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loaderDialog.show();
    }

    // Dismiss loader
    public static void hideLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }
}
