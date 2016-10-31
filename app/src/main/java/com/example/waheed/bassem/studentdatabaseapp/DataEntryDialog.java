package com.example.waheed.bassem.studentdatabaseapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * this class will handle user data entry
 * note that there will be one dialog class so I will modify the views visibility to match several dialogs
 */

public class DataEntryDialog extends DialogFragment {

    public static final int CREATE_DIALOG = 1;
    public static final int READ_DIALOG = 2;
    public static final int UPDATE_DIALOG = 3;
    public static final int DELETE_DIALOG = 4;

    private int mDialogType;
    private AlertDialog.Builder mBuilder;
    private LayoutInflater mInflater;
    private View mDialogView;
    private ArrayAdapter<CharSequence> mAcademicYearSpinnerAdapter;
    private ArrayAdapter<CharSequence> mGradeSpinnerAdapter;


    private EditText mNewStudentNameEditText;
    private Spinner mStudentGradeSpinner;
    private Spinner mStudentAcademicYearSpinner;

    private EditText mExistingStudentNameEditText;

    private CheckBox mReadAllCheckBox;

    private EditText mNameToDeleteEditText;
    private CheckBox mDeleteAll;

    private TextView mDialogTitleCRU;
    private TextView mDialogTitleD;

    private LinearLayout mNewNameLinearLayout;
    private LinearLayout mExistingNameLinearLayout;
    private LinearLayout mGradeLinearLayout;
    private LinearLayout mAcademicYearLinearLayout;

    private DialogCallback mDialogCallback;




    public DataEntryDialog () {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mBuilder = new AlertDialog.Builder(getActivity());
        mInflater = getActivity().getLayoutInflater();

        // building the dialog as per the selected dialog type
        buildAndInflateDialog();

        // adjusting the dialog to match the selected type
        adjustDialog();

        // adjust the positive and negative buttons and their listeners
        adjustPositiveAndNegativeButtons();

        return mBuilder.create();
    }

    /**
     * setting the dialog type
     * @param dialogType = enter the dialog type (create, read, update or delete)
     */
    public void setDialogType (int dialogType) {
        mDialogType = dialogType;
    }

    public void setDialogCallback (DialogCallback dialogCallback) {
        mDialogCallback = dialogCallback;
    }

    /**
     * build the dialog as per the selected dialog type @int mDialogType
     */
    public void buildAndInflateDialog () {
        switch (mDialogType) {
            case CREATE_DIALOG:
            case READ_DIALOG:
            case UPDATE_DIALOG:
                mDialogView = mInflater.inflate(R.layout.create_read_update_dialog, null);
                mBuilder.setView(mDialogView);
                mNewStudentNameEditText = (EditText) mDialogView.findViewById(R.id.create_name_edittext);
                mExistingStudentNameEditText = (EditText) mDialogView.findViewById(R.id.read_name_edittext);
                mStudentAcademicYearSpinner = (Spinner) mDialogView.findViewById(R.id.create_academicyear_spinner);
                mStudentGradeSpinner = (Spinner) mDialogView.findViewById(R.id.create_grade_spinner);
                mNewNameLinearLayout = (LinearLayout) mDialogView.findViewById(R.id.new_name);
                mExistingNameLinearLayout = (LinearLayout) mDialogView.findViewById(R.id.existing_name);
                mGradeLinearLayout = (LinearLayout) mDialogView.findViewById(R.id.new_grade);
                mAcademicYearLinearLayout = (LinearLayout) mDialogView.findViewById(R.id.new_academic_year);
                mReadAllCheckBox = (CheckBox) mDialogView.findViewById(R.id.read_all);
                mDialogTitleCRU = (TextView) mDialogView.findViewById(R.id.dialog_title_for_CRU);
                break;
            case DELETE_DIALOG:
                mDialogView = mInflater.inflate(R.layout.delete_dialog, null);
                mBuilder.setView(mDialogView);
                mNameToDeleteEditText = (EditText) mDialogView.findViewById(R.id.name_to_be_deleted_edit_text);
                mDeleteAll = (CheckBox) mDialogView.findViewById(R.id.delete_all);
                mDialogTitleD = (TextView) mDialogView.findViewById(R.id.dialog_title_for_delete);
                break;
            default:
        }
    }

    /**
     * adjust the dialog to be create, read, update or delete dialog
     */
    private void adjustDialog () {
        switch (mDialogType) {
            case CREATE_DIALOG:
                adjustCreateDialogVisibility();
                adjustSpinners();
                break;
            case READ_DIALOG:
                adjustReadDialogVisibility();
                break;
            case UPDATE_DIALOG:
                adjustUpdateDialogVisibility();
                adjustSpinners();
                break;
            case DELETE_DIALOG:
                adjustDeleteDialogVisibility();
                break;
        }
    }

    /**
     * adjusting the dialog to be create dialog by adjusting view visibility
     */
    private void adjustCreateDialogVisibility() {
        mExistingNameLinearLayout.setVisibility(View.GONE);
        mReadAllCheckBox.setVisibility(View.GONE);
        mDialogTitleCRU.setText("Create");
    }

    /**
     * adjust the dialog to be read dialog by adjusting views visibility
     */
    private void adjustReadDialogVisibility() {
        mNewNameLinearLayout.setVisibility(View.GONE);
        mGradeLinearLayout.setVisibility(View.GONE);
        mAcademicYearLinearLayout.setVisibility(View.GONE);
        mDialogTitleCRU.setText("Read");

        mReadAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mExistingStudentNameEditText.setEnabled(false);
                } else {
                    mExistingStudentNameEditText.setEnabled(true);
                }
            }
        });
    }

    private void adjustSpinners () {
        mAcademicYearSpinnerAdapter =  ArrayAdapter.createFromResource(getContext(),
                R.array.academic_years, android.R.layout.simple_spinner_item);
        mAcademicYearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStudentAcademicYearSpinner.setAdapter(mAcademicYearSpinnerAdapter);

        mGradeSpinnerAdapter =  ArrayAdapter.createFromResource(getContext(),
                R.array.grades, android.R.layout.simple_spinner_item);
        mGradeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStudentGradeSpinner.setAdapter(mGradeSpinnerAdapter);
    }

    private void adjustUpdateDialogVisibility () {
        mReadAllCheckBox.setVisibility(View.GONE);
        mDialogTitleCRU.setText("Update");
    }

    private void adjustDeleteDialogVisibility () {
        mDialogTitleD.setText("Delete");
        mDeleteAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mNameToDeleteEditText.setEnabled(false);
                } else {
                    mNameToDeleteEditText.setEnabled(true);
                }
            }
        });
    }

    private void adjustPositiveAndNegativeButtons() {
        mBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialogCallback.enableButtons();
                String newName;
                String newAcademicYear;
                String newGrade;
                String existingName;
                String NameToBeDeleted;
                switch (mDialogType) {
                    case CREATE_DIALOG:
                        newName = mNewStudentNameEditText.getText().toString();
                        newAcademicYear = (String)mStudentAcademicYearSpinner.getSelectedItem();
                        newGrade = (String)mStudentGradeSpinner.getSelectedItem();
                        mDialogCallback.createItem(newName, newAcademicYear, newGrade);
                        break;
                    case READ_DIALOG:
                        if (mReadAllCheckBox.isChecked()) {
                            mDialogCallback.readAllItems();
                        } else {
                            existingName = mExistingStudentNameEditText.getText().toString();
                            mDialogCallback.readItem(existingName);
                        }
                        break;
                    case UPDATE_DIALOG:
                        existingName = mExistingStudentNameEditText.getText().toString();
                        newName = mNewStudentNameEditText.getText().toString();
                        newAcademicYear = (String)mStudentAcademicYearSpinner.getSelectedItem();
                        newGrade = (String)mStudentGradeSpinner.getSelectedItem();
                        mDialogCallback.updateItem(existingName, newName, newAcademicYear, newGrade);
                        break;
                    case DELETE_DIALOG:
                        if (mDeleteAll.isChecked()) {
                            mDialogCallback.deleteTable();
                        } else {
                            NameToBeDeleted = mNameToDeleteEditText.getText().toString();
                            mDialogCallback.deleteItem(NameToBeDeleted);
                        }
                        break;
                }
            }
        });
        mBuilder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialogCallback.enableButtons();
                DataEntryDialog.this.getDialog().cancel();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mDialogCallback.enableButtons();
    }
}
