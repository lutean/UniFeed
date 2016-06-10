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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipe;
    private List<Feed> feedList = new ArrayList<>();

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

                        JSONObject array = response.getJSONObject();
                        Log.v("My", "" + array);

                        try {
                            JSONArray data = array.getJSONArray("data");
                            for (int i=0; i<data.length(); i++){
                                JSONObject obj = data.getJSONObject(i);
                                Feed feed = new Feed();
                                feed.setId(obj.getString("id"));
                                feed.setCreatedTime(obj.getString("created_time"));
                                feed.setStroy("");
                                try {
                                    feed.setStroy(obj.getString("story"));
                                } catch (JSONException e){
                                    Log.e("My", "!!!! " + e);
                                }

                                feedList.add(feed);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
