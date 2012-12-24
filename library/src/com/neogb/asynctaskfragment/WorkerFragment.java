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

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;

public class WorkerFragment extends Fragment {

    static String TAG_PREFIX = "worker_fragment_";

    private final WorkerHandlerThread mWorkerHandlerThread;

    private final WorkerHandler mWorkerHandler;

    private final CallbackHandler mCallbackHandler;

    public static WorkerFragment newInstance(AsyncTaskInterface asyncTaskInterface) {
        WorkerFragment workerFragment = new WorkerFragment();
        workerFragment.setTargetFragment((Fragment) asyncTaskInterface, 0);
        return workerFragment;
    }

    public WorkerFragment() {
        mCallbackHandler = new CallbackHandler(WorkerFragment.this);
        mWorkerHandlerThread = new WorkerHandlerThread();
        mWorkerHandlerThread.start();
        mWorkerHandler = new WorkerHandler(
                WorkerFragment.this,
                mWorkerHandlerThread.getLooper()
                );
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        synchronized (mWorkerHandler) {
            mWorkerHandler.setReady(true);
            mWorkerHandler.notify();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onDestroy() {
        synchronized (mWorkerHandler) {
            mWorkerHandler.quit();
            mWorkerHandler.setReady(false);
            mWorkerHandler.notify();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                mWorkerHandlerThread.quit();
            }
            else {
                Looper looper = mWorkerHandlerThread.getLooper();
                if (looper != null) {
                    looper.quit();
                }
            }
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        synchronized (mWorkerHandler) {
            mWorkerHandler.setReady(false);
            mWorkerHandler.notify();
        }
        super.onDetach();
    }

    WorkerHandler getWorkerHandler() {
        return mWorkerHandler;
    }

    CallbackHandler getCallbackHandler() {
        return mCallbackHandler;
    }

}
