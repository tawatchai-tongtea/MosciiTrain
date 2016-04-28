package com.tawatchai.mosciitrain.Fragement;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tawatchai.mosciitrain.Activity.MainActivity;
import com.tawatchai.mosciitrain.Adapter.RecyclerAdapter;
import com.tawatchai.mosciitrain.Class.MotorBike;
import com.tawatchai.mosciitrain.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment extends Fragment {


    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<MotorBike> motorBikes = new ArrayList<>();
    RecyclerAdapter adapter;
    CoordinatorLayout coordinatorLayout;

    public PagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coorDinator);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity()); //new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(getActivity(),motorBikes);

        recyclerView.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connectServer();
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                connectServer();
            }
        });


        return view;
    }

    private void connectServer(){

//        RequestParams params = new RequestParams();
//        params.add("Key","Value");

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get("http://beta.json-generator.com/api/json/get/4y8va_deb",null, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();

                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Moscii","Response "+new String(responseBody));

                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));

                    motorBikes.clear();

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        MotorBike motorBike = new MotorBike();
                        motorBike.title = jsonObject.getString("title");
                        motorBike.desc = jsonObject.getString("desc");
                        motorBike.image = jsonObject.getString("image");

                        motorBikes.add(motorBike);

                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(((MainActivity)getActivity()).getViewPager(),e.getLocalizedMessage(),Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Snackbar.make(((MainActivity)getActivity()).getViewPager(),error.getLocalizedMessage(),Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
