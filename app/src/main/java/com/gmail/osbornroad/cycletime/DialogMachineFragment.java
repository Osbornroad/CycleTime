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

import com.gmail.osbornroad.cycletime.model.Machine;

/**
 * Created by User on 25.05.2017.
 */
public class DialogMachineFragment extends DialogFragment {
    private EditText machineName;
    private android.support.v7.widget.SwitchCompat enable;
    private Machine machineSelectedForEdit;

    public EditText getMachineName() {
        return machineName;
    }

    public SwitchCompat getEnable() {
        return enable;
    }

    public interface DialogMachineListener {
        void onMachineDialogPositiveCheck(DialogFragment dialog, Machine machine);
        void onMachineDialogNegativeCheck(DialogFragment dialog);
    }

    private DialogMachineListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogMachineListener) activity;
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
            machineSelectedForEdit = bundle.getParcelable("machineSelectedForEdit");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()/*, R.style.AddDialog*/);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_update, null);

        machineName = (EditText) rootView.findViewById(R.id.edit_text_name_add_update);
        enable =(android.support.v7.widget.SwitchCompat) rootView.findViewById(R.id.switch_show_item);
        machineName.setHint(R.string.hint_add_machine);

        if (machineSelectedForEdit != null) {
            machineName.setText((machineSelectedForEdit.getMachineName()));
            enable.setChecked(machineSelectedForEdit.isEnable());

        }

        builder.setView(rootView)
                .setTitle(machineSelectedForEdit == null ? R.string.add_machine : R.string.edit_machine)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onMachineDialogPositiveCheck(DialogMachineFragment.this, machineSelectedForEdit);
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onMachineDialogNegativeCheck(DialogMachineFragment.this);
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
