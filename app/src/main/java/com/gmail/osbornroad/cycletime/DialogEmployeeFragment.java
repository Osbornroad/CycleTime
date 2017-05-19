package com.gmail.osbornroad.cycletime;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by User on 19.05.2017.
 */

public class DialogEmployeeFragment extends DialogFragment {

    private EditText employeeName;

    public interface DialogEmployeeListener {
        public void onDialogPositiveCheck(DialogFragment dialog);
        public void onDialogNegativeCheck(DialogFragment dialog);
    }

    DialogEmployeeListener mListener;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_employee_add, null);

        employeeName = (EditText) rootView.findViewById(R.id.edit_text_add_employee);

        builder.setView(rootView)
                .setTitle(R.string.add_employee)
                .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveCheck(DialogEmployeeFragment.this);
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeCheck(DialogEmployeeFragment.this);
                    }
                })
                .create();


        return builder.create();
    }
}
