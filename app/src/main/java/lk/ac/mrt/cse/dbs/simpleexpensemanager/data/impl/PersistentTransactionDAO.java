package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO  extends DataBaseManager implements TransactionDAO{
    public PersistentTransactionDAO(@Nullable Context context) {super(context);}

    @Override    //Log the transaction requested by the user
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount){
        //obtaining an instant to write to the database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //this will stores the column values in the entry
        ContentValues contentValues = new ContentValues();
        //formatting the date to the correct format
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(date);

        contentValues.put(Date,formattedDate);
        contentValues.put(AccountNo,accountNo);
        contentValues.put(Expense_Type,String.valueOf(expenseType));
        contentValues.put(Amount,amount);

        sqLiteDatabase.insert("TRANSACTION_TABLE",null,contentValues);

        sqLiteDatabase.close();
    }

    @Override  //Return all the transactions logged
    public List<Transaction> getAllTransactionLogs(){

        List<Transaction> transactionLogList = new ArrayList<>();
        //this get an instant to read theDataBase
        SQLiteDatabase sqLiteDatabase =this.getReadableDatabase();
        //the query to take all the instruction from the database
        String queryForLogs= "SELECT * FROM TRANSACTION_TABLE ;";
        Cursor c =sqLiteDatabase.rawQuery(queryForLogs,null);
        if (c.moveToFirst()){
            do{
                //change the Name of the Expense name if it is in different formats
                String expense_type = c.getString(2);
                ExpenseType expenseType=ExpenseType.valueOf(expense_type.toUpperCase());
                //date of transaction happens

                String[] date = c.getString(1).split(",");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(date[0]),Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                Date transactionDate = calendar.getTime();

                Transaction transaction = new Transaction(transactionDate,c.getString(1), expenseType, c.getDouble(3));
                transactionLogList.add(transaction);

            }while (c.moveToNext());
        }
        c.close();
        sqLiteDatabase.close();
        return transactionLogList;
    }


    private List<Transaction> transactions;
    @Override  //Return a limited amount of transactions logged
    public List<Transaction> getPaginatedTransactionLogs(int limit){

        transactions = getAllTransactionLogs();
        int size = transactions.size();
        if(size<= limit) {
            return transactions;
        }
        return transactions.subList(size-limit, size);
    }




}
