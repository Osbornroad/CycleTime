/*
package com.gmail.osbornroad.cycletime;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CoreActivity extends AppCompatActivity {

    private String[] mMainDrawerArray;
    private ListView mDrawerListView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private static final int STOPWATCH_DRAWER_ITEM = 0;
    private static final int EMPLOYEE_DRAWER_ITEM = 1;
    private static final int PROCESS_DRAWER_ITEM = 2;
    private static final int MACHINE_DRAWER_ITEM = 3;
    private static final int PART_DRAWER_ITEM = 4;

    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        mMainDrawerArray = getResources().getStringArray(R.array.main_drawer_array);
        mDrawerListView = (ListView) findViewById(R.id.list_for_core_drawer);

        mDrawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMainDrawerArray));

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectDrawerItem(position);
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.core_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        } else {
            selectDrawerItem(0);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        FragmentManager fragMan = getSupportFragmentManager();
                        Fragment fragment = fragMan.findFragmentByTag("visible_fragment");
                        if (fragment instanceof StopWatchFragment) {
                            currentPosition = 0;
                        }
                        if (fragment instanceof EmployeesFragment) {
                            currentPosition = 1;
                        }
                        if (fragment instanceof ProcessesFragment) {
                            currentPosition = 2;
                        }
                        if (fragment instanceof MachinesFragment) {
                            currentPosition = 3;
                        }
                        if (fragment instanceof PartsFragment) {
                            currentPosition = 4;
                        }
                        setActionBarTitle(currentPosition);
                        mDrawerListView.setItemChecked(currentPosition, true);
                    }
                }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(mDrawerListView);
        //Input here code for hide some item of menu if Drawer is opened
        return super.onPrepareOptionsMenu(menu);
    }

    private void selectDrawerItem(int position){
        Fragment fragment;
//        String title;

        currentPosition = position;

        switch (position) {
            case STOPWATCH_DRAWER_ITEM:
                fragment = new StopWatchFragment();
//                title = getResources().getString(R.string.stopwatch_fragment_title);
                break;
            case EMPLOYEE_DRAWER_ITEM:
                fragment = new EmployeesFragment();
//                title = getResources().getString(R.string.employees_fragment_title);
                break;
            case PROCESS_DRAWER_ITEM:
                fragment = new ProcessesFragment();
//                title = getResources().getString(R.string.processes_fragment_title);
                break;
            case MACHINE_DRAWER_ITEM:
                fragment = new MachinesFragment();
//                title = getResources().getString(R.string.machines_fragment_title);
                break;
            case PART_DRAWER_ITEM:
                fragment = new PartsFragment();
//                title = getResources().getString(R.string.parts_fragment_title);
                break;
            default:
                fragment = new StopWatchFragment();
//                title = getResources().getString(R.string.stopwatch_fragment_title);
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.core_frame_layout, fragment, "visible_fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

//        getSupportActionBar().setTitle(title);

        setActionBarTitle(position);

        drawerLayout.closeDrawer(mDrawerListView);
    }

    private void setActionBarTitle(int position) {
        String title;
        title = mMainDrawerArray[position];
        getSupportActionBar().setTitle(title);
    }
}
*/
