package com.prepod.unifeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipe;

    public FeedFragment() {
        super();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_feed, container, false);
        //getActivity().setTitle(getResources().getString(R.string.feed));
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        mSwipe = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_feed_swipe);
        mSwipe.setOnRefreshListener(this);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */

                        JSONObject array = response.getJSONObject();
                        Log.v("My", "" + array);
                    }
                }
        ).executeAsync();
/*
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/1073827109322453_1074005975971233",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
       
                        JSONObject array = response.getJSONObject();
                        Log.v("My", "" + array);
                    }
                }
        ).executeAsync();
*/
    }

    @Override
    public void onRefresh() {
        mSwipe.setRefreshing(true);
    }
}
