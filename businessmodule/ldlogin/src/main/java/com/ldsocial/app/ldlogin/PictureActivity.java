package com.ldsocial.app.ldlogin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ldsocial.app.ldlogin.adapter.UriAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.util.List;

/**
 * @ClassName PictureActivity
 * @Description: 描述
 * @Author: Lary.huang
 * @CreateDate: 2020/7/27 10:07 PM
 * @Version: 1.0
 */
public class PictureActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;

    private RecyclerView recyclerView;
    private UriAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new UriAdapter());

        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum();
            }
        });
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Matisse.from(PictureActivity.this)
                //选择文件类型:(视频和图片)
                //.choose(MimeType.ofImage(), false)
                .choose(MimeType.ofAll())
                //.choose(MimeType.ofVideo())
                //自定义选择类型
                //.choose(MimeType.of(MimeType.JPEG, MimeType.AVI))
                //有序选择图片
                .countable(true)
                //capture和captureStrategy 要连用，是否在选择图片中展示照相和适配安卓7.0 FileProvider
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.ldsocial.app.fileprovider", "test"))
                //最大选择图片的张数
                .maxSelectable(9)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //界面中缩略图的质量
                .thumbnailScale(0.85f)
                //图片加载引擎,Glide加载方式
                .imageEngine(new GlideEngine())
                .setOnSelectedListener(new OnSelectedListener() {
                    @Override
                    public void onSelected(@NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                        Log.e("onSelected", "onSelected: pathList=" + pathList);
                    }
                })
                //是否只显示选择的类型的缩略图，就不会把所有图片和视频都放在一起，而是需要什么展示什么
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(new OnCheckedListener() {
                    @Override
                    public void onCheck(boolean isChecked) {
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    }
                })
                .forResult(REQUEST_CODE_CHOOSE);
        mAdapter.setData(null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
        }
    }
}
