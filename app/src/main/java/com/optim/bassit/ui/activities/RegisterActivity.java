package com.optim.bassit.ui.activities;

import static com.optim.bassit.ui.activities.LoginActivity.doLogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Customer;
import com.optim.bassit.utils.LocaleHelper;
import com.optim.bassit.utils.OptimTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    public static final int INSCRIPTION_CODE = 9001;

    @BindView(R.id.et_email)
    EditText tEmail;
    @BindView(R.id.et_pwd)
    EditText tPwd;
    @BindView(R.id.et_pwd2)
    EditText tPwd2;
    @BindView(R.id.et_userid)
    EditText tinvite;
    @BindView(R.id.btn_fb)
    Button btn_fb;
    @BindView(R.id.btn_google)
    Button btn_google;

    private static final int RC_SIGN_IN = 1037;

    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;

    @Inject
    AppApi appApi;

    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        mobile = getIntent().getExtras().getString("mobile");

        if (LocaleHelper.isRTL())
        {

            tPwd.setGravity(Gravity.RIGHT);
            tPwd2.setGravity(Gravity.RIGHT);

        }

        //************* GOOGLE **********************
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(getString(R.string.server_client_id))
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //************* FB **********************

        if (LocaleHelper.isRTL())
            tPwd.setGravity(Gravity.RIGHT);
        callbackManager = CallbackManager.Factory.create();
        try {
            LoginManager.getInstance().logOut();

        } catch (Exception ex) {
        }
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    try {
                                        String email = "";
                                        if (!object.has("email"))
                                            email = OptimTools.generateRandomStringUnique() + "@bassit-app.com";
                                        else
                                            email = object.getString("email");

                                        String first_name = object.getString("first_name");
                                        String last_name = object.getString("last_name");
                                        Customer customer = new Customer();
                                        customer.setPrenom(last_name);
                                        customer.setName(first_name);
                                        customer.setEmail(email);
                                        customer.setAccess_token(loginResult.getAccessToken().getToken());
                                        FacebookSignIn(customer);
                                    } catch (JSONException e) {


                                    }

                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "first_name,last_name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Alert("Canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Alert(exception.getMessage());
                    }
                });


//        findViewById(R.id.textView28).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bassit-app.com/privacy"));
//                startActivity(browserIntent2);
//            }
//        });
//        findViewById(R.id.textView30).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              openDialoginfo2();
//            }
//        });
    }

    private void openDialoginfo2() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.info2, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        alertDialog.show();
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String txt="<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>By downloading or using the app, these terms will automatically apply to you &ndash; you should make sure therefore that you read them carefully before using the app. You&rsquo;re not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. You&rsquo;re not allowed to attempt to extract the source code of the app, and you also shouldn&rsquo;t try to translate the app into other languages, or make derivative versions. The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it, still belong to hadri technologie.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>hadri technologie is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you&rsquo;re paying for.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>The BassitApp app stores and processes personal data that you have provided to us, in order to provide our Service. It&rsquo;s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone&rsquo;s security features and it could mean that the BassitApp app won&rsquo;t work properly or at all.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>The app does use third party services that declare their own Terms and Conditions.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>Link to Terms and Conditions of third party service providers used by the app</span></p>\n" +
                "<div style='margin-top:0cm;margin-right:0cm;margin-bottom:10.0pt;margin-left:0cm;line-height:115%;font-size:15px;font-family:\"Calibri\",sans-serif;'>\n" +
                "    <ul style=\"margin-bottom:0cm;list-style-type: disc;margin-left:20px;\">\n" +
                "        <li style='margin-top:0cm;margin-right:0cm;margin-bottom:10.0pt;margin-left:0cm;line-height:115%;font-size:15px;font-family:\"Calibri\",sans-serif;'><a href=\"https://policies.google.com/terms\" target=\"_blank\"><span style='font-family:\"Segoe UI\",sans-serif;text-decoration:none;'>Google Play Services</span></a></li>\n" +
                "    </ul>\n" +
                "</div>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>You should be aware that there are certain things that hadri technologie will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi, or provided by your mobile network provider, but hadri technologie cannot take responsibility for the app not working at full functionality if you don&rsquo;t have access to Wi-Fi, and you don&rsquo;t have any of your data allowance left.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>If you&rsquo;re using the app outside of an area with Wi-Fi, you should remember that your terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third party charges. In using the app, you&rsquo;re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you&rsquo;re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>Along the same lines, hadri technologie cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged &ndash; if it runs out of battery and you can&rsquo;t turn it on to avail the Service, hadri technologie cannot accept responsibility.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>With respect to hadri technologie&rsquo;s responsibility for your use of the app, when you&rsquo;re using the app, it&rsquo;s important to bear in mind that although we endeavour to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. hadri technologie accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>At some point, we may wish to update the app. The app is currently available on Android &ndash; the requirements for system(and for any additional systems we decide to extend the availability of the app to) may change, and you&rsquo;ll need to download the updates if you want to keep using the app. hadri technologie does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><strong><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#363636;'>Changes to This Terms and Conditions</span></strong></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>We may update our Terms and Conditions from time to time.&nbsp;</span><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Terms and Conditions on this page.</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>These terms and conditions are effective as of 2021-07-08</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><strong><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#363636;'>Contact Us</span></strong></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:.0001pt;margin-left:0cm;line-height:normal;font-size:15px;font-family:\"Calibri\",sans-serif;background:white;'><span style='font-size:16px;font-family:\"Segoe UI\",sans-serif;color:#4A4A4A;'>If you have any questions or suggestions about our Terms and Conditions, do not hesitate to contact us at hadritechnologie@gmail.com.&nbsp;</span></p>\n" +
                "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:10.0pt;margin-left:18.0pt;line-height:115%;font-size:15px;font-family:\"Calibri\",sans-serif;'><span dir=\"RTL\" style='font-family:\"Arial\",sans-serif;'>&nbsp;</span></p>";
        ((WebView) alertDialog.findViewById(R.id.webv)).loadDataWithBaseURL("", txt, mimeType, encoding, "");

        ((Button) alertDialog.findViewById(R.id.buttonOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

    @OnClick(R.id.b_inscrire)
    public void InscrireClick() {
//        CheckBox checkBox=(CheckBox) findViewById(R.id.checkBox);
//        if(!checkBox.isChecked()){
//            //checkBox.setError(R.string.uhavetoacceptterm);
//            Alert(R.string.uhavetoacceptterm);
//            return;
//        }
        if (tEmail.getText().toString().matches("") || tPwd.getText().toString().matches("")
                || tPwd2.getText().toString().matches("")) {
            Alert(R.string.message_fill_required_fields);
            return;
        } else if (!OptimTools.isEmailValid(tEmail.getText().toString())) {
            Alert(R.string.message_email_invalid);
            return;
        } else if (!tPwd.getText().toString().equals(tPwd2.getText().toString())) {
            Alert(R.string.message_confirm_password);
            return;
        } else if (tPwd.getText().length()<8) {
            Alert(R.string.pwd_too_short);
            return;
        } else {

            Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
            intent.putExtra("mobile", mobile);
            intent.putExtra("email", tEmail.getText().toString());
            intent.putExtra("password", tPwd.getText().toString());
            intent.putExtra("invite_code", tinvite.getText().toString());
            intent.putExtra("macAddr", getMacAddr());
            startActivity(intent);


        }

    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    private String getcode(String inv) {
        int b=0;
    if(!inv.isEmpty() && inv.matches("[0-9a-fA-F]+"))
         b= Integer.parseInt(inv,16);
      if(b==0)  return "";
      else   return b+"";
    }

    @OnClick(R.id.btn_fb)
    public void onFacebookClick() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    String mAccessToken = null;
    long mTokenExpired = 0;

    private String requestAccessToken(GoogleSignInAccount googleAccount) {


        if (mAccessToken != null && SystemClock.elapsedRealtime() < mTokenExpired)
            return mAccessToken;

        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            final URL url = new URL("https://www.googleapis.com/oauth2/v4/token");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            final StringBuilder b = new StringBuilder();
            b.append("code=").append(googleAccount.getServerAuthCode()).append('&')
                    .append("client_id=").append(getString(R.string.server_client_id)).append('&')
                    .append("client_secret=").append(getString(R.string.server_client_secret)).append('&')
                    .append("redirect_uri=").append("").append('&')
                    .append("grant_type=").append("authorization_code");

            final byte[] postData = b.toString().getBytes("UTF-8");

            os = conn.getOutputStream();
            os.write(postData);

            final int responseCode = conn.getResponseCode();
            if (200 <= responseCode && responseCode <= 299) {
                is = conn.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
            } else {
                Log.d("Error:", conn.getResponseMessage());
                return null;
            }

            b.setLength(0);
            String output;
            while ((output = br.readLine()) != null) {
                b.append(output);
            }

            final JSONObject jsonResponse = new JSONObject(b.toString());
            mAccessToken = jsonResponse.getString("access_token");
            mTokenExpired = SystemClock.elapsedRealtime() + jsonResponse.getLong("expires_in") * 1000;
            return mAccessToken;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Thread thread = new Thread(() -> {
                    try {
                        String token = requestAccessToken(account);

                        String first_name = account.getGivenName();
                        String last_name = account.getFamilyName();
                        String email = account.getEmail();
                        Customer customer = new Customer();
                        customer.setPrenom(last_name);
                        customer.setName(first_name);
                        customer.setEmail(email);
                        customer.setAccess_token(token);
                        GoogleSignIn(customer);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                thread.start();


                // Signed in successfully, show authenticated UI.
                //updateUI(account);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("optim_ERROR", "signInResult:failed code=" + e.getStatusCode());
                Alert("Error sign in with google");
                //updateUI(null);
            }
        } else {
            //If not request code is RC_SIGN_IN it must be facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.btn_google)
    public void onGoogle() {
        try {
            mGoogleSignInClient.signOut();
        } catch (Exception ex) {

        }

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void FacebookSignIn(Customer customer) {
        appApi.facebookLogin(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && !response.body().isError())
                    doLogin(response, RegisterActivity.this, appApi);
                else if (response.isSuccessful())
                    Alert(response.body().getMessage());
                else
                    Alert(response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(R.string.messages_erreur_connexion);
            }
        });
    }

    private void GoogleSignIn(Customer customer) {
        appApi.googleLogin(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && !response.body().isError())
                    doLogin(response, RegisterActivity.this, appApi);
                else if (response.isSuccessful())
                    Alert(response.body().getMessage());
                else
                    Alert(response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(R.string.messages_erreur_connexion);
            }
        });
    }
}
