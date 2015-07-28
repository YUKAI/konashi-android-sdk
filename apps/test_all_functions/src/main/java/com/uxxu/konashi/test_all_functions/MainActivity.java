package com.uxxu.konashi.test_all_functions;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiManager;
import com.uxxu.konashi.lib.KonashiObserver;
import com.uxxu.konashi.lib.KonashiUtils;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String[] MENU_TITLES = {
            "HOME",
            PioFragment.TITLE
    };

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Menu mMenu;
    private KonashiManager mKonashiManager;
    private KonashiObserver mKonashiObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKonashiManager = Konashi.getManager();
        mKonashiManager.initialize(getApplicationContext());
        mKonashiObserver = new KonashiObserver(this) {
            @Override
            public void onReady() {
                KonashiUtils.log("onReady");
                setTitle(mKonashiManager.getPeripheralName());
                mMenu.findItem(R.id.action_find_konashi).setVisible(false);
                mMenu.findItem(R.id.action_disconnect).setVisible(true);
            }

            @Override
            public void onDisconncted() {
                KonashiUtils.log("onDisconnected");
                super.onDisconncted();
                // TODO: fix typo
                // TODO: Not called
            }
        };
        mKonashiManager.addObserver(mKonashiObserver);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onDestroy() {
        if (mKonashiManager != null) {
            mKonashiManager.disconnect();
            mKonashiManager.close();
            mKonashiManager = null;
        }
        super.onDestroy();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new BaseFragment();
                break;
            case 1:
                fragment = new PioFragment();
                break;
        }
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putInt(BaseFragment.ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        if (number == 0) {
            setTitle("HOME");
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        refreshActionBarMenu();
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_find_konashi:
                mKonashiManager.find(this);
                return true;
            case R.id.action_disconnect:
                mKonashiManager.disconnect();
                refreshActionBarMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshActionBarMenu() {
        if (mKonashiManager.isConnected()) {
            mMenu.findItem(R.id.action_find_konashi).setVisible(false);
            mMenu.findItem(R.id.action_disconnect).setVisible(true);
        } else {
            mMenu.findItem(R.id.action_find_konashi).setVisible(true);
            mMenu.findItem(R.id.action_disconnect).setVisible(false);
        }
    }

    public static class BaseFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        public BaseFragment() {
        }

        public MainActivity getMainActivity() {
            return (MainActivity) getActivity();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            getMainActivity().onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
