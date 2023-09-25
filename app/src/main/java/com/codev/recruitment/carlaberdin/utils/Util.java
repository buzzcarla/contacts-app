package com.codev.recruitment.carlaberdin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Util methods
 */
public class Util {

    public static final String KEY_SHARED_PREF = "myPref";
    public static final String KEY_FIRST_APP_LAUNCH = "FIRST_APP_LAUNCH";
    public static final String KEY_ENCRYPTION_ON= "ENCRYPTION_ON";

    /**
     * Function that crops an image into a circle
     */
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap) {
        int widthLight = bitmap.getWidth();
        int heightLight = bitmap.getWidth();

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paintColor = new Paint();
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new android.graphics.Rect(0, 0, widthLight, heightLight));

        canvas.drawRoundRect(rectF, widthLight / 2, heightLight / 2, paintColor);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paintImage);

        return output;
    }

    /**
     * Get byte stream of a Bitmap
     */
    public static byte[] getBytes(final Bitmap bitmap, int quality) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream);
        final byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        return bytes;
    }

    /**
     * Encode byte stream to Base64
     */
    public static String encodeImageToBase64(byte[] byteArray) {
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Decode Base64 image to Bitmap
     */
    public static Bitmap decodeBase64ToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
