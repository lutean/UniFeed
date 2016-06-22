package com.prepod.unifeed;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Feed> feedList;

    public ImageView srcFeed;
    ImageLoader imageLoader = UniFeed.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<Feed> feedLsit) {
        this.activity = activity;
        this.feedList = feedLsit;
    }

    @Override
    public int getCount() {
        return feedList.size();
    }

    @Override
    public Object getItem(int location) {
        return feedList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = UniFeed.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView hashtag = (TextView) convertView
                .findViewById(R.id.hashtag);
        TextView jid = (TextView) convertView
                .findViewById(R.id.jid);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);

        srcFeed = (ImageView) convertView
                .findViewById(R.id.sourceIcon);

        LinearLayout itemLL = (LinearLayout) convertView.findViewById(R.id.itemLL);




        Feed item = feedList.get(position);

        Person person = item.getPerson();
        /*
        String firstName = item.getPerson().getFirstName();
        String lastName = item.getPerson().getLastName();
        if (firstName == null){
            firstName = "";
        }
        if (lastName == null){
            lastName = "";
        }*/
        if (person != null)
            name.setText(item.getPerson().getFirstName() + " " + item.getPerson().getLastName());
/*
        List<String> tags = new ArrayList<String>();
        tags.add(item.getId());
        tags.add(String.valueOf(position));

        srcFeed.setTag(tags);

        if (item.getDelBtn() == "true"){
        srcFeed.setVisibility(View.VISIBLE);

            srcFeed.setImageResource(R.drawable.wall_del_g);



            srcFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = Integer.parseInt(((List<String>) srcFeed.getTag()).get(1).toString());

                    alert(((List<String>) srcFeed.getTag()).get(0).toString(), pos);
                }

            });


        }else
        {
            srcFeed.setVisibility(View.INVISIBLE);
        }
*/



/*
        srcFeed.setTag(item.getId());

        srcFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id =  (String) view.getTag();


                RequestQueue queue = Volley.newRequestQueue(this);  // this = context

                StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, Consts.CLIENT_ADD_URL,
                        new com.android.volley.Response.Listener<String>()
                        {

                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);


                            }
                        },
                        new com.android.volley.Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", "" + error);


                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("id", id);

                        return params;
                    }
                };
                queue.add(postRequest);




            }
        });*/

        if (position % 2 == 0){
            itemLL.setBackgroundColor(Color.parseColor("#EEFCFF"));
        }else{
            itemLL.setBackgroundColor(Color.parseColor("#ffffff"));
        }



        // Converting timestamp into x ago format
        //  CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
        //               item.getTimeStamp(),
        //              System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        try {
            //CharSequence timeAgo = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date(item.getTimeStamp() * 1000));
            //timestamp.setText(timeAgo);

            Long tada = item.getTimeStamp();
            Long shas = System.currentTimeMillis();
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    tada,
                    shas, DateUtils.MINUTE_IN_MILLIS);
            timestamp.setText(timeAgo);
        } catch(Exception e){}
        //hashtag.setText(" #"+item.getHashtag());

        //jid.setText(item.getJid());



        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getMessage()) && !item.getMessage().equals("")) {
            statusMsg.setText(item.getMessage());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        if (item.getLink() != null && !item.getLink().equals("")) {
            url.setText(Html.fromHtml("<a href=\"" + item.getLink() + "\">"
                    + item.getLink() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }

        // user profile pic

        if (person != null)
        profilePic.setImageUrl(item.getPerson().getAvaUrl(), imageLoader);

        if (item.getFeedSource() == Consts.SOURCE_FB){
            srcFeed.setImageResource(R.drawable.fb);
        } else {
            srcFeed.setImageResource(R.drawable.vk);
        }

        // Feed image
        if (item.getPicture() != null) {
            feedImageView.setImageUrl(item.getPicture(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }
/*
    public void alert(final String postId, final int pos) {
        AlertDialog.Builder ad;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("Подтверждение.");  // Р·Р°РіРѕР»РѕРІРѕРє
        ad.setMessage("Вы действительно хотите удалить эту запись?"); // СЃРѕРѕР±С‰РµРЅРёРµ
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                RequestQueue queue = Volley.newRequestQueue(activity);  // this = context

                StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, Consts.DEL_POST_URL,
                        new com.android.volley.Response.Listener<String>()
                        {

                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                feedItems.remove(pos);
                                notifyDataSetChanged();
                            }
                        },
                        new com.android.volley.Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", "" + error);


                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("id", postId);

                        return params;
                    }
                };
                queue.add(postRequest);
            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        ad.show();
    }
*/
}