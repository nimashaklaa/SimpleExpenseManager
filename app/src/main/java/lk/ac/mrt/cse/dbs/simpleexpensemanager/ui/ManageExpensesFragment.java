package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 *
 */
public class ManageExpensesFragment extends Fragment implements View.OnClickListener {
    private Button submitButton;
    private EditText amount;
    private Spinner accountSelector;
    private RadioGroup expenseTypeGroup;
    private RadioButton expenseType;
    private RadioButton incomeType;
    private DatePicker datePicker;
    private ExpenseManager currentExpenseManager;

    public static ManageExpensesFragment newInstance(ExpenseManager expenseManager) {
        ManageExpensesFragment manageExpensesFragment = new ManageExpensesFragment();
        Bundle args = new Bundle();
        args.putSerializable("expense-manager", expenseManager);
        manageExpensesFragment.setArguments(args);
        return manageExpensesFragment;
    }

    public ManageExpensesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_expenses, container, false);
        submitButton = (Button) rootView.findViewById(R.id.submit_amount);
        submitButton.setOnClickListener(this);

        amount = (EditText) rootView.findViewById(R.id.amount);
        accountSelector = (Spinner) rootView.findViewById(R.id.account_selector);
        currentExpenseManager = (ExpenseManager) getArguments().get("expense-manager");
        ArrayAdapter<String> adapter =
                null;
        if (currentExpenseManager != null) {
            adapter = new ArrayAdapter<>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item,
                    currentExpenseManager.getAccountNumbersList());
        }
        accountSelector.setAdapter(adapter);

        expenseTypeGroup = (RadioGroup) rootView.findViewById(R.id.expense_type_group);
        expenseType = (RadioButton) rootView.findViewById(R.id.expense);
        incomeType = (RadioButton) rootView.findViewById(R.id.income);
        datePicker = (DatePicker) rootView.findViewById(R.id.date_selector);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_amount:
                String selectedAccount = (String) accountSelector.getSelectedItem();
                String amountStr = amount.getText().toString();
                RadioButton checkedType = (RadioButton) getActivity().findViewById(expenseTypeGroup
                        .getCheckedRadioButtonId());
                String type = (String) checkedType.getText();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                if (amountStr.isEmpty()) {
                    amount.setError("Amount is required.");
                }

                if (currentExpenseManager != null) {
                    try {
                        currentExpenseManager.updateAccountBalance(selectedAccount, day, month, year,
                                ExpenseType.valueOf(type.toUpperCase()), amountStr);
                    } catch (InvalidAccountException e) {
                        new AlertDialog.Builder(this.getActivity())
                                .setTitle("Unable to update the account : " + selectedAccount)
                                .setMessage(e.getMessage())
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    }
                }
                amount.getText().clear();
                break;
        }
    }
}
