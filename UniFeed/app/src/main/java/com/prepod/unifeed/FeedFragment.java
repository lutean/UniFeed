package com.prepod.unifeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipe;
    private FeedListAdapter feedListAdapter;
    private ListView listView;

    private List<Feed> feedList = new ArrayList<>();
    private List<Person> personList = new ArrayList<>();
    private List<Person> personsListFb = new ArrayList<>();

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



        getWallFb();

        getWallVk();
    }

    private void getWallFb(){

        Bundle params = new Bundle();
        params.putString("fields", "link,message,picture,from,created_time");

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
                                JSONObject from = obj.getJSONObject("from");
                                Person person = new Person();
                                person.setFirstName(from.getString("name"));
                                person.setLastName("");
                                person.setId(from.getString("id"));
                                if (personsListFb.size() == 0){
                                    personsListFb.add(person);
                                    //getFbAva(person.getId());
                                }
                                if (!personsListFb.contains(person)) {
                                    personsListFb.add(person);
                                    // getFbAva(person.getId());
                                }


                                Feed feed = new Feed();
                                feed.setPerson(person);
                                feed.setId(obj.getString("id"));

                                try {
                                    feed.setLink(obj.getString("link"));
                                } catch (JSONException e){
                                    feed.setLink("");
                                    Log.e("My", "!!!! " + e);
                                }

                                try {
                                    feed.setCreatedTime(obj.getString("created_time"));
                                    feed.setTimeStamp(Util.getTimeStamp(feed.getCreatedTime()));
                                } catch (JSONException e){
                                    Log.e("My", "!!!! " + e);
                                }

                                try {
                                    feed.setStroy(obj.getString("story"));
                                } catch (JSONException e){
                                    feed.setStroy("");
                                    Log.e("My", "!!!! " + e);
                                }

                                try {
                                    feed.setCaption(obj.getString("caption"));
                                } catch (JSONException e){
                                    feed.setCaption("");
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
                                    feed.setMessage("");
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
                        getFbAva();


                        Log.v("My", "" + personsListFb);
                    }
                }
        ).executeAsync();


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

    private void storeAva(){

        for (int i=0; i < feedList.size(); i++){
            for (int j=0; j < personsListFb.size(); j++){
                if(feedList.get(i).getPerson().getId().equals(personsListFb.get(j).getId())){
                    feedList.get(i).getPerson().setAvaUrl(personsListFb.get(j).getAvaUrl());
                }
            }

        }
        Collections.sort(feedList);
        feedListAdapter.notifyDataSetChanged();
        mSwipe.setRefreshing(false);
    }

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
                            feed.setTimeStamp(Long.parseLong(item.getString("date")) * 1000);
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
                Collections.sort(feedList);
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

    public void getFbAva(){

        String ids = "";
        for (int i=0; i < personsListFb.size(); i++){
            ids = ids + personsListFb.get(i).getId();
            if (i != personsListFb.size() - 1)
                ids = ids + ",";
        }

        Bundle params = new Bundle();
        params.putString("ids", ids);
        params.putBoolean("redirect", false);
        params.putString("height", "200");
        params.putString("type", "normal");
        params.putString("width", "200");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {



                        JSONObject json = (JSONObject)response.getJSONObject();
                        Log.v("My", " " + json);

                        for (int i=0; i < personsListFb.size(); i++){
                            try {
                                JSONObject obj = json.getJSONObject(personsListFb.get(i).getId());
                                JSONObject data = obj.getJSONObject("data");
                                String url = data.getString("url");
                                personsListFb.get(i).setAvaUrl(url);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        storeAva();

                      /*  try {
                            JSONObject pic = json.getJSONObject("data");
                            String avaUrl = pic.optString("url");
                            for (int i=0; i < personsListFb.size();  i++){
                                if (personsListFb.get(i).getId().equals(id)){
                                    personsListFb.get(i).setAvaUrl(avaUrl);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/
                    }
                }
        ).executeAsync();
    }

    private void sortFeedList(){

    }

    @Override
    public void onRefresh() {
        mSwipe.setRefreshing(true);
        feedList.clear();
        feedListAdapter.notifyDataSetChanged();
        getWallFb();
        getWallVk();
    }
}
