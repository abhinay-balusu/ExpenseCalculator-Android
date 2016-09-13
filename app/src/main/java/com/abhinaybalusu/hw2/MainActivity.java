package com.abhinaybalusu.hw2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Collections;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList expensesList;
    public static final String EXPENSE_LIST_KEY = "expenseList";
    public static final String EXPENSE_KEY = "expense";
    public static final int REQUEST_EXPENSE = 100;
    public static final int EDIT_EXPENSE = 102;
    public static final int DELETE_EXPENSE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        expensesList = new ArrayList<Expense>();

        Button addExpenseButton = (Button)findViewById(R.id.addExpenseButton);
        Button editExpenseButton = (Button)findViewById(R.id.editExpenseButton);
        Button deleteExpenseButton = (Button)findViewById(R.id.deleteExpenseButton);
        Button showExpensesButton = (Button)findViewById(R.id.showExpensesButton);
        Button finishButton = (Button)findViewById(R.id.finishButton);

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddExpense.class);
                startActivityForResult(intent,REQUEST_EXPENSE);

            }
        });
        editExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!expensesList.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, EditExpense.class);
                    intent.putExtra(EXPENSE_LIST_KEY, expensesList);
                    startActivityForResult(intent, EDIT_EXPENSE);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Expenses to Edit",Toast.LENGTH_SHORT).show();
                }

            }
        });
        deleteExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!expensesList.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, DeleteExpense.class);
                    intent.putExtra(EXPENSE_LIST_KEY, expensesList);
                    startActivityForResult(intent, DELETE_EXPENSE);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Expenses to Delete",Toast.LENGTH_SHORT).show();
                }

            }
        });
        showExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!expensesList.isEmpty())
                {
                    Intent intent = new Intent(MainActivity.this, ShowExpense.class);
                    intent.putExtra(EXPENSE_LIST_KEY,expensesList);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Expenses to Show",Toast.LENGTH_SHORT).show();
                }

            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_EXPENSE)
        {
            if(resultCode == RESULT_OK)
            {
                Expense new_expense =  data.getExtras().getParcelable(MainActivity.EXPENSE_KEY);

                expensesList.add(new_expense);

                Collections.sort(expensesList);

                Toast.makeText(getApplicationContext(),"Expense Added Successfully",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Adding Expense Failed",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == EDIT_EXPENSE)
        {
            if(resultCode == RESULT_OK)
            {

                ArrayList expensesList_new = (ArrayList<Expense>) data.getExtras().getSerializable(EXPENSE_LIST_KEY);
                expensesList = expensesList_new;
                Collections.sort(expensesList);
                Toast.makeText(getApplicationContext(),"Expense Updated Successfully",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Updating Expense Failed",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == DELETE_EXPENSE)
        {
            if(resultCode == RESULT_OK)
            {

                ArrayList expensesList_new = (ArrayList<Expense>) data.getExtras().getSerializable(EXPENSE_LIST_KEY);
                expensesList = expensesList_new;
                Collections.sort(expensesList);
                Toast.makeText(getApplicationContext(),"Expense Deleted Successfully",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Delete Expense Failed",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
