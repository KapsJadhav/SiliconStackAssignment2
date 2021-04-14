package com.kaps.siliconstackreport.model.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.kaps.siliconstackreport.BuildConfig;
import com.kaps.siliconstackreport.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AppConstants {


    @SuppressLint("MissingPermission")
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private static class ApiConfiguration {
        private static final String SERVER_URL = "https://freeapi.rdewan.dev/";   // Production
    }


    public static class ApiNames {
        public static final String API_URL = ApiConfiguration.SERVER_URL + "api/no_auth/";
        public static final String GET_ALL_TASK = "get_all_task";
        public static final String ADD_TASK = "add_task";
        public static final String EDIT_TASK = "update_task";
        public static final String DELETE_TASK = "delete_task";
        public static final String SEARCH_TASK = "search/";
    }

    public static class Status {
        public static String ADD = "0";
        public static String UPDATE = "1";
        public static int SUCCESS = 200;
        public static int FAILED = 404;
        public static int SESSION_OUT = 201;
        public static int NOT_ACCESS = 401;
    }

    public static class Keys {
        public static final String REPORT = "REPORT";
        public static final String FROM = "FROM";
    }

    public static class Formats {
        public static String DATE_FORMAT = "yyyy-MM-dd";
        public static String TIME_FORMAT = "hh:mm:ss a";
        public static String TIME_FORMAT_24 = "HH:mm:ss";
        public static String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss a";
        public static String USER_DATE_TIME_FORMAT = "EEE, dd MMM yyyy | HH:mm";
        public static String USER_DATE_FORMAT = "dd MMM yyyy\nEEEE";
        public static String USER_DATE_FORMAT_SHOW = "EEE dd MMM yyyy HH:mm";
        public static String USER_TIME_FORMAT = "HH:mm";
        public static String DATE_TIME_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        public static String USER_DATE_TIME_FORMAT_SERVER = "yyyy-MM-dd HH:mm";
        public static String USER_DATE_TIME_FORMAT_SERVER_NEW = "yyyy-MM-dd HH:mm";


        public static String getStringInNumberFormat(double dNumber) {
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(3);
            formatter.setMaximumFractionDigits(3);
            return formatter.format(dNumber);
        }

        public static String getStringInCCNumberFormat(double dNumber) {
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(5);
            formatter.setMaximumFractionDigits(5);
            return formatter.format(dNumber);
        }

        public static String getDate(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_SERVER);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                sConvertDate = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sConvertDate;
        }

        public static String getServerDate(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(USER_DATE_FORMAT);
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                sConvertDate = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sConvertDate;
        }

        public static String getUserDate(String sDate) {
            Locale locale = new Locale("en");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", locale);
                sConvertDate = simpleDateFormat.format(date);
                PrintLog("APPPCONSTANTS", "DATE : " + sConvertDate);
            } catch (ParseException e1) {
                e1.printStackTrace();
                PrintLog("APPPCONSTANTS", "DATE : " + e1.getMessage());
            }
            return sConvertDate;
        }


        public static String getTime(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_SERVER);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String sTime = "";
            Date date = null;
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
                sTime = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sTime;
        }

        public static String getDateTime(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_SERVER);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                sConvertDate = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sConvertDate;
        }

        public static String getUserDateTime(String sDate) {
            Locale locale = new Locale("en");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(USER_DATE_TIME_FORMAT_SERVER, Locale.ENGLISH);
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(USER_DATE_TIME_FORMAT, locale);
                sConvertDate = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                PrintLog("AppConstants", "Error : " + e1.getMessage());
                e1.printStackTrace();
            }
            return sConvertDate;
        }
    }


    public static String getDeviceName(Context context) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            //return capitalize(model);
            return model;
        }
        Log.d("Log", "Manufacturer : " + manufacturer);
        Log.d("Log", "Device Model : " + model);
//        Toast.makeText(context, "Device : " + model + manufacturer, Toast.LENGTH_SHORT).show();
        // return capitalize(manufacturer) + " " + model;
        return manufacturer;
    }

    //  Get & Set Values in Shared Preferences
    public static class Preferences {

        // Boolean Preferences (Checkbox)
        public static boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getBoolean(key, defaultValue);
        }

        public static void setBooleanPreferences(Context context, String key, boolean value) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor preferenceEditor = preferences.edit();
            preferenceEditor.putBoolean(key, value);
            preferenceEditor.apply();
        }

        public static String getStringPreference(Context context, String key) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(key, null);
        }

        public static String getRecentRatePreference(Context context, String key) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(key, "0");
        }

        public static void setStringPreferences(Context context, String key, String value) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

            SharedPreferences.Editor preferenceEditor = preferences.edit();
            preferenceEditor.putString(key, value);
            preferenceEditor.apply();
        }

        public static String getStringImagePreference(Context context, String key) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(key, null);
        }

        public static void setStringImagePreferences(Context context, String key, String value) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor preferenceEditor = preferences.edit();
            preferenceEditor.putString(key, value);
            preferenceEditor.apply();
        }

        public static int getIntPreference(Context context, String key, int defValue) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getInt(key, defValue);
        }

        public static void setIntPreferences(Context context, String key, int value) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor preferenceEditor = preferences.edit();
            preferenceEditor.putInt(key, value);
            preferenceEditor.apply();
        }
    }

    //  copy wallet address
    public static class Copy {
        public static void copyText(Context context, String sText) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("WalletAddress", sText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Wallet address has been copied",
                    Toast.LENGTH_LONG).show();
        }
    }

    //  copy Key
    public static class CopyKey {
        public static void copyText(Context context, String sText) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Key", sText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Authentication key has been copied, Please paste this key in Google Authenticator app", Toast.LENGTH_LONG).show();
        }
    }

    public static class Paste {

        public static String pasteText(Context context) {
            String pasteText = "";
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.hasPrimaryClip() == true) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                pasteText = item.getText().toString();
            } else {
                Toast.makeText(context, "Nothing to Paste", Toast.LENGTH_SHORT).show();
            }
            return pasteText;
        }

    }

    // Print Logs
    public static void PrintLog(String sFrom, String sMessage) {
        if (BuildConfig.DEBUG) {
            Log.d(sFrom, sMessage);
        }
    }

    // Print Toast
    public static void showToastMessage(Context context, String sMessage) {
        Toast.makeText(context, "" + sMessage, Toast.LENGTH_SHORT).show();
    }

    //  Progress Dialog

    public static Dialog progressDialog;

    public static void showProgressDialog(Context context) {
        /*if (((Activity) context) != null && !((Activity) context).isFinishing()) {
            try {
                if (isProgressDialogShowing()) {
                    return;
                }
                if (((Activity) context) != null && ((Activity) context).isFinishing()) {
                    return;
                }
                progressDialog = new Dialog(context, android.R.style.Theme_Translucent);
                if (progressDialog != null) {
                    progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    progressDialog.getWindow().setGravity(Gravity.CENTER);
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.layout_progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    progressDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public static boolean isProgressDialogShowing() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        }
        return false;
    }

    public static void closeProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
