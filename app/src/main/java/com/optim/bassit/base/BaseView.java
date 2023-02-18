package com.optim.bassit.base;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public interface BaseView {

    void initDependencies();

    Context getViewContext();

    void showMessage(String message);

    interface BaseActivity extends BaseView {
        void initViews();

        void launchActivity(Intent intent);

        void destroyView();
    }

    interface BaseFragment extends BaseView {
        void initViews(View view);
    }
}
