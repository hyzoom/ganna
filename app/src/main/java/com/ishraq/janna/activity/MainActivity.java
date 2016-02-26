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
import com.ishraq.janna.service.ServiceHandler;

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
        ServiceHandler sh = new ServiceHandler();
        String logInURL = "http://ganah.zagel1.com/Pages/JsonData/JsonInsertUsers.aspx?userName=oojhhj&userPass=112233";
        // Making a request to url and getting response
        // //Ash101932/Ash2744740
        String jsonStr = sh.makeServiceCall(logInURL, ServiceHandler.GET);

        Log.e("Response: ", "> " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // System.out.println("jsonObj " + jsonObj);
                // looping through All events

//                memId = jsonObj.getString(TAG_MEMBER_ID);
//                nameEng = jsonObj.getString(TAG_NAME_ENG);
//                nameAr = jsonObj.getString(TAG_NAME_AR);
//                religion = jsonObj.getString(TAG_RELIGION);
//                birthDate = jsonObj.getString(TAG_BIRTHDATE);
//                joinEBA = jsonObj.getString(TAG_JOIN_EBA);
//                path = jsonObj.getString(TAG_PATH);
//                mr_mrs_ar = jsonObj.getString(TAG_MR_MRS_AR);
//                mr_mrs_eng = jsonObj.getString(TAG_MR_MRS_ENG);

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Exception");
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
    }


}
