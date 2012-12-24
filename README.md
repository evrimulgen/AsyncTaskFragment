AsyncTaskFragment
=======

AsyncTaskFragment is an Android library project designed to help you to perform background operations and publish results on the UI thread from your fragment without having to manipulate threads and/or handlers.

And most important, it keeps background operations alive when an activity needs to be restarted due to, for example, a configuration change.

For each Fragment, it uses a WorkerFragment as retained Fragment which holds a HandlerThread and two Handlers : one for handle Messages in background and the other one to handle Messages in UI thread.

Usage
-----

1. Make your Fragment class extends AsyncTaskFragment or AsyncTaskListFragment

        public static class MyFragment extends AsyncTaskFragment {
        	...
    	}

2. Implements the inherited abstract methods
	
		@Override
        public String getWorkerFragmentTag() {
            return "MyFragmentTag";
        }

        @Override
        public Object handleMessageInBackground(int what, Message msg) {
        	// You are in the background thread
            return null;
        }

        @Override
        public void onPostHandleMessage(int what, Message msg) {
        	// You are in the UI thread
        }

3. Send a Message from your Fragment to the WorkerHandlerThread with sendMessageToWorkerThread() methods.

4. Handle this Message in handleMessageInBackground().

5. Get the object you return in handleMessageInBackground in obj Message field in onPostHandleMessage. (The what Message field is the same as the one in handleMessageInBackground)

Sample
------

There is a sample with the library. It shows two Fragments side by side which do background operations and during them, they send Messages to UI thread to update Views.
Try to change your device orientation during background operations.

Example
-------

		public static class MyFragment extends AsyncTaskListFragment {

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

Developed by
------------

Guillaume BOUERAT

- [Google+](https://plus.google.com/u/0/112136052387869387989)
- [Twitter](https://twitter.com/GBouerat)
- [Google play](https://play.google.com/store/apps/developer?id=Guillaume+BOUERAT)

License
-------

	Copyright (C) 2012 Guillaume BOUERAT (https://github.com/GBouerat/Crouton)
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	     http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
