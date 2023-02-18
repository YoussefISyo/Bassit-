package com.optim.bassit.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optim.bassit.R;

import java.util.Locale;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class PhoneActivity extends AppCompatActivity {


    private TextView txtErrorPhone;
    private MaskedEditText edtMaskedEditText;
    private RelativeLayout btnEnvoyer;
    private LinearLayout layoutPhoneContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        init();
    }

    private void init() {
        edtMaskedEditText = findViewById(R.id.edtMasked);
        btnEnvoyer = findViewById(R.id.btnEnvoyer);
        txtErrorPhone = findViewById(R.id.txtErrorPhone);
        layoutPhoneContainer = findViewById(R.id.layout_phone_container);

        edtMaskedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edtMaskedEditText.getRawText().isEmpty()){
                    txtErrorPhone.setVisibility(View.GONE);
                    layoutPhoneContainer.setBackgroundResource(R.drawable.bg_edt_focused);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = edtMaskedEditText.getRawText();
                if (mobile.isEmpty() || mobile.length() < 9){
                    txtErrorPhone.setVisibility(View.VISIBLE);
                    layoutPhoneContainer.setBackgroundResource(R.drawable.bg_edt_error);
                    edtMaskedEditText.requestFocus();
                    return;
                }
                Intent intent = new Intent(PhoneActivity.this, CodeActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);

            }
        });


    }
}