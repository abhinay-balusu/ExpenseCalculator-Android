package com.abhinaybalusu.hw2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.DialogInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.support.v7.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class EditExpense extends AppCompatActivity {

    private ArrayList expensesList_Edit;
    private EditText expenseNameEditText_Edit;
    private EditText expenseAmountEditText_Edit;
    private EditText expenseDateEditText_Edit;
    private Expense expense_edit;
    private String categoryValue = "";
    private Spinner categorySpinner;
    private ImageView receiptImageView_Edit;
    private Calendar dateCalendar_Edit;
    private DatePickerDialog.OnDateSetListener dateSetListener_Edit;
    private Uri gallaryImageURI_Edit = Uri.EMPTY;
    private int expenseIndex = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == AddExpense.CAMERA_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                gallaryImageURI_Edit = data.getData();

                //Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                //receiptImageView.setImageBitmap(imageBitmap);

                Bitmap imageBitmap;

                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),gallaryImageURI_Edit);
                    receiptImageView_Edit.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        expensesList_Edit = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_LIST_KEY);

        expenseNameEditText_Edit = (EditText)findViewById(R.id.nameEditText_Edit);
        expenseAmountEditText_Edit = (EditText)findViewById(R.id.amountEditText_Edit);
        expenseDateEditText_Edit = (EditText)findViewById(R.id.dateEditText_Edit);
        expenseDateEditText_Edit.setEnabled(false);

        categorySpinner = (Spinner)findViewById(R.id.categorySpinner_Edit);

        receiptImageView_Edit = (ImageView)findViewById(R.id.receiptImageView_Edit);

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.category_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(staticAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                categoryValue = (String)parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.selectExpenseButton_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expense_edit = null;

                AlertDialog.Builder builder = new AlertDialog.Builder(EditExpense.this);
                builder.setTitle("Pick An Expense");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        EditExpense.this,
                        android.R.layout.select_dialog_item);

                for(int i=0;i<expensesList_Edit.size();i++)
                {
                    expense_edit = (Expense)expensesList_Edit.get(i);
                    arrayAdapter.add(expense_edit.getExpenseName());
                }

                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                expenseIndex = which;
                                expense_edit = (Expense)expensesList_Edit.get(which);

                                expenseNameEditText_Edit.setText(expense_edit.getExpenseName());
                                expenseAmountEditText_Edit.setText(expense_edit.getExpenseAmount());
                                expenseDateEditText_Edit.setText(expense_edit.getDateOfExpense());
                                gallaryImageURI_Edit = expense_edit.getReceiptImageUri();

                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditExpense.this, R.array.category_array, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                categorySpinner.setAdapter(adapter);
                                if (!expense_edit.getExpenseCategory().equals(null)) {
                                    int spinnerPosition = adapter.getPosition(expense_edit.getExpenseCategory());
                                    categorySpinner.setSelection(spinnerPosition);
                                }

                                receiptImageView_Edit.setImageResource(0);
                                Bitmap imageBitmap = null;
                                if(expense_edit.getReceiptImageUri() != Uri.EMPTY)
                                {
                                    try {
                                        imageBitmap = MediaStore.Images.Media.getBitmap(EditExpense.this.getContentResolver(),expense_edit.getReceiptImageUri());
                                        receiptImageView_Edit.setImageBitmap(imageBitmap);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        dateCalendar_Edit = Calendar.getInstance();

        dateSetListener_Edit = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                expenseDateEditText_Edit.setText((monthOfYear+1)+"/"+dayOfMonth+"/"+year);

            }
        };

        findViewById(R.id.calendarImageView_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditExpense.this,dateSetListener_Edit,dateCalendar_Edit.get(Calendar.YEAR),dateCalendar_Edit.get(Calendar.MONTH),dateCalendar_Edit.get(Calendar.DATE)).show();

            }
        });

        findViewById(R.id.receiptImageView_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent camIntent = new Intent(Intent.ACTION_GET_CONTENT);
                camIntent.setType("image/*");
                startActivityForResult(camIntent,AddExpense.CAMERA_REQUEST);

            }
        });

        findViewById(R.id.cancelButton_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        findViewById(R.id.saveButton_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!expenseNameEditText_Edit.getText().toString().isEmpty() || !expenseAmountEditText_Edit.getText().toString().isEmpty() || !expenseDateEditText_Edit.getText().toString().isEmpty()) {

                    Expense updatedExpense = (Expense) expensesList_Edit.get(expenseIndex);
                    updatedExpense.setExpenseName(expenseNameEditText_Edit.getText().toString());
                    updatedExpense.setExpenseCategory(categoryValue);
                    updatedExpense.setExpenseAmount(expenseAmountEditText_Edit.getText().toString());
                    updatedExpense.setDateOfExpense(expenseDateEditText_Edit.getText().toString());
                    updatedExpense.setReceiptImageUri(gallaryImageURI_Edit);

                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.EXPENSE_LIST_KEY,expensesList_Edit);
                    setResult(RESULT_OK,intent);

                    finish();
                }
                else if(Double.parseDouble(expenseAmountEditText_Edit.getText().toString()) == 0)
                {
                    new AlertDialog.Builder(EditExpense.this)
                            .setTitle("Alert")
                            .setMessage("Amount should be greater than zero")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })

                            .show();
                }
                else
                {
                    new AlertDialog.Builder(EditExpense.this)
                            .setTitle("Alert")
                            .setMessage("Please Enter All the Details to Continue")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })

                            .show();
                }

            }
        });


    }
}
