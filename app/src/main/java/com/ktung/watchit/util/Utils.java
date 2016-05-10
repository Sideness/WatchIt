package com.ktung.watchit.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.ktung.watchit.R;

public class Utils {
    private static Toast currentToast;

    public static void handleTheme(Context context, Activity activity, boolean hasActionBar) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int theme;
        if (hasActionBar) {
            theme = sp.getBoolean("darkTheme", false) ? R.style.AppDark : R.style.App;
        } else {
            theme = sp.getBoolean("darkTheme", false) ? R.style.AppDark_NoActionBar : R.style.App_NoActionBar;
        }

        activity.setTheme(theme);
    }

    public static void toaster(Context context, int resId, int length) {
        if (currentToast != null) {
            currentToast.cancel();
        }

        currentToast = Toast.makeText(context, resId, length);
        currentToast.show();
    }

    public static void toaster(Context context, String str, int length) {
        if (currentToast != null) {
            currentToast.cancel();
        }

        currentToast = Toast.makeText(context, str, length);
        currentToast.show();
    }
}
