package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends DataBaseManager implements AccountDAO {

    public PersistentAccountDAO(Context context){
        super(context);
    }

    @Override             //get the list of account numbers
    public List<String> getAccountNumbersList() {

        List<String> AccountNoList = new ArrayList<>();
        //queryForGEtAccNo is a query which use to get the account no.from the table
        String queryForGetAccNo = "SELECT " + AccountNo + " FROM ACCOUNT_TABLE ; ";
        //to obtain an instance of SQLiteDatabase for operating the database
        SQLiteDatabase sqLiteDB = this.getReadableDatabase();
        //Cursors are user-defined iterative variables that allow us to access the query results

        Cursor c = sqLiteDB.rawQuery(queryForGetAccNo, null);

        if (c.moveToFirst()){
            do{
                String accNo = c.getString(0);
                AccountNoList.add(accNo);
            }while (c.moveToNext());
        }
        c.close();
        sqLiteDB.close();
        return AccountNoList;
    }
    @Override        //Get the account given the account number.
    public List<Account> getAccountsList(){

        List<Account> AccountDetailsList = new ArrayList<>();
        String queryForAccounts = "SELECT * FROM ACCOUNT_TABLE ;";
        SQLiteDatabase sqLiteDB = this.getReadableDatabase();
        //this use to iterate
        Cursor c = sqLiteDB.rawQuery(queryForAccounts, null);

        if (c.moveToFirst()){
            do{
                Account accountDetails = new Account(c.getString(0),c.getString(1),c.getString(2),c.getDouble(3));
                AccountDetailsList.add(accountDetails);

            }while (c.moveToNext());
        }

        c.close();
        sqLiteDB.close();
        return AccountDetailsList;
    }

    @Override     //Get the account given the account number.
    public Account getAccount(String accountNo) throws InvalidAccountException{
        //query for selecting the account which provides
        String queryForAccount = "SELECT * FROM  ACCOUNT_TABLE WHERE " + AccountNo + " = " + accountNo+ ";";
        SQLiteDatabase sqLiteDB = this.getReadableDatabase();
        Cursor c = sqLiteDB.rawQuery(queryForAccount, null);

        if (c.moveToFirst()){
            Account account = new Account(accountNo,c.getString(1),c.getString(2),c.getDouble(3));
            c.close();
            sqLiteDB.close();
            return account;
        }
        else{
            String ErrorMsg = "Account number" + accountNo + " is invalid.";
            throw new InvalidAccountException(ErrorMsg);
        }

    }

    @Override   //Add an account to the accounts collection.
    public void addAccount(Account account){

        //obtaining an instance to write in the DataBase
        SQLiteDatabase sqLiteDB = this.getWritableDatabase();
        //ContentValues means a key/value store that inserts data into a row of a table
        ContentValues contentValues = new ContentValues();
        //storing the values in contentValues
        contentValues.put(AccountNo, account.getAccountNo());
        contentValues.put(BankName, account.getBankName());
        contentValues.put(AccountHoldersName, account.getAccountHolderName());
        contentValues.put(BankBalance, account.getBalance());

        sqLiteDB.insert("ACCOUNT_TABLE", null, contentValues);

        sqLiteDB.close();
    }

    @Override  //Remove an account from the accounts collection.
    public void removeAccount(String accountNo) throws InvalidAccountException{

        //get an instant to edit the database
        SQLiteDatabase sqLiteDB = this.getWritableDatabase();
        //query for delete an account
        String queryForDelete = "DELETE FROM ACCOUNT_TABLE  WHERE " + AccountNo + " = " + accountNo + ";";
        Cursor c =sqLiteDB.rawQuery(queryForDelete,null);
        if(!c.moveToFirst()) {
            String ErrorMsg = "Account Number " + accountNo + " is invalid.";
            throw new InvalidAccountException(ErrorMsg);
        }
        c.close();
        sqLiteDB.execSQL(queryForDelete);  //this will delete the account details


    }
    //Update the balance of the given account. The type of the expense is specified in order to determine which action to be performed.
    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException{
        //this make an instance to read the database
        SQLiteDatabase sqLiteDB = this.getReadableDatabase();
        //this query gives the bank balance of the account holder
        String queryForBalance = "SELECT " + BankBalance + " FROM ACCOUNT_TABLE WHERE " + AccountNo + " = " + accountNo+";";

        Cursor c = sqLiteDB.rawQuery(queryForBalance, null);
        //.getColumnIndexOrThrow-Returns the zero-based index for the given column name, or throws IllegalArgumentException if the column doesn't exist.
        double balance = c.getDouble(c.getColumnIndexOrThrow(BankBalance));

        if(!c.moveToFirst()){
            String ErrorMsg = "Account Number " + accountNo + " is invalid.";
            throw new InvalidAccountException(ErrorMsg);
        }
        //update the bank balance
        switch (expenseType) {
            case EXPENSE:
                balance = balance - amount;
                break;
            case INCOME:
                balance = balance + amount;
                break;
        }
        c.close();
        sqLiteDB.close();

        // this gives an instant to write in the dataBase
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String queryForUpdate = "UPDATE ACCOUNT_TABLE SET BANK_BALANCE" + balance + ";";
        sqLiteDatabase.execSQL(queryForUpdate);
        sqLiteDatabase.close();

    }





}
