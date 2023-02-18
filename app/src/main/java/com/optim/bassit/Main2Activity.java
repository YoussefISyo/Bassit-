package com.optim.bassit;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Tache;
import com.optim.bassit.ui.activities.ChatActivity;
import com.optim.bassit.ui.activities.SplashActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {
    @Inject
    AppApi appApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);
           App.getApp(getApplication()).getComponent().inject(this);
            if (getIntent().hasExtra("notification")) {
          /*  if(appApi==null)
            {
                Intent intent=new Intent(ChatActivity.this,SplashActivity.class);
                intent.putExtra("notification",getIntent().getStringExtra("notification"));
                startActivity(intent);
                finish();

            }*/

                appApi.getOneTaches(getIntent().getStringExtra("notification")).enqueue(new Callback<Tache>() {
                    @Override
                    public void onResponse(Call<Tache> call, Response<Tache> response) {

                        if (response.isSuccessful()) {
                            Intent intent=new Intent(Main2Activity.this, ChatActivity.class);
                            intent.putExtra("notification",getIntent().getStringExtra("notification"));
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent=new Intent(Main2Activity.this, SplashActivity.class);
                            intent.putExtra("notification",getIntent().getStringExtra("notification"));
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Tache> call, Throwable t) {
                        Intent intent=new Intent(Main2Activity.this, SplashActivity.class);
                        intent.putExtra("notification",getIntent().getStringExtra("notification"));
                        startActivity(intent);
                        finish();
                    }
                });

                return;

            }

       /*  Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
