package com.ishraq.janna.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.ishraq.janna.R;
import com.ishraq.janna.fragment.AboutUs;
import com.ishraq.janna.fragment.Events;
import com.ishraq.janna.fragment.Reservation;
import com.ishraq.janna.service.CommonService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressDialog pDialog;
    AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonService commonService = new CommonService(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        addUserAync("asdrffefdsr", "123", "df", "asd", "ff");
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("عن مستشفى جنّة");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ali_tab1, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("الأحداث");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.naseema_tab1, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("الحجوزات");
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_info_, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AboutUs());
        adapter.addFrag(new Events());
        adapter.addFrag(new Reservation());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

    public void addUserAync(final String userName, final String userPass, final String userType, final String mobile, final String email) {
        mRegisterTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("جاري تنفيذ طلبك");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
//                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // Register on our server
                // On server creates a new user
                addUser(userName, userPass, userType, mobile, email);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
//                pDialog.dismiss();

                Toast.makeText(MainActivity.this, "تمت الاضافة", Toast.LENGTH_LONG).show();
                mRegisterTask = null;
            }
        };
        mRegisterTask.execute(null, null, null);
    }

    void addUser(String userName, String userPass, String userType, String mobile, String email) {
//        shaheed.esy.es/create_subject_from_user.php?title=test&content=test&image_url=test
        String randomUserURL = "http://ganah.zagel1.com/pages/JsonData/JsonInsertUsers.aspx";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("userPass", userPass));
        params.add(new BasicNameValuePair("userType", userType));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("email", email));

        JSONObject json = CommonService.makeHttpRequest(randomUserURL, "POST", params);
        Log.e("hazemjson", json + "");
        if (json != null) {
            try {
                JSONArray jsonArray = json
                        .getJSONArray("");

//                for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject c = jsonArray.getJSONObject(0);
//
//                    FeedItem item = new FeedItem();
//                    item.setTitle(c.getString("title"));
//                    item.setContent(c.getString("content"));
//                    item.setThumbnail(c.getString("image_url"));
//                    feedsList.add(item);
//            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
