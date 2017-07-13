package com.gmail.osbornroad.cycletime;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract;
import com.gmail.osbornroad.cycletime.dao.StopWatchDbHelper;
import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.model.Machine;
import com.gmail.osbornroad.cycletime.model.Part;
import com.gmail.osbornroad.cycletime.model.Process;
import com.gmail.osbornroad.cycletime.utility.Utility;

import java.io.File;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DialogEmployeeFragment.DialogEmployeeListener,
        DialogProcessFragment.DialogProcessListener,
        DialogMachineFragment.DialogMachineListener,
        DialogPartFragment.DialogPartListener{

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
    StopWatchDbHelper helper;

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
/*                if ((fragMan.getBackStackEntryCount() > 1) && (fragment.getClass() == StopWatchFragment.class)) {
                    fragMan.popBackStack();
                }*/
                if (fragment.getClass() == StopWatchFragment.class) {
                    if (fragMan.getBackStackEntryCount() > 1) {
                        fragMan.popBackStack();
                    }
                } else if (fragMan.getBackStackEntryCount() > 2) {
                    fragMan.popBackStack();
                }
            }
        });

        setSavedFragment(savedInstanceState);
        setSavedInfo(savedInstanceState);
        helper = new StopWatchDbHelper(this);
        mDb = helper.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
            } else {
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
        } else if (fragment instanceof SampleFragment) {
            title = getResources().getString(R.string.sample_fragment_title);
        }
        setTitle(title);
    }

    protected void switchFragment(Class fragmentClass, String title) {
        Fragment currentFragment = fragMan.findFragmentByTag("visible_fragment");
        try {
            fragment = (Fragment) fragmentClass.newInstance();
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

/*        int count;
        if (currentFragment != null) {
            if (fragment.getClass() == currentFragment.getClass()){
                if ((count = fragMan.getBackStackEntryCount()) > 1){
                    fragMan.popBackStack();
                }
            }
        }*/
        transaction.addToBackStack(null);
        transaction.commit();
        invalidateOptionsMenu();
        setActionBarTitle();
        setDrawerAccess();
        showAll = false;
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
            stopWatch.resetStopWatch();
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
        showAll = false;

        if (fragment.getClass() == StopWatchFragment.class) {
            stopWatch.resetStopWatch();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.main_action_calc).setVisible(fragment.getClass() == StopWatchFragment.class);
//        menu.findItem(R.id.main_action_employee_plus).setVisible(fragment.getClass() == EmployeesFragment.class);
//        menu.findItem(R.id.main_action_process_plus).setVisible(fragment.getClass() == ProcessesFragment.class);
//        menu.findItem(R.id.main_action_machine_plus).setVisible(fragment.getClass() == MachinesFragment.class);
//        menu.findItem(R.id.main_action_part_plus).setVisible(fragment.getClass() == PartsFragment.class);

        menu.findItem(R.id.send_file).setVisible(fragment.getClass() == SampleFragment.class);

        menu.findItem(R.id.main_action_show_all)
                .setIcon(!showAll ? R.drawable.ic_visibility_white_24dp : R.drawable.ic_visibility_off_white_24dp)
                .setVisible((fragment.getClass() != StopWatchFragment.class) && (fragment.getClass() != SampleFragment.class));

        menu.findItem(R.id.main_action_sort)
                .setIcon(((NavigationFragment)fragment).getSavedSorting() ? R.drawable.ic_sort_white_24dp : R.drawable.ic_sort_by_alpha_white_24dp)
                .setVisible((fragment.getClass() != StopWatchFragment.class) && (fragment.getClass() != SampleFragment.class));

        return super.onPrepareOptionsMenu(menu);
    }

    boolean showAll = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
/*        if (id == R.id.main_action_employee_plus) {
            DialogEmployeeFragment dialogEmployeeFragment = new DialogEmployeeFragment();
            dialogEmployeeFragment.show(fragMan, "dialogEmployeeFragment");
        }*/
        if (id == R.id.send_file) {
            Utility.exportDB(helper, getApplicationContext());

            File fileLocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Utility.FILE_NAME);
            Uri path = Uri.fromFile(fileLocation);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, Utility.getMailAddresses());
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Collect table");
            emailIntent.putExtra(Intent.EXTRA_STREAM, path);
            emailIntent.setType("message/rfc822");
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
            else {
                Toast.makeText(this, R.string.no_email_app, Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.main_action_sort) {
            ((NavigationFragment) fragment).setSortingType();
            invalidateOptionsMenu();
            ((NavigationFragment) fragment).updateView();
        }
        if (id == R.id.main_action_show_all) {
            showAll = !showAll;
            invalidateOptionsMenu();
            ((NavigationFragment) fragment).updateView();
        }
/*        if (id == R.id.main_action_process_plus) {
            DialogProcessFragment dialogProcessFragment = new DialogProcessFragment();
            dialogProcessFragment.show(fragMan, "dialogEmployeeFragment");
        }
        if (id == R.id.main_action_machine_plus) {
            DialogMachineFragment dialogMachineFragment = new DialogMachineFragment();
            dialogMachineFragment.show(fragMan, "dialogMachineFragment");
        }
        if (id == R.id.main_action_part_plus) {
            DialogPartFragment dialogPartFragment = new DialogPartFragment();
            dialogPartFragment.show(fragMan, "dialogPartFragment");
        }*/
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
                Toast.makeText(this, R.string.quantity_not_number, Toast.LENGTH_SHORT).show();
                return true;
            }
            if (partQuantity > 0) {
                intent.putExtra("partQuantity", partQuantity);
            } else {
                Toast.makeText(this, R.string.quantity_less_than_one, Toast.LENGTH_SHORT).show();
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
            } else {
                Toast.makeText(this, "Stopwatch data is zero", Toast.LENGTH_SHORT).show();
                return true;
            }
            intent.putExtra("startTime", stopWatch.getStartDateTime());
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
        } else if (id == R.id.nav_samples) {
            fragmentClass = SampleFragment.class;
        }
        switchFragment(fragmentClass, "");
        item.setChecked(true);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onEmployeeDialogPositiveCheck(DialogFragment dialog, Employee employee) {
        int newOrderNumber = employee == null ? Utility.getMaxOrderNumberOfEmployees(mDb) + 1 : employee.getOrderNumber();
        String newEmployeeName = ((DialogEmployeeFragment) dialog).getEmployeeName().getText().toString();
        boolean newEmployeeEnable = ((DialogEmployeeFragment) dialog).getEnable().isChecked();
        if ("".equals(newEmployeeName)) {
            Toast.makeText(getApplicationContext(), R.string.dialog_no_data, Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER, newOrderNumber);
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, newEmployeeName);
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, newEmployeeEnable);
        if (employee == null) {
            mDb.insert(StopWatchContract.EmployeeEntry.TABLE_NAME, null, cv);
        } else {
            mDb.update(StopWatchContract.EmployeeEntry.TABLE_NAME, cv,
                    StopWatchContract.EmployeeEntry._ID + " = ?",
                    new String[]{String.valueOf(employee.getId())});
        }
        ((NavigationFragment) fragment).updateView();
    }

    @Override
    public void onEmployeeDialogNegativeCheck(DialogFragment dialog) {
        ((NavigationFragment) fragment).updateView();
    }

    @Override
    public void onProcessDialogPositiveCheck(DialogFragment dialog, Process process) {
        int newOrderNumber = process == null ? Utility.getMaxOrderNumberOfProcesses(mDb) + 1 : process.getOrderNumber();
        String newProcessName = ((DialogProcessFragment) dialog).getProcessName().getText().toString();
        boolean newProcessEnable = ((DialogProcessFragment) dialog).getEnable().isChecked();
        if ("".equals(newProcessName)) {
            Toast.makeText(getApplicationContext(), R.string.dialog_no_data, Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ORDER_NUMBER, newOrderNumber);
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_NAME, newProcessName);
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ENABLE, newProcessEnable);
        if (process == null) {
            mDb.insert(StopWatchContract.ProcessEntry.TABLE_NAME, null, cv);
        } else {
            mDb.update(StopWatchContract.ProcessEntry.TABLE_NAME, cv,
                    StopWatchContract.ProcessEntry._ID + " = ?",
                    new String[]{String.valueOf(process.getId())});
        }
        ((NavigationFragment) fragment).updateView();
    }

    @Override
    public void onProcessDialogNegativeCheck(DialogFragment dialog) {
        ((NavigationFragment) fragment).updateView();
    }

    @Override
    public void onMachineDialogPositiveCheck(DialogFragment dialog, Machine machine) {
        int newOrderNumber = machine == null ? Utility.getMaxOrderNumberOfMachine(mDb) + 1 : machine.getOrderNumber();
        String newMachineName = ((DialogMachineFragment) dialog).getMachineName().getText().toString();
        boolean newMachineEnable = ((DialogMachineFragment) dialog).getEnable().isChecked();
        if ("".equals(newMachineName)) {
            Toast.makeText(getApplicationContext(), R.string.dialog_no_data, Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER, newOrderNumber);
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME, newMachineName);
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE, newMachineEnable);
        cv.put(StopWatchContract.MachineEntry.COLUMN_PARENT_PROCESS_ID, 0);
        if (machine == null) {
            mDb.insert(StopWatchContract.MachineEntry.TABLE_NAME, null, cv);
        } else {
            mDb.update(StopWatchContract.MachineEntry.TABLE_NAME, cv,
                    StopWatchContract.MachineEntry._ID + " = ?",
                    new String[]{String.valueOf(machine.getId())});
        }
        ((NavigationFragment) fragment).updateView();
    }

    @Override
    public void onMachineDialogNegativeCheck(DialogFragment dialog) {
        ((NavigationFragment) fragment).updateView();
    }

    @Override
    public void onPartDialogPositiveCheck(DialogFragment dialog, Part part) {
        int newOrderNumber = part == null ? Utility.getMaxOrderNumberOfPart(mDb) + 1 : part.getOrderNumber();
        String newPartName = ((DialogPartFragment) dialog).getPartName().getText().toString();
        boolean newPartEnable = ((DialogPartFragment) dialog).getEnable().isChecked();
        if ("".equals(newPartName)) {
            Toast.makeText(getApplicationContext(), R.string.dialog_no_data, Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER, newOrderNumber);
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_NAME, newPartName);
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE, newPartEnable);
        if (part == null) {
            mDb.insert(StopWatchContract.PartsEntry.TABLE_NAME, null, cv);
        } else {
            mDb.update(StopWatchContract.PartsEntry.TABLE_NAME, cv,
                    StopWatchContract.PartsEntry._ID + " = ?",
                    new String[]{String.valueOf(part.getId())});
        }
        ((NavigationFragment) fragment).updateView();
    }

    @Override
    public void onPartDialogNegativeCheck(DialogFragment dialog) {
        ((NavigationFragment) fragment).updateView();
    }

    /*

    ActionMode mActionMode;
    Employee longClickEmployeeSelected;
    Process longClickProcessSelected;
    Machine longClickMachineSelected;
    Part longClickPartSelected;

    ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu_employee, menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (fragment == null) {
                return false;
            }
            switch (item.getItemId()) {
                case R.id.context_edit_employee:
                    if (fragment.getClass() == EmployeesFragment.class && longClickEmployeeSelected != null) {
                        DialogEmployeeFragment dialogEmployeeFragment = new DialogEmployeeFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("longClickEmployeeSelected", longClickEmployeeSelected);
                        dialogEmployeeFragment.setArguments(bundle);
                        dialogEmployeeFragment.show(fragMan, "dialogEmployeeFragment");
                    }
                    else if (fragment.getClass() == ProcessesFragment.class && longClickProcessSelected != null) {
                        DialogProcessFragment dialogProcessFragment = new DialogProcessFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("longClickProcessSelected", longClickProcessSelected);
                        dialogProcessFragment.setArguments(bundle);
                        dialogProcessFragment.show(fragMan, "dialogProcessFragment");
                    }
                    else if (fragment.getClass() == MachinesFragment.class && longClickMachineSelected != null) {
                        DialogMachineFragment dialogMachineFragment = new DialogMachineFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("longClickMachineSelected", longClickMachineSelected);
                        dialogMachineFragment.setArguments(bundle);
                        dialogMachineFragment.show(fragMan, "dialogMachineFragment");
                    }
                    else if (fragment.getClass() == PartsFragment.class && longClickPartSelected != null) {
                        DialogPartFragment dialogPartFragment = new DialogPartFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("longClickPartSelected", longClickPartSelected);
                        dialogPartFragment.setArguments(bundle);
                        dialogPartFragment.show(fragMan, "dialogPartFragment");
                    }
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((NavigationFragment) fragment).updateView();
            longClickEmployeeSelected = null;
            longClickProcessSelected = null;
            longClickMachineSelected = null;
            longClickPartSelected = null;
        }
    };

    */

/*    void updateRowinDatabase(final int id) {
        if (fragment.getClass() == EmployeesFragment.class) {

        }
        else if (fragment.getClass() == ProcessesFragment.class) {
            Process process = ((ProcessesFragment)fragment).getProcessById(id);
            DialogProcessFragment dialogProcessFragment = new DialogProcessFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("longClickProcessSelected", process);
            dialogProcessFragment.setArguments(bundle);
            dialogProcessFragment.show(fragMan, "dialogProcessFragment");
        }
        else if (fragment.getClass() == MachinesFragment.class) {

        }
        else if (fragment.getClass() == PartsFragment.class) {

        }
    }*/

    /**
     * Common method for delete from any table
     * Target fragment should implement NavigationFragment interface
     * @param id
     */
    void deleteRowFromDatabase(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this/*, R.style.DeleteDialog*/)
                .setMessage(getResources().getString(R.string.dialog_sure_delete))
                .setPositiveButton(R.string.dialog_delete_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDb.delete(((SavableToDatabase) fragment).getTableName(), ((SavableToDatabase) fragment).getRowIdFromDatabase() + " = " + id, null) < 0) {
                            Toast.makeText(getApplicationContext(), R.string.deleting_unsuccessful, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.deleting_successful, Toast.LENGTH_SHORT).show();
                            if (fragment.getClass() == EmployeesFragment.class && selectedEmployee != null && selectedEmployee.getId() == id) {
                                selectedEmployee = null;
                            }
                            if (fragment.getClass() == ProcessesFragment.class && selectedProcess != null && selectedProcess.getId() == id) {
                                selectedProcess = null;
                            }
                            if (fragment.getClass() == MachinesFragment.class && selectedMachine != null && selectedMachine.getId() == id) {
                                selectedMachine = null;
                            }
                            if (fragment.getClass() == PartsFragment.class && selectedPart != null && selectedPart.getId() == id) {
                                selectedPart = null;
                            }
                        }
                        ((NavigationFragment) fragment).updateView();
                    }
                })
                .setNegativeButton(R.string.dialog_delete_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((NavigationFragment) fragment).updateView();
                    }
                })
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.result_exists_data));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.result_no_data));
    }
}
