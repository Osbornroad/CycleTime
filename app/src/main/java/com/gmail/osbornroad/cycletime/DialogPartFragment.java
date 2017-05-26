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

import com.gmail.osbornroad.cycletime.model.Part;

/**
 * Created by User on 25.05.2017.
 */
public class DialogPartFragment extends DialogFragment {

    private EditText partName;
    private android.support.v7.widget.SwitchCompat enable;
    private Part longClickPartSelected;

    public EditText getPartName() {
        return partName;
    }

    public SwitchCompat getEnable() {
        return enable;
    }

    public interface DialogPartListener {
        void onPartDialogPositiveCheck(DialogFragment dialog, Part part);
    }

    private DialogPartListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogPartListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogPartListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            longClickPartSelected = bundle.getParcelable("longClickPartSelected");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()/*, R.style.AddDialog*/);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_update, null);

        partName = (EditText) rootView.findViewById(R.id.edit_text_name_add_update);
        enable =(android.support.v7.widget.SwitchCompat) rootView.findViewById(R.id.switch_show_item);

        if (longClickPartSelected != null) {
            partName.setText(longClickPartSelected.getPartName());
            enable.setChecked(longClickPartSelected.isEnable());
        }

        builder.setView(rootView)
                .setTitle(longClickPartSelected == null ? R.string.add_part : R.string.edit_part)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPartDialogPositiveCheck(DialogPartFragment.this, longClickPartSelected);
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
