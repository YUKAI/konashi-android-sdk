package com.uxxu.konashi.test_all_functions;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiManager;
import com.uxxu.konashi.lib.KonashiObserver;
import com.uxxu.konashi.lib.KonashiUtils;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private View overlay;
    private Menu mMenu;
    private KonashiManager mKonashiManager = Konashi.getManager();
    private KonashiObserver mKonashiObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKonashiManager.initialize(getApplicationContext());
        mKonashiObserver = new KonashiObserver(this) {

            @Override
            public void onReady() {
                KonashiUtils.log("onReady");
                refreshActionBarMenu();
                overlay.setVisibility(View.GONE);

                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDisconncted() {
                KonashiUtils.log("onDisconnected");
                refreshActionBarMenu();
                overlay.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            }
        };
        mKonashiManager.addObserver(mKonashiObserver);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        overlay = findViewById(R.id.overlay);
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
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new PioFragment();
                break;
            case 2:
                fragment = new PwmFragment();
                break;
            case 3:
                fragment = new AioFragment();
                break;
            case 4:
                fragment = new CommunicationFragment();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
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
            case android.R.id.home:
                if (mNavigationDrawerFragment.isDrawerOpen()) {
                    mNavigationDrawerFragment.close();
                } else {
                    mNavigationDrawerFragment.open();
                }
                break;
            case R.id.action_find_konashi:
                mKonashiManager.find(this);
                return true;
            case R.id.action_disconnect:
                mKonashiManager.disconnect();
                refreshActionBarMenu();
                overlay.setVisibility(View.VISIBLE);
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

        protected final KonashiManager mKonashiManager = Konashi.getManager();

        public BaseFragment() {
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }
}
