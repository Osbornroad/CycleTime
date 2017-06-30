package com.gmail.osbornroad.cycletime;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gmail.osbornroad.cycletime.model.Employee;

/**
 * Created by User on 19.05.2017.
 */

public class DialogEmployeeFragment extends DialogFragment {

    private EditText employeeName;
    private android.support.v7.widget.SwitchCompat enable;
    private Employee employeeSelectedForEdit;

    public EditText getEmployeeName() {
        return employeeName;
    }

    public SwitchCompat getEnable() {
        return enable;
    }

    public interface DialogEmployeeListener {
        void onEmployeeDialogPositiveCheck(DialogFragment dialog, Employee employee);
        void onEmployeeDialogNegativeCheck(DialogFragment dialog);
    }

    private DialogEmployeeListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogEmployeeListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogEmployeeListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            employeeSelectedForEdit = bundle.getParcelable("employeeSelectedForEdit");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()/*, R.style.AddDialog*/);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_update, null);

        employeeName = (EditText) rootView.findViewById(R.id.edit_text_name_add_update);
        enable =(android.support.v7.widget.SwitchCompat) rootView.findViewById(R.id.switch_show_item);
        employeeName.setHint(R.string.hint_add_employee);

        if (employeeSelectedForEdit != null) {
            employeeName.setText(employeeSelectedForEdit.getEmployeeName());
            enable.setChecked(employeeSelectedForEdit.isEnable());
        }

        builder.setView(rootView)
                .setTitle(employeeSelectedForEdit == null ? R.string.add_employee : R.string.edit_employee)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onEmployeeDialogPositiveCheck(DialogEmployeeFragment.this, employeeSelectedForEdit);
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onEmployeeDialogNegativeCheck(DialogEmployeeFragment.this);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.result_exists_data));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.result_no_data));


        return dialog;
    }
}
