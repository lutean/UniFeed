package com.prepod.unifeed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.prepod.unifeed.adapters.CustomDrawerAdapter;
import com.prepod.unifeed.models.DrawerItem;
import com.prepod.unifeed.fragments.FeedFragment;
import com.prepod.unifeed.views.RoundedImage;
import com.prepod.unifeed.UniFeed;
import com.vk.sdk.VKSdk;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    String[] mDrawerTitles;
    ActionBarDrawerToggle mDrawerToggle;
    CustomDrawerAdapter adapter;
    List<DrawerItem> dataList;
    ImageLoader imageLoader;
    TextView textTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String avaFb = getIntent().getStringExtra("avaFb");
        String avaVk = getIntent().getStringExtra("avaVk");

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        textTitle = (TextView) findViewById(R.id.textTitle);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerTitles = new String[]{"Logout"};

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_item, mDrawerTitles));

        dataList = new ArrayList<DrawerItem>();
        dataList.add(new DrawerItem("Feed", R.drawable.feed));
        dataList.add(new DrawerItem("LogOut", R.drawable.logout));

        adapter = new CustomDrawerAdapter(this, R.layout.drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        showFeed();
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case 1:
                        VKSdk.logout();
                        LoginManager.getInstance().logOut();
                        Intent dialogIntent = new Intent(MainActivity.this , AuthActivity.class);
                        startActivity(dialogIntent);
                        break;
                }
            }
        });

            imageLoader = UniFeed.getInstance().getImageLoader();

        LinearLayout drawerBtn = (LinearLayout) findViewById(R.id.drawer_btn);
        RoundedImage avatarFb = (RoundedImage) findViewById(R.id.drawer_pic);
        RoundedImage avatarVk = (RoundedImage) findViewById(R.id.drawer_pic2);

        avatarFb.setImageUrl(avaFb, imageLoader);
        avatarVk.setImageUrl(avaVk, imageLoader);

        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        showFeed();


    }

    private void showFeed(){
        textTitle.setText("Feed");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new FeedFragment())
                .commit();
    }


}
