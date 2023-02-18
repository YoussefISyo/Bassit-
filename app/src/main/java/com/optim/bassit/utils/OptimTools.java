package com.optim.bassit.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.App;
import com.optim.bassit.PagedRecyclerView;
import com.optim.bassit.PagedScrollView;
import com.optim.bassit.R;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Photo;
import com.optim.bassit.ui.activities.PreviewPhotoActivity;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OptimTools {


    public static RequestCreator getPicasso(String s) {
        return Picasso.get().load(s);//.placeholder( R.drawable.progress_animation ).error(R.drawable.ic_error);
    }


    public static String getAbsolutePathFromUri(Uri uri, Context ctx) {


        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = ctx.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void Alert(Context ctx, int message) {
        new android.os.Handler(ctx.getMainLooper()).post(() -> {
            new AlertDialog.Builder(ctx)
                    .setTitle(R.string.app_name)
                    .setMessage(message)
                    .setPositiveButton(R.string.text_ok, null).show();
        });
    }

    public static void AlertWait(Context ctx, String message, AlertOkListener handler) {
        new android.os.Handler(ctx.getMainLooper()).post(() -> {
            new AlertDialog.Builder(ctx)
                    .setTitle(R.string.app_name)
                    .setMessage(message)
                    .setPositiveButton(R.string.text_ok, (a, b) -> {
                        handler.onSuccess();
                        a.dismiss();
                    }).show();
        });
    }


    public static void AlertWait(Context ctx, int message, AlertOkListener handler) {

        new android.os.Handler(ctx.getMainLooper()).post(() -> {
            new AlertDialog.Builder(ctx)
                    .setTitle(R.string.app_name)
                    .setMessage(message)
                    .setPositiveButton(R.string.text_ok, (a, b) -> {
                        handler.onSuccess();
                        a.dismiss();
                    }).show();
        });


    }

    public static void Alert(Context ctx, String message) {
        new android.os.Handler(ctx.getMainLooper()).post(() -> {
            new AlertDialog.Builder(ctx)
                    .setTitle(R.string.app_name)
                    .setMessage(AppData.TR(message))
                    .setPositiveButton(R.string.text_ok, null).show();
        });
    }

    public static void AlertYesNo(Context ctx, int message, String ok_button, String cancel_button, AlertOkListener handler) {
        AlertYesNo(ctx, ctx.getResources().getString(message), ok_button, cancel_button, handler);
    }


    public static void AlertYesNo(Context ctx, int message, AlertOkListener handler) {
        AlertYesNo(ctx, ctx.getResources().getString(message), "Oui", "Non", handler);
    }

    public static void AlertConfirmCancel(Context ctx, int message, AlertOkListener handler) {
        AlertYesNo(ctx, ctx.getResources().getString(message), "Confirmer", "Annuler", handler);
    }

    public static void AlertYesNo(Context ctx, String message, AlertOkListener handler) {
        AlertYesNo(ctx, message, "Oui", "Non", handler);
    }

    public static void AlertConfirmCancel(Context ctx, String message, AlertOkListener handler) {
        AlertYesNo(ctx, message, "Confirmer", "Annuler", handler);
    }


    public static void AlertYesNo(Context ctx, String message, String ok_button, String cancel_button, AlertOkListener handler) {
        new AlertDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(ok_button, (a, b) -> {
                    handler.onSuccess();
                    a.dismiss();
                })
                .setNegativeButton(cancel_button, null).show();
    }

    /**
     * @param view         View to animate
     * @param toVisibility Visibility at the end of animation
     * @param toAlpha      Alpha at the end of animation
     * @param duration     Animation duration in ms
     */
    public static void animateView(final View view, final int toVisibility, float toAlpha, int duration) {
        boolean show = toVisibility == View.VISIBLE;
        if (show) {
            view.setAlpha(0);
        }
        view.setVisibility(View.VISIBLE);
        view.animate()
                .setDuration(duration)
                .alpha(show ? toAlpha : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(toVisibility);
                    }
                });
    }

    public static String toApiString(double vv) {
        return String.valueOf(vv).replace(",", ".");
    }

    static Picasso picasso;
    static LruCache picassoLruCache;

    public static void ClearPicassoCache(Context ctx) {


        picassoLruCache = new LruCache(ctx);

// Set cache
        picasso = new Picasso.Builder(ctx)
                .memoryCache(picassoLruCache)
                .build();

// Clear cache
        picassoLruCache.clear();
    }

    public static PagedRecyclerView injectPaginationHavingLayoutManager(RecyclerView recyclerView, paginationHandler handler) {

        PagedRecyclerView endlessRecyclerView = new PagedRecyclerView(recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (handler != null)
                    handler.loadMore(page, totalItemsCount);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerView);
        return endlessRecyclerView;
    }

    public static PagedScrollView injectPaginationHavingLayoutManager(ScrollView scroll, RecyclerView.LayoutManager lmz, paginationHandler handler) {
        LinearLayoutManager lm = (LinearLayoutManager) lmz;
        PagedScrollView endlessRecyclerView = new PagedScrollView(scroll, lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (handler != null)
                    handler.loadMore(page, totalItemsCount);
            }
        };
        scroll.getViewTreeObserver().addOnScrollChangedListener(endlessRecyclerView);

        return endlessRecyclerView;
    }

    public static String generateRandomStringUnique() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyddmm");
        Date date = new Date();
        String dt = String.valueOf(dateFormat.format(date));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("HHmm");
        String tm = String.valueOf(time.format(new Date()));//time in 24 hour format
        String id = dt + tm;
        return id;
    }


    public interface paginationHandler {
        void loadMore(int page, int totalItemsCount);
    }

    public interface AlertOkListener {
        void onSuccess();
    }


    public interface InputPromptListener {
        void onSuccess(String s);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static File resizeFile(File file, int maxWH, Context ctx) {
        try {

            String filePath = file.getPath();
            Bitmap source = BitmapFactory.decodeFile(filePath);

            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxWH;
                if (source.getHeight() <= targetHeight) { // if image already smaller than the required height
                    return file;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                }
                File f = generateFileFromBitmap(ctx, result);

                return f;
            } else {
                int targetWidth = maxWH;

                if (source.getWidth() <= targetWidth) { // if image already smaller than the required height
                    return file;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (targetWidth * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                }
                File f = generateFileFromBitmap(ctx, result);
                return f;

            }
        } catch (Exception e) {
            return file;
        }
    }

    private static File generateFileFromBitmap(Context ctx, Bitmap bitmap) throws IOException {
        //create a file to write bitmap data
        File f = new File(ctx.getCacheDir(), UUID.randomUUID().toString() + ".jpg");
        f.createNewFile();

//Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        return f;
    }


    public static String firstUpper(String str) {
        if (str == null || str.matches(""))
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public static Date DateOnly(Date date) {
        if (date == null)
            return null;
        Date d = (Date) date.clone();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date Today() {
        return DateOnly(new Date());
    }

    public static Date Now() {
        return new Date();
    }

    public static boolean isToday(Date date) {

        if (date == null)
            return false;
        Date d = DateOnly(date);
        if (d == null)
            return false;

        return d.equals(Today());
    }


    public static boolean isElapsed(Date datetime) {
        if (datetime == null)
            return true;

        Date now = Now();
        return datetime.before(now);
    }

    public static boolean isOldToday(Date date) {
        Date d = DateOnly(date);
        if (d == null)
            return false;

        return d.before(Today());
    }

    public static Date toDateFromSQL(String thedate) {

        if (thedate == null || thedate.matches(""))
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            return dateFormat.parse(thedate);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Date toDate(String thedate) {

        if (thedate == null || thedate.matches(""))
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            return dateFormat.parse(thedate);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String replaceNonstandardDigits(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (isNonstandardDigit(ch)) {
                int numericValue = Character.getNumericValue(ch);
                if (numericValue >= 0) {
                    builder.append(numericValue);
                }
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    public static boolean isNonstandardDigit(char ch) {
        return Character.isDigit(ch) && !(ch >= '0' && ch <= '9');
    }

    public static Date toDateTime(String thedate) {

        if (thedate == null || thedate.matches(""))
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            return dateFormat.parse(thedate);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Date toDateTimefromFR(String thedate) {

        if (thedate == null || thedate.matches(""))
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        try {
            return dateFormat.parse(thedate);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String dateToSQLString(Date date) {

        if (date == null)
            return null;

        try {

            String dstring = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
            return dstring;
        } catch (Exception ex) {

        }
        return null;
    }

    public static String dateToFRString(Date date) {

        if (date == null)
            return null;

        try {
            String dstring = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date);
            return dstring;
        } catch (Exception ex) {

        }
        return null;
    }

    public static String dateTimeToSQLString(Date date) {

        if (date == null)
            return null;

        try {
            String dstring = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
            return dstring;
        } catch (Exception ex) {

        }
        return null;
    }

    public static String dateTimeToFRString(Date date) {

        if (date == null)
            return null;

        try {
            String dstring = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(date);
            return dstring;
        } catch (Exception ex) {

        }
        return null;
    }

    /**
     * Change value in dp to pixels
     *
     * @param dp
     * @param context
     */
    public static float dpToPixels(int dp, Context context) {
        try {
            if (context == null)
                if (App.getAppContext() != null)
                    return dp * (App.getAppContext().getResources().getDisplayMetrics().density);
                else
                    return dp;
            return dp * (context.getResources().getDisplayMetrics().density);


        } catch (Exception ex) {
            return dp;
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Bitmap getBMP(String img_url) {
        try {
            URL url = new URL(img_url);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            return null;
        }
    }

    public static void previewPhoto(String url, Context activity) {
        Intent intent = new Intent(activity, PreviewPhotoActivity.class);
        intent.putExtra("URL", url);
        activity.startActivity(intent);
    }

    public static void previewMultiplePhotos(ArrayList<Photo> photos, int curr, Context activity) {
        Intent intent = new Intent(activity, PreviewPhotoActivity.class);
        intent.putExtra("photos", photos);
        intent.putExtra("curr", curr);
        activity.startActivity(intent);
    }

    public static double toDouble(EditText t) {

        return toDouble(t.getText().toString());
    }

    public static double toDouble(String t) {
        try {
            String p = replaceNonstandardDigits(t);
            return Double.parseDouble(p);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String getMonthName(int month) {
        try {
            return new DateFormatSymbols().getMonths()[month - 1];
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getShortMonthName(int month) {
        try {
            return new DateFormatSymbols().getShortMonths()[month - 1];
        } catch (Exception ex) {
            return "";
        }
    }
}