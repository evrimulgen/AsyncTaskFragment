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

import android.os.Handler;
import android.os.Message;

public class CallbackHandler extends Handler {

    private final WorkerFragment mWorkerFragment;

    public CallbackHandler(WorkerFragment workerFragment) {
        mWorkerFragment = workerFragment;
    }

    @Override
    public void handleMessage(Message msg) {
        AsyncTaskInterface target = (AsyncTaskInterface) mWorkerFragment.getTargetFragment();
        target.onPostHandleMessage(msg.what, msg);
    }

}
