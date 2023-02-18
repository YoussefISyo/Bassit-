package com.optim.bassit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.ui.fragments.MyProfileFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {

    @Inject
    AppApi appApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(this.getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Integer user_id = getIntent().getIntExtra("user_id", 0);
        MyProfileFragment fragment= MyProfileFragment.newInstance(user_id);
        Bundle args = new Bundle();
        args.putInt("user_id", user_id);
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    public void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();

    }

}
