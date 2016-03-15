package com.ishraq.janna.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ishraq.janna.R;
import com.ishraq.janna.activity.MainActivity;
import com.ishraq.janna.model.User;
import com.ishraq.janna.service.SettingsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

//    private ImageView userProfileImageView;
    private TextView userNameTextView;
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    public MyActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private MyActionBarDrawerToggle drawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private SettingsService settingsService;
    private User loggedInUser;


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        settingsService = new SettingsService(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawerView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) drawerView.findViewById(R.id.navigation_drawer_list);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.recipe_list_background_color));
                view.findViewById(R.id.drawerItemName).setAlpha((float) 0.25);
                view.findViewById(R.id.drawerItemIcon).setAlpha((float) 0.25);
                selectItem(position);
            }
        });
        initializeItemsList();

        userNameTextView = (TextView) drawerView.findViewById(R.id.userNameTextView);

        return drawerView;
    }

    public void setmCurrentSelectedPosition(int position) {
        mCurrentSelectedPosition = position;
    }

    private void initializeItemsList() {
        mDrawerListView.setAdapter(new ArrayAdapter<Map<String, Integer>>(
                getActivity(),
                R.layout.row_navigation_drawer,
                getDrawerData()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_navigation_drawer, parent, false);
                }

                Map<String, Integer> rowData = getItem(position);

                ImageView icon = (ImageView) convertView.findViewById(R.id.drawerItemIcon);
                TextView text = (TextView) convertView.findViewById(R.id.drawerItemName);

                icon.setImageResource(rowData.get("icon"));
                text.setText(rowData.get("text"));

//                if (position == mCurrentSelectedPosition) {
//                    convertView.setBackgroundColor(getActivity().getResources().getColor(R.color.recipe_list_background_color));
//                    text.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//                    switch (position) {
//                        case 0:
//                            icon.setImageResource(R.drawable.ic_bookmark);
//                            break;
//
//                        case 1:
//                            icon.setImageResource(R.drawable.ic_exit);
//                            break;
//                    }
//                }


                return convertView;
            }
        });
    }

    private List<Map<String, Integer>> getDrawerData() {
        List<Map<String, Integer>> entries = new ArrayList<Map<String, Integer>>();
        entries.add(new HashMap<String, Integer>() {{
            put("icon", R.drawable.ic_bookmark);
            put("text", R.string.nav_booking_title);
        }});
        entries.add(new HashMap<String, Integer>() {{
            put("icon", R.drawable.ic_exit);
            put("text", R.string.nav_logout_title);
        }});
        return entries;
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow_rtl, GravityCompat.START);
        } else {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        }

        showGlobalContextActionBar();

        drawerToggle = new MyActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(drawerToggle);
    }

    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public void setItemSelected(int position) {
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallbacks = (NavigationDrawerCallbacks) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean drawerOpen = (mDrawerLayout != null);
        loggedInUser = settingsService.getSettings().getLoggedInUser();
        if (drawerOpen){
            if (settingsService.getSettings().getLoggedInUser() != null ){
                String l;
            }
            if (loggedInUser != null){
//                if (loggedInUser.getAvatar() != null) {
//                    if (loggedInUser.getAvatar().getUrl() != null && !loggedInUser.getAvatar().getUrl().equals("")) {
//                        settingsService.displayImage(loggedInUser.getAvatar().getUrl(), userProfileImageView);
//                    }
//                } else {
//                    userProfileImageView.setImageResource(R.drawable.profile_image_default);
//                }
                userNameTextView.setText(loggedInUser.getUseName());
                initializeItemsList();
            }
            else {
//                userNameTextView.setText(getResources().getString(R.string.navigation_drawer_user_name));
//                userProfileImageView.setImageResource(R.drawable.profile_image_default);
                initializeItemsList();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    private ActionBar getActionBar() {
        return ((MainActivity) getActivity()).getSupportActionBar();
    }

    public void reload() {
        initializeItemsList();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    public class MyActionBarDrawerToggle extends ActionBarDrawerToggle {

        public MyActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        public MyActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            if (!isAdded()) {
                return;
            }

            getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            if (!isAdded()) {
                return;
            }

            if (!mUserLearnedDrawer) {
                // The user manually opened the drawer; store this flag to prevent auto-showing
                // the navigation drawer automatically in the future.
                mUserLearnedDrawer = true;
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
            }

            getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
        }
    }
}
