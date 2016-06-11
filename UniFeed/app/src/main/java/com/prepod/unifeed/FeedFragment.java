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
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


        Bundle params = new Bundle();
        params.putString("fields", "link,message,picture");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
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
                                feed.setCaption("");
                                feed.setDescription("");
                                feed.setMessage("");
                                feed.setPicture("");
                                feed.setTimeStamp(Util.getTimeStamp(feed.getCreatedTime()));

                                try {
                                    feed.setStroy(obj.getString("story"));
                                } catch (JSONException e){
                                    Log.e("My", "!!!! " + e);
                                }

                                try {
                                    feed.setCaption(obj.getString("caption"));
                                } catch (JSONException e){
                                    Log.e("My", "!!!! " + e);
                                }

                                try {
                                    feed.setDescription(obj.getString("description"));
                                } catch (JSONException e){
                                    Log.e("My", "!!!! " + e);
                                }

                                try {
                                    feed.setMessage(obj.getString("message"));
                                } catch (JSONException e){
                                    Log.e("My", "!!!! " + e);
                                }

                                try {
                                    feed.setPicture(obj.getString("picture"));
                                } catch (JSONException e){
                                    Log.e("My", "!!!! " + e);
                                }

                                feedList.add(feed);
                               // getPost(feed);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

        getWallVk();
    }

/*
    private void getPost(Feed feed) {

        String id = feed.getId();

        Bundle params = new Bundle();
        params.putString("fields", "link,message,caption");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.v("My", " " + response);
                    }
                }
        ).executeAsync();
    }
*/

    private void getFeedVk(){
        VKRequest request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS, "post"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                JSONObject object = response.json;
                if (object.has("response")) {
                    JSONObject responseObj = object.optJSONObject("response");
                    Log.v("My", " " + responseObj);
                }



//Do complete stuff
            }
            @Override
            public void onError(VKError error) {
                Log.v("My", " " + error);
//Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.v("My", " ");
//I don't really believe in progress
            }
        });
    }

    private void getWallVk(){
        VKRequest request = VKApi.wall().get(VKParameters.from(VKApiConst.COUNT, 50, VKApiConst.EXTENDED, 1));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                JSONObject object = response.json;
                if (object.has("response")) {
                    JSONObject responseObj = object.optJSONObject("response");
                    try {
                        JSONArray items = (JSONArray)responseObj.get("items");
                        for (int i=0; i < items.length(); i++){
                            JSONObject item = (JSONObject)items.get(i);
                            Feed feed = new Feed();
                            if (item.getString("text") != null)
                                feed.setMessage(item.getString("text"));
                            feed.setTimeStamp(Long.parseLong(item.getString("date")));
                            feed.setOwnerId(item.getString("owner_id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("My", " " + responseObj);
                }



//Do complete stuff
            }
            @Override
            public void onError(VKError error) {
                Log.v("My", " " + error);
//Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.v("My", " ");
//I don't really believe in progress
            }
        });
    }


    @Override
    public void onRefresh() {
        mSwipe.setRefreshing(true);
    }
}
