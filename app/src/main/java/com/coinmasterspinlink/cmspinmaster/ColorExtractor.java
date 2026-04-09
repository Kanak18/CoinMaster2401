package com.coinmasterspinlink.cmspinmaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import androidx.palette.graphics.Palette;

public class ColorExtractor {
    
    public interface ColorExtractionCallback {
        void onColorsExtracted(int primaryColor, int secondaryColor, int accentColor);
    }
    
    public static void extractColorsFromIcon(Context context, int iconResourceId, ColorExtractionCallback callback) {
        try {
            // Load the bitmap from the icon resource
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), iconResourceId);
            
            if (bitmap != null) {
                // Generate palette from the bitmap
                Palette.from(bitmap).generate(palette -> {
                    if (palette != null) {
                        // Extract colors from the palette
                        int primaryColor = palette.getDominantColor(Color.parseColor("#A259FF")); // Fallback to current primary
                        int secondaryColor = palette.getVibrantColor(Color.parseColor("#FBC2EB")); // Fallback to current accent
                        int accentColor = palette.getMutedColor(Color.parseColor("#7C4DFF")); // Fallback to current button color
                        
                        // Call the callback with extracted colors
                        callback.onColorsExtracted(primaryColor, secondaryColor, accentColor);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Return default colors if extraction fails
            callback.onColorsExtracted(
                Color.parseColor("#A259FF"), // Default primary
                Color.parseColor("#FBC2EB"), // Default secondary
                Color.parseColor("#7C4DFF")  // Default accent
            );
        }
    }
    
    public static void extractColorsFromBitmap(Bitmap bitmap, ColorExtractionCallback callback) {
        if (bitmap != null) {
            Palette.from(bitmap).generate(palette -> {
                if (palette != null) {
                    int primaryColor = palette.getDominantColor(Color.parseColor("#A259FF"));
                    int secondaryColor = palette.getVibrantColor(Color.parseColor("#FBC2EB"));
                    int accentColor = palette.getMutedColor(Color.parseColor("#7C4DFF"));
                    
                    callback.onColorsExtracted(primaryColor, secondaryColor, accentColor);
                }
            });
        }
    }
    
    // Helper method to create gradient colors from extracted colors
    public static int[] createGradientColors(int primaryColor, int secondaryColor) {
        return new int[]{primaryColor, secondaryColor};
    }
    
    // Helper method to get complementary color
    public static int getComplementaryColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[0] = (hsv[0] + 180) % 360;
        return Color.HSVToColor(hsv);
    }
    
    // Helper method to get lighter version of a color
    public static int getLighterColor(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.rgb(red, green, blue);
    }
    
    // Helper method to get darker version of a color
    public static int getDarkerColor(int color, float factor) {
        int red = (int) (Color.red(color) * (1 - factor));
        int green = (int) (Color.green(color) * (1 - factor));
        int blue = (int) (Color.blue(color) * (1 - factor));
        return Color.rgb(red, green, blue);
    }
} 