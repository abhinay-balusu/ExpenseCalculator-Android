package com.abhinaybalusu.hw2;

/**
 * Created by abhinaybalusu on 9/10/16.
 */

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Expense implements Parcelable, Comparable<Expense>{

    String expenseName;
    String expenseCategory;
    String expenseAmount;
    String dateOfExpense;
    Uri receiptImageUri;

    public Expense(String expenseName, String expenseCategory, String expenseAmount, String dateOfExpense, Uri receiptImageUri) {
        this.expenseName = expenseName;
        this.expenseCategory = expenseCategory;
        this.expenseAmount = expenseAmount;
        this.dateOfExpense = dateOfExpense;
        this.receiptImageUri = receiptImageUri;
    }


    protected Expense(Parcel in) {
        expenseName = in.readString();
        expenseCategory = in.readString();
        expenseAmount = in.readString();
        dateOfExpense = in.readString();
        receiptImageUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getDateOfExpense() {
        return dateOfExpense;
    }

    public void setDateOfExpense(String dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
    }

    public Uri getReceiptImageUri() {
        return receiptImageUri;
    }

    public void setReceiptImageUri(Uri receiptImageUri) {
        this.receiptImageUri = receiptImageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expenseName);
        dest.writeString(expenseCategory);
        dest.writeString(expenseAmount);
        dest.writeString(dateOfExpense);
        dest.writeParcelable(receiptImageUri, flags);
    }


    @Override
    public int compareTo(Expense another) {
        if(this.expenseName.compareToIgnoreCase(another.expenseName) >0)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
