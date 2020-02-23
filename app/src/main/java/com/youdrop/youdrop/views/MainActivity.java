package com.youdrop.youdrop.views;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.util.Log;

import com.youdrop.youdrop.controllers.EditableProfileController;
import com.youdrop.youdrop.controllers.MainController;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.views.sections.AddFragment;
import com.youdrop.youdrop.views.sections.EditableProfileFragment;
import com.youdrop.youdrop.views.sections.EditableProfileView;
import com.youdrop.youdrop.views.sections.FriendsTabFragment;
import com.youdrop.youdrop.views.sections.NearFragment;
import com.youdrop.youdrop.views.sections.NotificationsFragment;
import com.youdrop.youdrop.views.sections.ProfileFragment;
import com.youdrop.youdrop.R;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements MainView, NotificationsFragment.OnNotificationInteractionListener, AddFragment.OnFragmentInteractionListener, EditableProfileFragment.OnFragmentInteractionListener{

    private MainController controller;


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private BottomNavigationView navigation;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_notifications:
                    invalidateOptionsMenu();
                    controller.menuNotificationsClicked();
                    return true;
                case R.id.navigation_near:
                    invalidateOptionsMenu();
                    controller.menuNearClicked();
                    return true;
                case R.id.navigation_add:
                    invalidateOptionsMenu();
                    controller.menuAddClicked();
                    return true;
                case R.id.navigation_friends:
                    invalidateOptionsMenu();
                    controller.menuFriendsClicked();
                    return true;
                case R.id.navigation_profile:
                    invalidateOptionsMenu();
                    controller.menuProfileClicked();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        controller = new MainController(this);
        controller.checkAuth();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish")) {
                    //finishing the activity
                    finish();
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish"));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_notifications);
                        return;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_near);
                        return;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_add);
                        return;
                    case 3:
                        navigation.setSelectedItemId(R.id.navigation_friends);
                        return;
                    case 4:
                        navigation.setSelectedItemId(R.id.navigation_profile);
                        return;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
    });
        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.pause();
    }

    private void getSession(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
       // editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.commit();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!controller.getSection().equals(MainController.Section.FRIENDS)){
            menu.removeItem(R.id.action_add_friend);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add_friend:
                controller.showFriendSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showNotiicationsPanel() {
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void showNearPanel() {
        mViewPager.setCurrentItem(1);

    }

    @Override
    public void showAddPanel() {
        mViewPager.setCurrentItem(2);

    }

    @Override
    public void showFriendsPanel() {
        mViewPager.setCurrentItem(3);

    }

    @Override
    public void showProfilePanel() {
        mViewPager.setCurrentItem(4);

    }

    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private EditableProfileFragment profile = EditableProfileFragment.newInstance(ProfileFragment.CURRENT_USER);

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0)return NotificationsFragment.newInstance(controller.grtAUthToken());
            if (position == 1)return NearFragment.newInstance("","");
            if (position == 2)return AddFragment.newInstance("Add","");
            if (position == 3)return FriendsTabFragment.newInstance(controller.grtAUthToken());
            if (position == 4)return profile;
            return NotificationsFragment.newInstance(controller.grtAUthToken());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return "";
        }
    }

    @Override
    public void onNotificationInteraction(Notification item) {

    }

    @Override
    public void showLogin() {
        startActivityForResult(new Intent(this,LoginActivity.class), 123);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123){
            int current = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(1);
        }

    }

    @Override
    public Activity getActivity() {
        return this;
    }

}
