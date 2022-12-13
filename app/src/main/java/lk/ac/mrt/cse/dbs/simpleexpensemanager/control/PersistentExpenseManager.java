package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    private final Context context;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context = context;
        setup();
    }
    @Override
    public void setup() throws ExpenseManagerException {
        try{
            AccountDAO persistentAccountDAO = new PersistentAccountDAO(this.context);
            setAccountsDAO(persistentAccountDAO);

            TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(this.context);
            setTransactionsDAO(persistentTransactionDAO);

        }
        catch(Exception e){
            String msg="Invalided!";
            throw new ExpenseManagerException(msg)  ;

        }

    }

}
