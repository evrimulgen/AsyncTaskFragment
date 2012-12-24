
package com.neogb.asynctaskfragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public interface AsyncTaskInterface {

    /**
     * Define a tag for {@link WorkerFragment} to retrieve it with
     * {@link FragmentManager#findFragmentByTag(String) findFragmentByTag()}
     * method.
     * 
     * @return a tag, it will be prefix by {@link WorkerFragment} default tag.
     */
    public abstract String getWorkerFragmentTag();

    /**
     * Handle {@link Message} send to {@link WorkerHandler}.
     * 
     * @param what the message code to handle
     * @param msg the message to handle
     * @return The return object will be pass to
     *         {@link #onPostHandleMessage(int, Message)} in Ui Thread as
     *         {@link Message#obj Message.obj} field.
     */
    public abstract Object handleMessageInBackground(int what, Message msg);

    /**
     * This method will be call after
     * {@link #handleMessageInBackground(int, Message)} or if you send a
     * {@link Message} directly to Ui Thread with
     * {@link #sendMessageToUiThread(Message)},
     * {@link #sendMessageToUiThread(int, Object)},
     * {@link #sendMessageToUiThread(int, int, int)} or
     * {@link #sendMessageToUiThread(int, int, int, Object)} from
     * {@link #handleMessageInBackground(int, Message)}.
     * 
     * @param what the message code to handle
     * @param msg the message to handle
     */
    public abstract void onPostHandleMessage(int what, Message msg);

    /**
     * This method is a convenient way to send {@link Message} from your
     * {@link Fragment} to {@link WorkerHandlerThread}.
     * 
     * @see Message#obtain(Handler, int)
     */
    public void sendEmptyMessageToWorkerThread(int what);

    /**
     * This method is a convenient way to send {@link Message} from your
     * {@link Fragment} to {@link WorkerHandlerThread}.
     * 
     * @see Message#obtain(Handler, int, Object)
     */
    public void sendMessageToWorkerThread(int what, Object obj);

    /**
     * This method is a convenient way to send {@link Message} from your
     * {@link Fragment} to {@link WorkerHandlerThread}.
     * 
     * @see Message#obtain(Handler, int, int, int)
     */
    public void sendMessageToWorkerThread(int what, int arg1, int arg2);

    /**
     * This method is a convenient way to send {@link Message} from your
     * {@link Fragment} to {@link WorkerHandlerThread}.
     * 
     * @see Message#obtain(Handler, int, int, int, Object)
     */
    public void sendMessageToWorkerThread(int what, int arg1, int arg2, Object obj);

    /**
     * This method is a convenient way to send {@link Message} from your
     * {@link Fragment} to {@link WorkerHandlerThread}.
     * 
     * @see Message#obtain(Message)
     */
    public void sendMessageToWorkerThread(Message msg);

    /**
     * This method is a convenient way to send {@link Message} from
     * {@link #handleMessageInBackground(int, Message)} to Ui Thread.
     * 
     * @see Message#obtain(Handler, int)
     */
    public void sendEmptyMessageToUiThread(int what);

    /**
     * This method is a convenient way to send {@link Message} from
     * {@link #handleMessageInBackground(int, Message)} to Ui Thread.
     * 
     * @see Message#obtain(Handler, int, Object)
     */
    public void sendMessageToUiThread(int what, Object obj);

    /**
     * This method is a convenient way to send {@link Message} from
     * {@link #handleMessageInBackground(int, Message)} to Ui Thread.
     * 
     * @see Message#obtain(Handler, int, int, int)
     */
    public void sendMessageToUiThread(int what, int arg1, int arg2);

    /**
     * This method is a convenient way to send {@link Message} from
     * {@link #handleMessageInBackground(int, Message)} to Ui Thread.
     * 
     * @see Message#obtain(Handler, int, int, int, Object)
     */
    public void sendMessageToUiThread(int what, int arg1, int arg2, Object obj);

    /**
     * This method is a convenient way to send {@link Message} from
     * {@link #handleMessageInBackground(int, Message)} to Ui Thread.
     * 
     * @see Message#obtain(Message)
     */
    public void sendMessageToUiThread(Message msg);

}
