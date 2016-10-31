package com.example.waheed.bassem.studentdatabaseapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogCallback{

    // declaring buttons and textviews
    private Button mCreateButton;
    private Button mReadButton;
    private Button mUpdateButton;
    private Button mDeleteButton;
    private Button mClearButton;
    private TextView mDisplayTextView;

    private DataEntryDialog mDataEntryDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActiveAndroid.initialize(this);

        // initializing variables
        mDataEntryDialog = new DataEntryDialog();
        mDataEntryDialog.setDialogCallback(this);

        // init the buttons
        mCreateButton = (Button) findViewById(R.id.create_button);
        mReadButton = (Button) findViewById(R.id.read_button);
        mUpdateButton = (Button) findViewById(R.id.update_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);
        mClearButton = (Button) findViewById(R.id.clear_button);

        // init textviews
        mDisplayTextView = (TextView) findViewById(R.id.display_textview);

        mCreateButton.setOnClickListener(this);
        mReadButton.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mClearButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.create_button:
                disableAllButtons();
                viewCreateItemDialog();
                break;
            case R.id.read_button:
                disableAllButtons();
                viewReadItemDialog();
                break;
            case R.id.update_button:
                disableAllButtons();
                viewUpdateItemDialog();
                break;
            case R.id.delete_button:
                disableAllButtons();
                viewDeleteDialog();
                break;
            case R.id.clear_button:
                clear();
                break;
        }
    }


    private void disableAllButtons () {
        mCreateButton.setEnabled(false);
        mReadButton.setEnabled(false);
        mUpdateButton.setEnabled(false);
        mDeleteButton.setEnabled(false);
        mClearButton.setEnabled(false);
    }

    private void enableAllButtons () {
        mCreateButton.setEnabled(true);
        mReadButton.setEnabled(true);
        mUpdateButton.setEnabled(true);
        mDeleteButton.setEnabled(true);
        mClearButton.setEnabled(true);
    }

    private void viewCreateItemDialog() {
        mDataEntryDialog.setDialogType(DataEntryDialog.CREATE_DIALOG);
        mDataEntryDialog.show(getSupportFragmentManager(), "create");
    }

    private void viewReadItemDialog() {
        mDataEntryDialog.setDialogType(DataEntryDialog.READ_DIALOG);
        mDataEntryDialog.show(getSupportFragmentManager(), "read");
    }

    private void viewUpdateItemDialog() {
        mDataEntryDialog.setDialogType(DataEntryDialog.UPDATE_DIALOG);
        mDataEntryDialog.show(getSupportFragmentManager(), "update");
    }

    private void viewDeleteDialog() {
        mDataEntryDialog.setDialogType(DataEntryDialog.DELETE_DIALOG);
        mDataEntryDialog.show(getSupportFragmentManager(), "delete");
    }

    private void clear () {
        mDisplayTextView.setText("");
    }

    @Override
    public void createItem(String name, String academicYear, String grade) {
        if (!name.isEmpty() && name != null) {
            StudentData studentData = new StudentData(name, academicYear, grade);
            Long result = studentData.save();

            if (result == -1) {
                Toast.makeText(this, "Failed to Add the new student", Toast.LENGTH_SHORT).show();
            } else {
                mDisplayTextView.setText("");
                mDisplayTextView.append(name + " "
                        + academicYear + " "
                        + grade + "\n");
                Toast.makeText(this, "Student named " + name + " was added successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void readItem(String name) {
        if (!name.isEmpty() && name != null) {
            StudentData studentData = new Select()
                    .from(StudentData.class)
                    .where("Name = ?", name)
                    .executeSingle();
            if (studentData != null) {
                mDisplayTextView.setText("");
                mDisplayTextView.append(studentData.getName() + " "
                        + studentData.getAcademicYear() + " "
                        + studentData.getGrade() + "\n");
            } else {
                Toast.makeText(this, "There is no student named: " + name, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void readAllItems() {
        mDisplayTextView.setText("");
        List<StudentData> studentDataList = new Select().from(StudentData.class).execute();
        StudentData studentDataItem;
        if (studentDataList != null) {
            for (int i = 0; i<studentDataList.size(); i++) {
                studentDataItem = studentDataList.get(i);
                mDisplayTextView.append(studentDataItem.getName() + " "
                        + studentDataItem.getAcademicYear() + " "
                        + studentDataItem.getGrade() + "\n");
            }
            if(studentDataList.size() == 0) {
                Toast.makeText(this, "Data base is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Sorry, something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateItem(String oldName, String newName, String newAcademicYear, String newGrade) {
        if (!oldName.isEmpty() && oldName != null && !newName.isEmpty() && newName != null) {
            StudentData studentData = new Select()
                    .from(StudentData.class)
                    .where("Name = ?", oldName)
                    .executeSingle();

            if (studentData != null) {
                studentData.setName(newName);
                studentData.setAcademicYear(newAcademicYear);
                studentData.setGrade(newGrade);
                Long result = studentData.save();

                if (result == -1) {
                    Toast.makeText(this, "Could not update the student data", Toast.LENGTH_SHORT).show();
                } else {
                    mDisplayTextView.setText("");
                    mDisplayTextView.append(studentData.getName() + " "
                            + studentData.getAcademicYear() + " "
                            + studentData.getGrade() + "\n");
                    Toast.makeText(this, newName + "'s data was updated successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "There is no student named: " + oldName, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void deleteItem(String name) {
        if (!name.isEmpty() && name != null) {
            try {
                mDisplayTextView.setText("");
                new Delete().from(StudentData.class).where("Name = ?", name).execute();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,
                        "Sorry, couldn't delete student " + name + " , something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteTable() {
        try {
            mDisplayTextView.setText("");
            new Delete().from(StudentData.class).execute();
            Toast.makeText(this, "the whole table is deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Sorry couldn't delete the data, something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void enableButtons() {
        enableAllButtons();
    }
}
