package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.model.Machine;
import com.gmail.osbornroad.cycletime.model.Part;
import com.gmail.osbornroad.cycletime.model.Process;
import com.gmail.osbornroad.cycletime.service.EmployeeService;
import com.gmail.osbornroad.cycletime.service.MachineService;
import com.gmail.osbornroad.cycletime.service.PartService;
import com.gmail.osbornroad.cycletime.service.ProcessService;
import com.gmail.osbornroad.cycletime.utility.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private Class fragmentClass;
    private FragmentManager fragMan;
//    FragmentManager fm;

    protected EmployeeService employeeService;
    protected ProcessService processService;
    protected MachineService machineService;
    protected PartService partService;

    /**
     * mStarted == true after Start button pressed until Stop pressed
     * mInProgress == true after Resume/Start button, Continue counting until Reset pressed
     */
    protected boolean mStarted;
    protected boolean mInProgress;

    protected Employee selectedEmployee;
    protected Process selectedProcess;
    protected Machine selectedMachine;
    protected Part selectedPart;
    protected int partQuantity;

    private NavigationView navigationView = null;
    /**
     * StopWatch is singletone
     */
    private StopWatch stopWatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stopWatch = StopWatch.getStopWatch();

        employeeService = Utility.getEmployeeService();
        processService = Utility.getProcessService();
        machineService = Utility.getMachineService();
        partService = Utility.getPartService();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                /*final Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem item = menu.getItem(i);
                    if (item.hasSubMenu()) {
                        SubMenu subMenu = item.getSubMenu();
                        for (int j = 0; j < subMenu.size(); j++) {
                            MenuItem subMenuItem = subMenu.getItem(j);
                            subMenuItem.setChecked(false);
                        }
                    } else {
                        item.setChecked(false);
                    }
                }*/
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        fragMan = getSupportFragmentManager();
        fragMan.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                setActionBarTitle();
            }
        });

        setSavedFragment(savedInstanceState);

/*        if (fragment == null) {
            try {
                fragment = StopWatchFragment.class.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            fragMan.beginTransaction().replace(R.id.content_main, fragment, "visible_fragment").commit();
            setTitle(getResources().getString(R.string.stopwatch_fragment_title));
        }*/

//        if (fragment.getClass() == StopWatchFragment.class) {
            setSavedInfo(savedInstanceState);
//        }

    }

    private void setSavedFragment(Bundle savedInstanceState) {
        Class savedFragmentClass = null;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("fragmentName")) {
                try {
                    savedFragmentClass = Class.forName((String) savedInstanceState.get("fragmentName"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            savedFragmentClass = StopWatchFragment.class;
        }
        switchFragment(savedFragmentClass, "");
    }

    private void setActionBarTitle() {
        String title = null;
        if (fragment instanceof StopWatchFragment) {
            title = getResources().getString(R.string.stopwatch_fragment_title);
        } else if (fragment instanceof EmployeesFragment) {
            title = getResources().getString(R.string.employees_fragment_title);
        }
        setTitle(title);
    }

/*    protected void switchFragmentWithoutBackStack(Class fragmentClass, String title) {
        try {
            fragment= (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction transaction = fragMan.beginTransaction();
        transaction.replace(R.id.content_main, fragment, "visible_fragment");
//        transaction.setTransition(TRANSIT_FRAGMENT_FADE);
//        transaction.addToBackStack(null);
        transaction.commit();
        invalidateOptionsMenu();
        setActionBarTitle();
//        setTitle(title);
    }*/

    protected void switchFragment(Class fragmentClass, String title) {
        Fragment currentFragment = fragMan.findFragmentByTag("visible_fragment");
        try {
            fragment= (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction transaction = fragMan.beginTransaction();
        transaction.replace(R.id.content_main, fragment, "visible_fragment");
/*//        transaction.setTransition(TRANSIT_FRAGMENT_FADE);
        if (currentFragment != null) {
            transaction.remove(currentFragment);
*//*            if (fragment.getClass() != currentFragment.getClass()) {
                transaction.addToBackStack(null);
            }*//*
        }
        */

//        transaction.add(R.id.content_main, fragment, "visible_fragment");
        if (currentFragment != null) {
            if (fragment.getClass() == currentFragment.getClass()) {
                fragMan.popBackStack();
            }
        }
        transaction.addToBackStack(null);
        transaction.commit();
        invalidateOptionsMenu();
        setActionBarTitle();
//        setTitle(title);
    }

    private void setSavedInfo(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("mStarted")) {
                mStarted = savedInstanceState.getBoolean("mStarted");
            }
            if (savedInstanceState.containsKey("mInProgress")) {
                mInProgress = savedInstanceState.getBoolean("mInProgress");
            }
            if (savedInstanceState.containsKey("employeeId")) {
                int employeeId = savedInstanceState.getInt("employeeId");
                selectedEmployee = employeeService.get(employeeId);
            }
            if (savedInstanceState.containsKey("processId")) {
                int processId = savedInstanceState.getInt("processId");
                selectedProcess = processService.get(processId);
            }
            if (savedInstanceState.containsKey("machineId")) {
                int machineId = savedInstanceState.getInt("machineId");
                selectedMachine = machineService.get(machineId);
            }
            if (savedInstanceState.containsKey("partId")) {
                int partId = savedInstanceState.getInt("partId");
                selectedPart = partService.get(partId);
            }
            if (savedInstanceState.containsKey("partQuantity")) {
                partQuantity = savedInstanceState.getInt("partQuantity");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mStarted", mStarted);
        outState.putBoolean("mInProgress", mInProgress);
        if (fragment != null) {
            outState.putString("fragmentName", fragment.getClass().getName());
        }
        if (selectedEmployee != null) {
            outState.putInt("employeeId", selectedEmployee.getId());
        }
        if (selectedProcess != null) {
            outState.putInt("processId", selectedProcess.getId());
        }
        if (selectedMachine != null) {
            outState.putInt("machineId", selectedMachine.getId());
        }
        if (selectedPart != null) {
            outState.putInt("partId", selectedPart.getId());
        }
        if (partQuantity > 0) {
            outState.putInt("partQuantity", partQuantity);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (fragMan.getBackStackEntryCount() == 0) {
            finish();
        }
        fragment = fragMan.findFragmentByTag("visible_fragment");
        setActionBarTitle();

        /**
         * TODO: Add interface NavigationFragment to all fragments
         */

        final Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem item = menu.getItem(i);
                    if (item.hasSubMenu()) {
                        SubMenu subMenu = item.getSubMenu();
                        for (int j = 0; j < subMenu.size(); j++) {
                            MenuItem subMenuItem = subMenu.getItem(j);
                            subMenuItem.setChecked(false);
                        }
                    } else {
                        item.setChecked(false);
                    }
                }

        MenuItem item = menu.getItem(((NavigationFragment) fragment).getMenuId());
        item.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.main_action_calc).setVisible(fragment.getClass() == StopWatchFragment.class);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            }
            return true;
        }

        if (id == R.id.main_action_calc) {

                fragMan = getSupportFragmentManager();
                fragment = fragMan.findFragmentByTag("visible_fragment");

                if (!(fragment instanceof StopWatchFragment)) {
                    return false;
                }

                Intent intent = new Intent(this, ResultMeasurementActivity.class);
                if (selectedEmployee != null) {
                    intent.putExtra("employeeId", selectedEmployee.getId());
                }
                if (selectedProcess != null) {
                    intent.putExtra("processId", selectedProcess.getId());
                }
                if (selectedMachine != null) {
                    intent.putExtra("machineId", selectedMachine.getId());
                }
                if (selectedPart != null) {
                    intent.putExtra("partId", selectedPart.getId());
                }
                if (partQuantity > 0) {
                    int quantity;
                    try {
                        quantity = Integer.parseInt(String.valueOf(partQuantity));
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Please input correct quantity", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    intent.putExtra("partQuantity", quantity);
                } else {
                    Toast.makeText(this, "Please input correct quantity", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (mInProgress) {
                    if (!mStarted) {
                        int resultStopWatch = (int) stopWatch.getElapsedTimeInSec();
                        intent.putExtra("resultStopWatch", resultStopWatch);
                    } else {
                        Toast.makeText(this, "Stopwatch still running", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                else {
                    Toast.makeText(this, "Stopwatch data is zero", Toast.LENGTH_SHORT).show();
                    return true;
                }
                startActivity(intent);

                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        if (id == R.id.nav_stopwatch) {
            if (fragment.getClass() == StopWatchFragment.class) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            fragmentClass = StopWatchFragment.class;
        } else if (id == R.id.nav_employee) {
            fragmentClass = EmployeesFragment.class;
        } else if (id == R.id.nav_processes) {
            fragmentClass = ProcessesFragment.class;
        } else if (id == R.id.nav_machines) {
            fragmentClass = MachinesFragment.class;
        } else if (id == R.id.nav_parts) {
            fragmentClass = PartsFragment.class;
        }

/*        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

//        FragmentManager fm = getSupportFragmentManager();
        switchFragment(fragmentClass, "");
//        fragMan.beginTransaction().replace(R.id.content_main, fragment, "visible_fragment").commit();
        item.setChecked(true);
//        setTitle(item.getTitle());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setSelectedEmployee(int clickedEmployeeId) {
/*        selectedEmployee = employeeService.get(clickedEmployeeId);
        switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));*/
    }
}
