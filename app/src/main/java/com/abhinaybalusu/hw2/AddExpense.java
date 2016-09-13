package com.abhinaybalusu.hw2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.net.Uri;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import java.io.IOException;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import java.util.Calendar;

public class AddExpense extends AppCompatActivity {

    private EditText expenseNameEditText;
    private String categoryValue;
    private EditText amountEditText;
    private Calendar dateCalendar;
    private EditText dateEditText;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    public static final int CAMERA_REQUEST = 101;
    private ImageView receiptImageView;
    private Uri gallaryImageURI = Uri.EMPTY;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                gallaryImageURI = data.getData();

                //Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                //receiptImageView.setImageBitmap(imageBitmap);

                Bitmap imageBitmap;

                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),gallaryImageURI);
                    receiptImageView.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseNameEditText = (EditText)findViewById(R.id.nameEditText);

        Spinner categorySpinner = (Spinner)findViewById(R.id.categorySpinner);

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

        amountEditText = (EditText)findViewById(R.id.amountEditText);

        dateEditText = (EditText)findViewById(R.id.dateEditText);
        dateEditText.setEnabled(false);
        dateCalendar = Calendar.getInstance();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                dateEditText.setText((monthOfYear+1)+"/"+dayOfMonth+"/"+year);

            }
        };

        ImageView calendarImageView = (ImageView)findViewById(R.id.calendarImageView);
        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddExpense.this,dateSetListener,dateCalendar.get(Calendar.YEAR),dateCalendar.get(Calendar.MONTH),dateCalendar.get(Calendar.DATE)).show();

            }
        });

        receiptImageView = (ImageView)findViewById(R.id.receiptImageView);
        receiptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent camIntent = new Intent(Intent.ACTION_GET_CONTENT);
                Intent camIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                camIntent.setType("image/*");
                startActivityForResult(camIntent,CAMERA_REQUEST);
            }
        });

        Button addAnExpenseButton = (Button)findViewById(R.id.addAnExpenseButton);
        assert addAnExpenseButton != null;
        addAnExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String expenseName = expenseNameEditText.getText().toString();
                String expenseAmount = amountEditText.getText().toString();
                String date = dateEditText.getText().toString();

                if(expenseName.equals("") || expenseAmount.equals("") || date.equals(""))
                {
                    new AlertDialog.Builder(AddExpense.this)
                            .setTitle("Alert")
                            .setMessage("Please Enter All the Details to Continue")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })

                            .show();
                }
                else if(Double.parseDouble(expenseAmount) == 0)
                {
                    new AlertDialog.Builder(AddExpense.this)
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
                    Expense expense = new Expense(expenseName,categoryValue,expenseAmount,date,gallaryImageURI);

                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.EXPENSE_KEY,expense);
                    setResult(RESULT_OK,intent);

                    finish();
                }


            }
        });



    }

}
