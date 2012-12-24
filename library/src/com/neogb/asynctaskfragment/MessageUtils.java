
package com.neogb.asynctaskfragment;

import android.os.Handler;
import android.os.Message;

class MessageUtils {

    static void sendEmptyMessage(Handler handler, int what) {
        Message msg = Message.obtain(handler);
        msg.what = what;
        msg.sendToTarget();
    }

    static void sendMessage(Handler handler, int what, Object obj) {
        Message msg = Message.obtain(handler);
        msg.what = what;
        msg.obj = obj;
        msg.sendToTarget();
    }

    static void sendMessage(Handler handler, int what, int arg1, int arg2, Object obj) {
        Message msg = Message.obtain(handler);
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        msg.sendToTarget();
    }

    static void sendMessage(Handler handler, Message msg) {
        handler.sendMessage(msg);
    }

}
