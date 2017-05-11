package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakeMachineDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakePartDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakeProcessDaoImpl;
import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.model.Machine;
import com.gmail.osbornroad.cycletime.model.Part;
import com.gmail.osbornroad.cycletime.model.Process;
import com.gmail.osbornroad.cycletime.service.EmployeeService;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakeMachineServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakePartServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakeProcessServiceImpl;
import com.gmail.osbornroad.cycletime.service.MachineService;
import com.gmail.osbornroad.cycletime.service.PartService;
import com.gmail.osbornroad.cycletime.service.ProcessService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private Class fragmentClass;
    private FragmentManager fragMan;

    private EmployeeService employeeService;
    private ProcessService processService;
    private MachineService machineService;
    private PartService partService;

    protected boolean mStartedInActivity;
    protected boolean mInProgressInActivity;
    protected Employee selectedEmployeeInActivity;
    protected Process selectedProcessInActivity;
    protected Machine selectedMachineInActivity;
    protected Part selectedPartInActivity;
    protected int partQuantityInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (fragment == null) {
            try {
                fragment = StopWatchFragment.class.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_main, fragment, "visible_fragment").commit();
            setTitle(getResources().getString(R.string.stopwatch_fragment_title));
        }

        setSavedInfo(savedInstanceState);
    }

    private void setSavedInfo(Bundle savedInstanceState) {
        if (fragment.getClass() != StopWatchFragment.class) {
            return;
        }

        employeeService = new EmployeeServiceImpl(new FakeEmployeeDaoImpl());
        processService = new FakeProcessServiceImpl(new FakeProcessDaoImpl());
        machineService = new FakeMachineServiceImpl(new FakeMachineDaoImpl());
        partService = new FakePartServiceImpl(new FakePartDaoImpl());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("mStarted")) {
                mStartedInActivity = savedInstanceState.getBoolean("mStarted");
            }
            if (savedInstanceState.containsKey("mInProgress")) {
                mInProgressInActivity = savedInstanceState.getBoolean("mInProgress");
            }
            if (savedInstanceState.containsKey("employeeId")) {
                int employeeId = savedInstanceState.getInt("employeeId");
                selectedEmployeeInActivity = employeeService.get(employeeId);
            }
            if (savedInstanceState.containsKey("processId")) {
                int processId = savedInstanceState.getInt("processId");
                selectedProcessInActivity = processService.get(processId);
            }
            if (savedInstanceState.containsKey("machineId")) {
                int machineId = savedInstanceState.getInt("machineId");
                selectedMachineInActivity = machineService.get(machineId);
            }
            if (savedInstanceState.containsKey("partId")) {
                int partId = savedInstanceState.getInt("partId");
                selectedPartInActivity = partService.get(partId);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mStarted", mStartedInActivity);
        outState.putBoolean("mInProgress", mInProgressInActivity);
        if (selectedEmployeeInActivity != null) {
            outState.putInt("employeeId", selectedEmployeeInActivity.getId());
        }
        if (selectedProcessInActivity != null) {
            outState.putInt("processId", selectedProcessInActivity.getId());
        }
        if (selectedMachineInActivity != null) {
            outState.putInt("machineId", selectedMachineInActivity.getId());
        }
        if (selectedPartInActivity != null) {
            outState.putInt("partId", selectedPartInActivity.getId());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.main_action_calc) {

                fragMan = getSupportFragmentManager();
                fragment = fragMan.findFragmentByTag("visible_fragment");

                if (!(fragment instanceof StopWatchFragment)) {
                    return false;
                }

                Employee selectedEmployee = ((StopWatchFragment) fragment).getSelectedEmployee();
                Process selectedProcess = ((StopWatchFragment) fragment).getSelectedProcess();
                Machine selectedMachine = ((StopWatchFragment) fragment).getSelectedMachine();
                Part selectedPart = ((StopWatchFragment) fragment).getSelectedPart();
                EditText partQuantity = ((StopWatchFragment) fragment).getPartQuantity();
                boolean mInProgress = ((StopWatchFragment) fragment).ismInProgress();
                boolean mStarted = ((StopWatchFragment) fragment).ismStarted();
                StopWatch stopWatch = ((StopWatchFragment) fragment).getStopWatch();

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
                if (!partQuantity.getText().toString().equals("")) {
                    int quantity;
                    try {
                        quantity = Integer.parseInt(partQuantity.getText().toString());
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

/*        Fragment fragment = null;
        Class fragmentClass = null;*/
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

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_main, fragment, "visible_fragment").commit();
        item.setChecked(true);
        setTitle(item.getTitle());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
