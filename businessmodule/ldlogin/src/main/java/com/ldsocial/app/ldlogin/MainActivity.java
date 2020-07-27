package com.ldsocial.app.ldlogin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldsocial.app.ldlogin.utils.DisplayUtil;
import com.ldsocial.app.ldlogin.widget.CustomScrollView;

/**
 * @ClassName MainActivity
 * @Description: 描述
 * @Author: Lary.huang
 * @CreateDate: 2020/7/26 1:59 PM
 * @Version: 1.0
 */
public class MainActivity extends AppCompatActivity implements CustomScrollView.OnScrollChangedListener {
    private CustomScrollView scrollView;
    private LinearLayout llTitleBar;
    private TextView tvTitle;
    private ImageView ivNick;

    private int mHeight = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_main);
        scrollView = findViewById(R.id.scrollView);
        llTitleBar = findViewById(R.id.ll_title_bar);
        tvTitle = findViewById(R.id.tv_title);
        ivNick = findViewById(R.id.iv_nick);

        scrollView.setOnScrollChangedListener(this);

        mHeight = DisplayUtil.dp2px(this, 375);
    }

    @Override
    public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        float height = mHeight / 2;
        if (scrollY <= height) {
            float scale = scrollY / height;
            float alpha = scale * 255;
            llTitleBar.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            if (scrollY >= height - 10) {
                tvTitle.setTextColor(Color.parseColor("#333333"));
            } else {
                tvTitle.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            llTitleBar.setBackgroundColor(Color.argb(255, 255, 255, 255));
            tvTitle.setTextColor(Color.parseColor("#333333"));
        }
    }
}
