package com.optim.bassit;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optim.bassit.data.AppData;
import com.optim.bassit.ui.activities.ChatActivity;
import com.optim.bassit.ui.activities.ConsommationActivity;
import com.optim.bassit.ui.activities.MainActivity;
import com.optim.bassit.ui.activities.SplashActivity;
import com.optim.bassit.utils.OptimTools;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;



public class MyFirebaseNotification extends FirebaseMessagingService {
    public MyFirebaseNotification() {
    }

    public static final String NOTIFICATION_CHANNEL_ID = "10025";
    private boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;

        if (services.get(0).processName
                .equalsIgnoreCase(getPackageName()) && services.get(0).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            isActivityFound = true;
        }

        return isActivityFound;
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

       // showNotification3(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
       //   if (CurrentUser.getInstance().getApitoken() == null || CurrentUser.getInstance().getApitoken().matches(""))
        //    return;
       if(remoteMessage.getData().containsKey("service"))
            showNotification3(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getData().get("service"),remoteMessage.getData().get("badge"), remoteMessage.getData().get("extra"));
        else{
        Map<String, String> data = remoteMessage.getData();//remoteMessage.getData().get("him");
        String message = AppData.TR(remoteMessage.getData().get("body"), "€");
        String title = AppData.TR(remoteMessage.getData().get("title"), "€");
        String extra = remoteMessage.getData().get("extra");
        String rec = remoteMessage.getData().get("rec");
          //   if (CurrentUser.getInstance().getmCustomer() == null)
         //      return;
        //    if (rec == null || rec.matches("") || !Integer.valueOf(rec).equals(CurrentUser.getInstance().getmCustomer().getId()))
          //    return;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //     NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "default");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
        builder.setSmallIcon(R.drawable.logo);

        builder = builder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri);


        if (remoteMessage.getData().

                get("image") != null) {
            Bitmap bmp = OptimTools.getBMP(remoteMessage.getData().get("image"));
            if (bmp != null)
                builder = builder.setLargeIcon(bmp);
        }

        //        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher));


        if (extra != null && !extra.matches("")) {
            Intent notificationIntent;
            if (extra.equals("solde")) {

                notificationIntent = new Intent(this, ConsommationActivity.class);
                notificationIntent.putExtra("consommation_type", 1);
            } else if (extra.equals("bonus")) {

                notificationIntent = new Intent(this, ConsommationActivity.class);
                notificationIntent.putExtra("consommation_type", 101);


            } else {

                // This is what you are going to set a pending intent which will start once
// notification is clicked. Hopes you know how to add notification bar.
              if (remoteMessage.getData().containsKey("him") ) {
                  if(gethim().containsKey(remoteMessage.getData().get("him"))){
                      if(gethim().get(remoteMessage.getData().get("him")).equals("seen")){
                          savehim(remoteMessage.getData().get("him"), "not seen");
                          setnumchat("1");
                          ShortcutBadger.applyCount(getApplicationContext(), Integer.parseInt(getnumchat()));
                    /*      try {
                              chng(getnumchat(),bottomNav2,2);
                            //  showBadge(getApplicationContext(), bottomNav2,2,getnumchat());
                          }catch (Exception e){}*/
                      }else {
                          savehim(remoteMessage.getData().get("him"), "not seen");
                      }

                  }else {
                      savehim(remoteMessage.getData().get("him"), "not seen");
                      setnumchat("1");
                      ShortcutBadger.applyCount(getApplicationContext(), Integer.parseInt(getnumchat()));
                  /*    try {
                          chng(getnumchat(),bottomNav2,2);
                          //showBadge(getApplicationContext(), bottomNav2,2,getnumchat());
                      }catch (Exception e){}*/
                  } }
             //   if(applicationInForeground())
              //  if (rec == null || rec.matches("") || !Integer.valueOf(rec).equals(CurrentUser.getInstance().getmCustomer().getId()))
            //    if(MainActivity.runing!=null && MainActivity.runing)
                    notificationIntent = new Intent(this, Main2Activity.class);
             //     else
            ///     notificationIntent = new Intent(this, SplashActivity.class);
               /* try{
                    MainActivity.showBadge(MainActivity.context,bottomNav,2,getnumchat());

                }
                catch (Exception e){}*/

            }

            notificationIntent.putExtra("notification", extra);
            notificationIntent.setAction("android.intent.action.MAIN");
            notificationIntent.addCategory("android.intent.category.LAUNCHER");
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_CANCEL_CURRENT);

// Now, once this dummy activity starts send a broad cast to your parent activity and finish the pending activity
//(remember you need to register your broadcast action here to receive).
            BroadcastReceiver call_method = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action_name = intent.getAction();
                    if (action_name.equals("call_method")) {
                        // call your method here and do what ever you want.
                    }
                }

            };
         //   try {
                registerReceiver(call_method, new IntentFilter("call_method"));
          //  }catch (Exception e){}


            builder.setContentIntent(contentIntent);

        }


        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Message", importance);
            channel.enableLights(true);

            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert manager != null;
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            manager.createNotificationChannel(channel);
        }

        manager.notify(1412, builder.build());

        if (remoteMessage.getData().containsKey("him")) {
        //    savedatahim(remoteMessage.getData());
          String him = remoteMessage.getData().get("him");
          //  savehim(him);
           if (him != null && !him.matches("")) {

                try {
                    int ii = Integer.parseInt(him);
                    if (ii != 0)
                        for (NotificationListener ll : listeners) {
                            if (ll != null) {
                                int badge = 0;

                                if (remoteMessage.getData().containsKey("badge")) {
                                    String sbadge = remoteMessage.getData().get("badge");

                                    if (sbadge != null && !sbadge.matches("")) {
                                        badge = Integer.parseInt(sbadge);
                                    }
                                }

                                ll.doRefreshOnNotificationThread(ii, badge);
                            }
                        }
                } catch (Exception ex) {

                }
            }

        }

   }
    }

    public static String convert(Map<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }
    private void showNotification3(String tit, String text, String service,String id, String demand_id) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "default");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Dilip21")
                .setContentTitle(tit)
                .setContentText(text);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Message",
                    NotificationManager.IMPORTANCE_HIGH
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent notificationIntent = new Intent(this, SplashActivity.class);
        if(service.equals("offer")){
            notificationIntent.putExtra("service", "offer");
        }else if (service.equals("demand service")){
            notificationIntent.putExtra("service", "demand service");
        }else if (service.equals("chcek")){
            notificationIntent.putExtra("service", "check");
            notificationIntent.putExtra("id", demand_id);
            notificationIntent.putExtra("date", id);
        }


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        int bdg=2;
        try {
            if(!id.trim().isEmpty())
            bdg = Integer.valueOf(id);
        }catch (Exception e){
            bdg=2;
        }

        notificationManager.notify(bdg, notificationBuilder.build());



    }
    public static Map<String, String> revert(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(json, type);
        return map;
    }
    private void savedatahim(Map<String, String> data) {
        JSONArray jsonArray=getdatahim();
        jsonArray.put(convert(data));
        SharedPreferences settings = getSharedPreferences("compte", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("datahim",jsonArray.toString());
        editor.apply();
    }
    public JSONArray getdatahim() {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        JSONArray data=new JSONArray();
        try {
            data= new JSONArray(settings.getString("datahim",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void savehim(String him,String see) {

        Map<String, String> dd=gethim();
        dd.put(him,see);
        String json=  new Gson().toJson(dd);
        SharedPreferences settings = getSharedPreferences("compte", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("him",json);
        editor.apply();
    }

    public Map<String, String> gethim() {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        Map<String, String> data=new HashMap<String, String>();
        if(!settings.getString("him","").equals(""))
        data= revert(settings.getString("him",""));
        return data;
    }

    @Override
    public void onNewToken(String token) {
        //   Log.d("Token", token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fbtoken", token).apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", Context.MODE_PRIVATE).getString("fbtoken", "");
    }

    public void setnumchat(String num) {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        SharedPreferences.Editor editor = settings.edit();
        int s=Integer.valueOf(num)+Integer.valueOf(getnumchat());
        editor.putString("numchat",s+"");
        editor.apply();
    }

    public String getnumchat() {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        return settings.getString("numchat","0");
    }

    static List<NotificationListener> listeners = new ArrayList<>();

    public static void registerNotification(NotificationListener listener) {
        listeners.add(listener);
    }

    public static void unregisterNotification(NotificationListener listener) {
        try {
            listeners.remove(listener);
        } catch (Exception ex) {

        }
    }

    public interface NotificationListener {
        void doRefreshOnNotificationThread(Integer him, Integer badge);
    }
}
