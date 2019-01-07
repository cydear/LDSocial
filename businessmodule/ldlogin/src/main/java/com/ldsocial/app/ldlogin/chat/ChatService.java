package com.ldsocial.app.ldlogin.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * chat service
 *
 * @author lary.huang
 * @version v 1.4.8 2019/1/7 XLXZ Exp $
 * @email huangyang@xianglin.cn
 */
public class ChatService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return mService.getBinder();
    }

    private Messenger mService = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ChatConstants.MSG_FROM_CLIENT:
                    replyClient(msg);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private static void replyClient(Message msg) {
        String reply = "没有收到";
        Bundle msgClient = msg.getData();
        if (msgClient != null) {
            String client = msgClient.getString("ask");
            reply = client + " ? ? ? ? ?";
        }

        Messenger client = msg.replyTo;
        Message replyMessage = Message.obtain(null, ChatConstants.MSG_FROM_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putString("reply",reply);
        replyMessage.setData(bundle);

        try {
            client.send(replyMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
