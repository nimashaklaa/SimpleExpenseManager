package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class DataBaseManager extends SQLiteOpenHelper{
    //for the ACCOUNT_TABLE

    public static final String AccountNo = "ACCOUNT_NUMBER";
    public static final String BankName = "BANK_NAME";
    public static final String AccountHoldersName ="ACCOUNT_HOLDER_NAME";
    public static final String BankBalance = "BANK_BALANCE";

    //for the TRANSACTION_TABLE
    public static final String TransactionID= "TRANSACTION_ID";
    public static final String Date="DATE";
    public static final String Expense_Type ="EXPENSE_TYPE";
    public static final String Amount = "AMOUNT";

    //for the first time we create a database
    public DataBaseManager(@Nullable Context context) {
        super(context, "200425K_ExpenseManager.db", null, 1);
    }
    @Override         //this activity creates first
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        try{     //creating the Account Table
            String CreateAccountTable= "CREATE TABLE  ACCOUNT_TABLE " + "("+ AccountNo +"TEXT PRIMARY KEY ,"+BankName+"TEXT,"+AccountHoldersName+"TEXT,"+BankBalance+"REAL);";
            sqLiteDatabase.execSQL(CreateAccountTable);

        }
        catch(Exception exception){
            System.out.println("ERROR ON CREATING THE ACCOUNT_TABLE");
        }

        try{
            String CreateTransactionTable="CREATE TABLE TRANSACTION_TABLE" +"("+ TransactionID+"INTEGER PRIMARY KEY AUTOINCREMENT ,"+ Date+"TEXT,"+Expense_Type+"TEXT,"+Amount+"REAL);";
            sqLiteDatabase.execSQL(CreateTransactionTable);

        }
        catch(Exception exception){
            System.out.println("ERROR IN CREATING THE TRANSACTION TABLE");
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
