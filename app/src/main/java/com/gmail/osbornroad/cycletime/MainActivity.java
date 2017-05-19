package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
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

import com.gmail.osbornroad.cycletime.dao.StopWatchDbHelper;
import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.model.Machine;
import com.gmail.osbornroad.cycletime.model.Part;
import com.gmail.osbornroad.cycletime.model.Process;

//import com.gmail.osbornroad.cycletime.service.MachineService;
//import com.gmail.osbornroad.cycletime.service.PartService;
//import com.gmail.osbornroad.cycletime.service.ProcessService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    DialogEmployeeFragment.DialogEmployeeListener{

    private Fragment fragment;
    private Class fragmentClass;
    protected FragmentManager fragMan;
//    FragmentManager fm;

//    protected ProcessService processService;
//    protected MachineService machineService;
//    protected PartService partService;

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
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    /**
     * StopWatch is singletone
     */
    private StopWatch stopWatch;

    protected SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stopWatch = StopWatch.getStopWatch();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
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
                if ((fragMan.getBackStackEntryCount() > 1) && (fragment.getClass() == StopWatchFragment.class)) {
                    fragMan.popBackStack();
                }

            }
        });

        setSavedFragment(savedInstanceState);
        setSavedInfo(savedInstanceState);
        StopWatchDbHelper helper = new StopWatchDbHelper(this);
        mDb = helper.getWritableDatabase();

    }

    /**
     * Switch hamburger to back button and revert back
     */
    private void setDrawerAccess() {
        if (fragment != null) {
            if (fragment.getClass() == StopWatchFragment.class) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setToolbarNavigationClickListener(null);
            }
            else {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                toggle.setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
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
        } else if (fragment instanceof ProcessesFragment) {
            title = getResources().getString(R.string.processes_fragment_title);
        } else if (fragment instanceof MachinesFragment) {
            title = getResources().getString(R.string.machines_fragment_title);
        } else if (fragment instanceof PartsFragment) {
            title = getResources().getString(R.string.parts_fragment_title);
        }
        setTitle(title);
    }

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
//        transaction.replace(R.id.content_main, fragment, "visible_fragment");

        if (currentFragment != null) {
            transaction.remove(currentFragment);
        }
        transaction.add(R.id.content_main, fragment, "visible_fragment");

        if (currentFragment != null) {
            if (fragment.getClass() == currentFragment.getClass() && (fragMan.getBackStackEntryCount() > 1)) {
                fragMan.popBackStack();
            }
        }
        transaction.addToBackStack(null);
        transaction.commit();
        invalidateOptionsMenu();
        setActionBarTitle();
        setDrawerAccess();
    }

    private void setSavedInfo(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("mStarted")) {
                mStarted = savedInstanceState.getBoolean("mStarted");
            }
            if (savedInstanceState.containsKey("mInProgress")) {
                mInProgress = savedInstanceState.getBoolean("mInProgress");
            }
            if (savedInstanceState.containsKey("selectedEmployee")) {
                selectedEmployee = savedInstanceState.getParcelable("selectedEmployee");
            }
            if (savedInstanceState.containsKey("selectedProcess")) {
                selectedProcess = savedInstanceState.getParcelable("selectedProcess");
            }
            if (savedInstanceState.containsKey("selectedMachine")) {
                selectedMachine = savedInstanceState.getParcelable("selectedMachine");
            }
            if (savedInstanceState.containsKey("selectedPart")) {
                selectedPart = savedInstanceState.getParcelable("selectedPart");
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
            outState.putParcelable("selectedEmployee", selectedEmployee);
        }
        if (selectedProcess != null) {
            outState.putParcelable("selectedProcess", selectedProcess);
        }
        if (selectedMachine != null) {
            outState.putParcelable("selectedMachine", selectedMachine);
        }
        if (selectedPart != null) {
            outState.putParcelable("selectedPart", selectedPart);
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
            return;
        }
        fragment = fragMan.findFragmentByTag("visible_fragment");
        setActionBarTitle();

        setDrawerAccess();
        /**
         * TODO: Add interface NavigationFragment to all fragments
         */

        invalidateOptionsMenu();

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
        menu.findItem(R.id.main_action_employee_plus).setVisible(fragment.getClass() == EmployeesFragment.class);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.main_action_employee_plus) {
            DialogEmployeeFragment dialogEmployeeFragment = new DialogEmployeeFragment();
            dialogEmployeeFragment.show(fragMan, "dialogEmployeeFragment");
        }

        if (id == R.id.main_action_calc) {

                fragMan = getSupportFragmentManager();
                fragment = fragMan.findFragmentByTag("visible_fragment");

                if (!(fragment instanceof StopWatchFragment)) {
                    return false;
                }

                Intent intent = new Intent(this, ResultMeasurementActivity.class);
                if (selectedEmployee != null) {
                    intent.putExtra("selectedEmployee", selectedEmployee);
                }
                if (selectedProcess != null) {
                    intent.putExtra("selectedProcess", selectedProcess);
                }
                if (selectedMachine != null) {
                    intent.putExtra("selectedMachine", selectedMachine);
                }
                if (selectedPart != null) {
                    intent.putExtra("selectedPart", selectedPart);
                }
                try {
                    partQuantity = Integer.parseInt(((StopWatchFragment) fragment).partQuantity.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(this, "Please input correct quantity", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (partQuantity > 0) {
                    intent.putExtra("partQuantity", partQuantity);
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
        switchFragment(fragmentClass, "");
        item.setChecked(true);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setSelectedEmployee(int clickedEmployeeId) {
/*        selectedEmployee = employeeService.get(clickedEmployeeId);
        switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));*/
    }

    @Override
    public void onDialogPositiveCheck(DialogFragment dialog) {
        Toast.makeText(this, "Pressed Ok button", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeCheck(DialogFragment dialog) {
        Toast.makeText(this, "Pressed Cancel button", Toast.LENGTH_SHORT).show();
    }
}
