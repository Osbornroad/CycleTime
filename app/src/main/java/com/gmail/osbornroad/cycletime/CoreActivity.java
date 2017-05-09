package com.gmail.osbornroad.cycletime;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CoreActivity extends AppCompatActivity {

    private String[] mMainDrawerArray;
    private ListView mDrawerListView;

    DrawerLayout drawerLayout;

    private static final int STOPWATCH_DRAWER_ITEM = 0;
    private static final int EMPLOYEE_DRAWER_ITEM = 1;
    private static final int PROCESS_DRAWER_ITEM = 2;
    private static final int MACHINE_DRAWER_ITEM = 3;
    private static final int PART_DRAWER_ITEM = 4;

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
    }

    private void selectDrawerItem(int position){
        Fragment fragment;
        String title;

        switch (position) {
            case STOPWATCH_DRAWER_ITEM:
                fragment = new StopWatchFragment();
                title = getResources().getString(R.string.stopwatch_fragment_title);
                break;
            case EMPLOYEE_DRAWER_ITEM:
                fragment = new EmployeesFragment();
                title = getResources().getString(R.string.employees_fragment_title);
                break;
            case PROCESS_DRAWER_ITEM:
                fragment = new ProcessesFragment();
                title = getResources().getString(R.string.processes_fragment_title);
                break;
            case MACHINE_DRAWER_ITEM:
                fragment = new MachinesFragment();
                title = getResources().getString(R.string.machines_fragment_title);
                break;
            case PART_DRAWER_ITEM:
                fragment = new PartsFragment();
                title = getResources().getString(R.string.parts_fragment_title);
                break;
            default:
                fragment = new StopWatchFragment();
                title = getResources().getString(R.string.stopwatch_fragment_title);
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.core_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        getSupportActionBar().setTitle(title);


        drawerLayout.closeDrawer(mDrawerListView);
    }
}
