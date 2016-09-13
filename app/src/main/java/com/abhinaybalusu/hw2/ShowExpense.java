package com.abhinaybalusu.hw2;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class ShowExpense extends AppCompatActivity {

    private ArrayList expensesList_Show;
    private Expense expense_Show;
    private TextView expenseNameValueTextView_Show;
    private TextView expenseCategoryValueTextView_Show;
    private TextView expenseAmountValueTextView_Show;
    private TextView expenseDateValueTextView_Show;
    private ImageView receiptImageView;
    private ListIterator iterator;
    private int expenseIterator = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        expensesList_Show = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_LIST_KEY);

        expense_Show =(Expense) expensesList_Show.get(0);

        expenseNameValueTextView_Show = (TextView)findViewById(R.id.expenseNameValueTextView_Show);
        expenseCategoryValueTextView_Show = (TextView)findViewById(R.id.expenseCategoryValueTextView_Show);
        expenseAmountValueTextView_Show = (TextView)findViewById(R.id.expenseAmountValueTextView_Show);
        expenseDateValueTextView_Show = (TextView)findViewById(R.id.expenseDateValueTextView_Show);

        receiptImageView = (ImageView)findViewById(R.id.receiptImageView_Show);

        if(expense_Show != null)
        {
            showExpenseDetails(expense_Show);
        }

        iterator = expensesList_Show.listIterator();


        findViewById(R.id.showFirstExpenseIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseIterator = 0;
                expense_Show =(Expense) expensesList_Show.get(0);
                showExpenseDetails(expense_Show);

            }
        });

        findViewById(R.id.showPreviousExpenseIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseIterator--;
                if(expenseIterator >=0) {

                    expense_Show =(Expense)expensesList_Show.get(expenseIterator);
                    showExpenseDetails(expense_Show);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No More Expenses to show",Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.showNextExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseIterator++;
                if(expenseIterator < expensesList_Show.size()) {

                    expense_Show =(Expense) expensesList_Show.get(expenseIterator);
                    showExpenseDetails(expense_Show);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No More Expenses to show",Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.showLastExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseIterator = expensesList_Show.size()-1;
                expense_Show =(Expense) expensesList_Show.get(expensesList_Show.size()-1);
                showExpenseDetails(expense_Show);

            }
        });

        findViewById(R.id.finishButton_Show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public void showExpenseDetails(Expense expense)
    {
        receiptImageView.setImageDrawable(null);
        expenseNameValueTextView_Show.setText(expense.getExpenseName());
        expenseCategoryValueTextView_Show.setText(expense.getExpenseCategory());
        expenseAmountValueTextView_Show.setText("$ "+expense.getExpenseAmount());
        expenseDateValueTextView_Show.setText(expense.getDateOfExpense());

        Uri imageUri = expense.getReceiptImageUri();

        Bitmap imageBitmap = null;
        if(expense.getReceiptImageUri() != Uri.EMPTY)
        {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),expense.getReceiptImageUri());
                receiptImageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No Receipt for this Expense",Toast.LENGTH_SHORT).show();
        }


    }
}
