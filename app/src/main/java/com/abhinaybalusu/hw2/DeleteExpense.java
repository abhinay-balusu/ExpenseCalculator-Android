package com.abhinaybalusu.hw2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class DeleteExpense extends AppCompatActivity {

    private ArrayList expensesList_Delete;
    private EditText expenseNameEditText_Delete;
    private EditText expenseAmountEditText_Delete;
    private EditText expenseDateEditText_Delete;
    private Expense expense_Delete;
    private String categoryValue = "";
    private Spinner categorySpinner;
    private ImageView receiptImageView_Delete;
    private Uri gallaryImageURI_Delete = Uri.EMPTY;
    private int expenseIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expense);

        expensesList_Delete = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_LIST_KEY);

        expenseNameEditText_Delete = (EditText)findViewById(R.id.nameEditText_Delete);
        expenseNameEditText_Delete.setEnabled(false);

        expenseAmountEditText_Delete = (EditText)findViewById(R.id.amountEditText_Delete);
        expenseAmountEditText_Delete.setEnabled(false);

        expenseDateEditText_Delete = (EditText)findViewById(R.id.dateEditText_Delete);
        expenseDateEditText_Delete.setEnabled(false);

        categorySpinner = (Spinner)findViewById(R.id.categorySpinner_Delete);
        categorySpinner.setEnabled(false);

        receiptImageView_Delete = (ImageView)findViewById(R.id.receiptImageView_Delete);

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

        findViewById(R.id.selectExpenseButton_Delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expense_Delete = null;

                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteExpense.this);
                builder.setTitle("Pick An Expense");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        DeleteExpense.this,
                        android.R.layout.select_dialog_item);

                for(int i=0;i<expensesList_Delete.size();i++)
                {
                    expense_Delete = (Expense)expensesList_Delete.get(i);
                    arrayAdapter.add(expense_Delete.getExpenseName());
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
                                expense_Delete = (Expense)expensesList_Delete.get(which);

                                expenseNameEditText_Delete.setText(expense_Delete.getExpenseName());
                                expenseAmountEditText_Delete.setText(expense_Delete.getExpenseAmount());
                                expenseDateEditText_Delete.setText(expense_Delete.getDateOfExpense());

                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DeleteExpense.this, R.array.category_array, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                categorySpinner.setAdapter(adapter);
                                if (!expense_Delete.getExpenseCategory().equals(null)) {
                                    int spinnerPosition = adapter.getPosition(expense_Delete.getExpenseCategory());
                                    categorySpinner.setSelection(spinnerPosition);
                                }

                                receiptImageView_Delete.setImageResource(0);
                                Bitmap imageBitmap = null;
                                if(expense_Delete.getReceiptImageUri() != Uri.EMPTY)
                                {
                                    try {
                                        imageBitmap = MediaStore.Images.Media.getBitmap(DeleteExpense.this.getContentResolver(),expense_Delete.getReceiptImageUri());
                                        receiptImageView_Delete.setImageBitmap(imageBitmap);
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

        findViewById(R.id.cancelButton_Delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        findViewById(R.id.deleteButton_Delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!expenseNameEditText_Delete.getText().toString().isEmpty() || !expenseAmountEditText_Delete.getText().toString().isEmpty() || !expenseDateEditText_Delete.getText().toString().isEmpty()) {

                    Expense updatedExpense = (Expense) expensesList_Delete.get(expenseIndex);

                    expensesList_Delete.remove(expenseIndex);

                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.EXPENSE_LIST_KEY,expensesList_Delete);
                    setResult(RESULT_OK,intent);

                    finish();
                }
                else
                {
                    new AlertDialog.Builder(DeleteExpense.this)
                            .setTitle("Alert")
                            .setMessage("There is no Data to Delete")
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
