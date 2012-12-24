/*
 * Copyright (C) 2012 Guillaume BOUERAT (https://github.com/GBouerat/AsyncTaskFragment)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.neogb.asynctaskfragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public abstract class AsyncTaskFragment extends Fragment implements AsyncTaskInterface {

    private WorkerFragment mWorkerFragment;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        mWorkerFragment = (WorkerFragment) fragmentManager
                .findFragmentByTag(WorkerFragment.TAG_PREFIX + getWorkerFragmentTag());
        if (mWorkerFragment == null) {
            mWorkerFragment = WorkerFragment.newInstance(AsyncTaskFragment.this);
            fragmentManager.beginTransaction()
                    .add(mWorkerFragment, WorkerFragment.TAG_PREFIX + getWorkerFragmentTag())
                    .commit();
        }
    }

    @Override
    public abstract String getWorkerFragmentTag();

    @Override
    public abstract Object handleMessageInBackground(int what, Message msg);

    @Override
    public abstract void onPostHandleMessage(int what, Message msg);

    @Override
    public void sendEmptyMessageToWorkerThread(int what) {
        MessageUtils.sendEmptyMessage(mWorkerFragment.getWorkerHandler(), what);
    }

    @Override
    public void sendMessageToWorkerThread(int what, Object obj) {
        MessageUtils.sendMessage(mWorkerFragment.getWorkerHandler(), what, obj);
    }

    @Override
    public void sendMessageToWorkerThread(int what, int arg1, int arg2) {
        sendMessageToWorkerThread(what, arg1, arg2, null);
    }

    @Override
    public void sendMessageToWorkerThread(int what, int arg1, int arg2, Object obj) {
        MessageUtils.sendMessage(mWorkerFragment.getWorkerHandler(), what, arg1, arg2, obj);
    }

    @Override
    public void sendMessageToWorkerThread(Message msg) {
        MessageUtils.sendMessage(mWorkerFragment.getWorkerHandler(), msg);
    }

    @Override
    public void sendEmptyMessageToUiThread(int what) {
        MessageUtils.sendEmptyMessage(mWorkerFragment.getCallbackHandler(), what);
    }

    @Override
    public void sendMessageToUiThread(int what, Object obj) {
        MessageUtils.sendMessage(mWorkerFragment.getCallbackHandler(), what, obj);
    }

    @Override
    public void sendMessageToUiThread(int what, int arg1, int arg2) {
        sendMessageToUiThread(what, arg1, arg2, null);
    }

    @Override
    public void sendMessageToUiThread(int what, int arg1, int arg2, Object obj) {
        MessageUtils.sendMessage(mWorkerFragment.getCallbackHandler(), what, arg1, arg2, obj);
    }

    @Override
    public void sendMessageToUiThread(Message msg) {
        MessageUtils.sendMessage(mWorkerFragment.getCallbackHandler(), msg);
    }

}
