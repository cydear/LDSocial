package com.ldsocial.app.ldlogin.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ldsocial.app.ldlogin.R;

/**
 * 聊天室
 *
 * @author lary.huang
 * @version v 1.4.8 2019/1/7 XLXZ Exp $
 * @email huangyang@xianglin.cn
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvResult;
    private EditText mEtMsg;
    private Button mBtnSend;

    private Messenger mService;

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
            Log.d("===>", "===> service 已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_chat);
        mTvResult = findViewById(R.id.tv_chat);
        mEtMsg = findViewById(R.id.et_msg);
        mBtnSend = findViewById(R.id.btn_send);

        mBtnSend.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        //问
        String ask = "问：" + mEtMsg.getText().toString();
        String result = mTvResult.getText().toString();
        result += "\n" + ask;
        mTvResult.setText(result);

        //发送消息
        Message msgClient = Message.obtain(null, ChatConstants.MSG_FROM_CLIENT);
        msgClient.replyTo = mClientReply;

        Bundle bundle = new Bundle();
        bundle.putString("ask", mEtMsg.getText().toString());

        msgClient.setData(bundle);

        try {
            mService.send(msgClient);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Messenger mClientReply = new Messenger(new MessengerHandler());

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ChatConstants.MSG_FROM_SERVICE:
                    showChat(msg);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void showChat(Message msg) {
        String result = mTvResult.getText().toString();
        Bundle reply = msg.getData();
        result += "\n" + "答：" + reply.getString("reply");
        mTvResult.setText(result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
    }
}
