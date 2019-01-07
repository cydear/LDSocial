package com.ldsocial.app.ldlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ldsocial.app.ldlogin.chat.ChatActivity;

/**
 * [类功能说明]
 *
 * @author lary.huang
 * @version v 1.4.8 2018/12/20 XLXZ Exp $
 * @email huangyang@xianglin.cn
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ChatActivity.class));
            }
        });
    }
}
