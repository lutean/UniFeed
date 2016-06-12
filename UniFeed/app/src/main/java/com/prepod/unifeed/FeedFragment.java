package com.prepod.unifeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private FeedListAdapter feedListAdapter;
    private ListView listView;

    private List<Feed> feedList = new ArrayList<>();
    private List<Person> personList = new ArrayList<>();

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

        listView = (ListView)getView().findViewById(R.id.fragment_feed_body);
        listView.setDivider(null);
        feedListAdapter = new FeedListAdapter(getActivity(), feedList);

        listView.setAdapter(feedListAdapter);


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

                                //feedList.add(feed);
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

                        JSONArray profiles = (JSONArray) responseObj.get("profiles");
                        for (int i=0; i < profiles.length(); i++){
                            JSONObject profile = (JSONObject) profiles.get(i);
                            Person person = new Person();
                            person.setFirstName(profile.getString("first_name"));
                            person.setLastName(profile.getString("last_name"));
                            person.setAvaUrl(profile.getString("photo_100"));
                            person.setId(profile.getString("id"));
                            personList.add(person);
                        }


                        JSONArray items = (JSONArray)responseObj.get("items");
                        for (int i=0; i < items.length(); i++){
                            JSONObject item = (JSONObject)items.get(i);
                            Feed feed = new Feed();
                            if (item.getString("text") != null)
                                feed.setMessage(item.getString("text"));
                            feed.setTimeStamp(Long.parseLong(item.getString("date")));
                            feed.setFromId(item.getString("from_id"));

                            for (Person person : personList) {
                                if (feed.getFromId().equals(person.getId())){
                                    feed.setPerson(person);
                                }
                            }

                            if(item.has("copy_history")){

                                feed.setLink("http://vk.com/wall" + item.getString("from_id") + "_" + item.getString("id"));
                            }

                            if (item.has("attachments")) {
                                JSONArray attachments = (JSONArray) item.get("attachments");
                                for (int j = 0; j < attachments.length(); j++) {
                                    JSONObject attachment = (JSONObject) attachments.get(j);
                                    if (attachment.getString("type") != null)
                                        switch (attachment.getString("type")) {
                                            case "photo":
                                                JSONObject photo = attachment.getJSONObject("photo");
                                                Attachment photoAttachment = new Attachment();
                                                photoAttachment.setPhoto(photo.getString("photo_604"));
                                                photoAttachment.setText(photo.getString("text"));
                                                feed.setPicture(photoAttachment.getPhoto());
                                                if (!feed.getMessage().equals("")) {
                                                    feed.setMessage(feed.getMessage() + "\n" + photoAttachment.getText());
                                                } else {
                                                    feed.setMessage(photoAttachment.getText());
                                                }
                                                break;
                                            case "doc":
                                                JSONObject doc = attachment.getJSONObject("doc");
                                                Attachment docAttachment = new Attachment();
                                                docAttachment.setPhoto(doc.getString("photo_130"));
                                                docAttachment.setUrl(doc.getString("url"));
                                                feed.setPicture(docAttachment.getPhoto());
                                                feed.setLink(docAttachment.getUrl());
                                                break;
                                            case "video":
                                                JSONObject video = attachment.getJSONObject("video");
                                                Attachment videoAttachment = new Attachment();
                                                videoAttachment.setPhoto(video.getString("photo_130"));
                                                //videoAttachment.setUrl(video.getString("url"));
                                                videoAttachment.setText(video.getString("title"));
                                                feed.setPicture(videoAttachment.getPhoto());
                                                if (!feed.getMessage().equals("")) {
                                                    feed.setMessage(feed.getMessage() + "\n" + videoAttachment.getText());
                                                } else {
                                                    feed.setMessage(videoAttachment.getText());
                                                }
                                                break;
                                            case "link":
                                                JSONObject link = attachment.getJSONObject("link");
                                                Attachment linkAttachment = new Attachment();
                                                if (link.has("image_src"))
                                                    linkAttachment.setPhoto(link.getString("image_src"));
                                                linkAttachment.setUrl(link.getString("url"));
                                                linkAttachment.setTitle(link.getString("title"));
                                                linkAttachment.setDescription(link.getString("description"));
                                                feed.setLink(linkAttachment.getUrl());
                                                feed.setPicture(linkAttachment.getPhoto());
                                                if (!feed.getMessage().equals("")) {
                                                    feed.setMessage(feed.getMessage()
                                                            + "\n"
                                                            + linkAttachment.getTitle()
                                                            + "\n"
                                                            + linkAttachment.getDescription());
                                                } else {
                                                    feed.setMessage(linkAttachment.getTitle()
                                                            + "\n"
                                                            + linkAttachment.getDescription());
                                                }
                                                break;

                                        }
                                }
                            }

                            feedList.add(feed);

                            //Log.v("My", " " + attachments);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("My", " " + responseObj);
                }

                feedListAdapter.notifyDataSetChanged();
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
