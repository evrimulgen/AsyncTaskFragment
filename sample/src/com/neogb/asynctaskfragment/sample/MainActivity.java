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

package com.neogb.asynctaskfragment.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neogb.asynctaskfragment.AsyncTaskFragment;
import com.neogb.asynctaskfragment.AsyncTaskListFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager.enableDebugLogging(BuildConfig.DEBUG);
        enableStrictMode();

        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment1, new Fragment1())
                    .add(R.id.fragment2, new Fragment2())
                    .commit();
        }
    }

    private void enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
        }
    }

    public static class Fragment1 extends AsyncTaskListFragment {

        private static final String WORKER_TAG = "fragment1";

        private static final String KEY_STATE_DATA = "state_data";

        private static final int MESSAGE_LOAD = 0x0000;

        private static final int MESSAGE_ADD_LINE = 0x0001;

        private static final int MAX_LINES = 20;

        private StringAdapter mStringAdapter;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment1, container, false);
            view.findViewById(android.R.id.empty).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    sendEmptyMessageToWorkerThread(MESSAGE_LOAD);
                }
            });
            if (savedInstanceState == null) {
                mStringAdapter = new StringAdapter(getActivity());
            }
            else {
                mStringAdapter = new StringAdapter(getActivity(),
                        savedInstanceState.getStringArrayList(KEY_STATE_DATA));
            }
            ((ListView) view.findViewById(android.R.id.list)).setAdapter(mStringAdapter);
            return view;
        };

        @Override
        public String getWorkerFragmentTag() {
            return WORKER_TAG;
        }

        @Override
        public Object handleMessageInBackground(int what, Message msg) {
            switch (what) {
                case MESSAGE_LOAD:
                    SystemClock.sleep(250);
                    for (int i = 1; i < MAX_LINES; i++) {
                        sendMessageToUiThread(MESSAGE_ADD_LINE, i, 0);
                        SystemClock.sleep(250);
                    }
                    return MAX_LINES;
                default:
                    return null;
            }
        }

        @Override
        public void onPostHandleMessage(int what, Message msg) {
            switch (what) {
                case MESSAGE_LOAD:
                    mStringAdapter.add("Line : " + msg.obj);
                    break;
                case MESSAGE_ADD_LINE:
                    mStringAdapter.add("Line : " + msg.arg1);
                    break;
                default:
                    break;
            }
        }
        
        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putStringArrayList(KEY_STATE_DATA, mStringAdapter.getData());
        }
        
        private static class StringAdapter extends BaseAdapter {

            private final LayoutInflater mLayoutInflater;

            private final ArrayList<String> mData;

            public StringAdapter(Context context) {
                this(context, new ArrayList<String>());
            }

            public StringAdapter(Context context, ArrayList<String> data) {
                mLayoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mData = data;
            }

            public void add(String s) {
                mData.add(s);
                notifyDataSetChanged();
            }

            public ArrayList<String> getData() {
                return mData;
            }

            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Object getItem(int position) {
                return mData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1,
                            parent, false);
                }
                ((TextView) convertView).setText(mData.get(position));
                return convertView;
            }

        }

    }

    public static class Fragment2 extends AsyncTaskFragment {

        private static final String WORKER_TAG = "fragment2";

        private static final String STATE_BUTTON_ENABLE = "state_button_enable";

        private static final int MESSAGE_LOAD = 0x0000;

        private static final int MESSAGE_PROGRESS = 0x0001;

        private static final int MESSAGE_ENABLE = 0x0002;

        private static final int MAX_PROGRESS = 1000;

        private View mButton;
        private ProgressBar mProgressBar;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment2, container, false);

            mButton = view.findViewById(R.id.button);
            mButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mProgressBar.setProgress(0);
                    sendEmptyMessageToWorkerThread(MESSAGE_LOAD);
                }
            });

            mProgressBar = (ProgressBar) view.findViewById(R.id.progress);

            if (savedInstanceState != null) {
                mButton.setEnabled(savedInstanceState.getBoolean(STATE_BUTTON_ENABLE));
            }

            return view;
        }

        @Override
        public String getWorkerFragmentTag() {
            return WORKER_TAG;
        }

        @Override
        public Object handleMessageInBackground(int what, Message msg) {
            if (what == MESSAGE_LOAD) {
                sendMessageToUiThread(MESSAGE_ENABLE, 0, 0);
                SystemClock.sleep(5);
                for (int i = 1; i < MAX_PROGRESS; i++) {
                    sendMessageToUiThread(MESSAGE_PROGRESS, i, 0);
                    SystemClock.sleep(5);
                }
                return MAX_PROGRESS;
            }
            return null;
        }

        @Override
        public void onPostHandleMessage(int what, Message msg) {
            if (what == MESSAGE_LOAD) {
                mProgressBar.setProgress((Integer) msg.obj);
                mButton.setEnabled(true);
            }
            else if (what == MESSAGE_PROGRESS) {
                mProgressBar.setProgress(msg.arg1);
            }
            else if (what == MESSAGE_ENABLE) {
                mButton.setEnabled(msg.arg1 == 1);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putBoolean(STATE_BUTTON_ENABLE, mButton.isEnabled());
        }

    }

}
