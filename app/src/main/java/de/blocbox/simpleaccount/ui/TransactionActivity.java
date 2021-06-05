package de.blocbox.simpleaccount.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.blocbox.simpleaccount.R;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;
import de.blocbox.simpleaccount.viewmodel.TransactionViewModel;

public class TransactionActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private final int ACCOUNT_UID_UNDEFINED = -1;
    private int accountUid = ACCOUNT_UID_UNDEFINED;
    private final int TRANSACTION_UID_UNDEFINED = -1;
    private int transactionUid = TRANSACTION_UID_UNDEFINED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        Intent intent = getIntent();
        accountUid = intent.getIntExtra("AccountEntity.uid", ACCOUNT_UID_UNDEFINED);
        transactionUid = intent.getIntExtra("TransactionEntity.uid", TRANSACTION_UID_UNDEFINED);

        transactionViewModel = new ViewModelProvider(this).get( TransactionViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }

        if(accountUid != ACCOUNT_UID_UNDEFINED)
        {
            setContentView( R.layout.activity_transaction );

            ((EditText) findViewById( R.id.editTextAmount )).addTextChangedListener( new TextWatcher() {
                CharSequence beforeChanged;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    beforeChanged = s.toString();
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    //char decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
                    //String regexp = "[-]?[0-9]{0,10}+(((\\.[0-9]{0,2})?)|(\\.)?)";
                    //String regexp = "[-]?[0-9]{0,10}+((([\\.,]{1}[0-9]{0,2})?)|([\\.,]{1})?)";
                    String regexp = "[-]?[0-9]{0,10}+(\\.[0-9]{0,2})?";
                    Matcher matcher = Pattern.compile(regexp).matcher( s.toString() );
                    if (!matcher.matches()) {
                        s.replace( 0, s.length(), beforeChanged );
                    }
                    invalidateOptionsMenu();
                }
            } );

            ((EditText) findViewById( R.id.editTextDescription )).setFilters( new InputFilter[] {
                    (source, start, end, dest, d_start, d_end) -> {
                        invalidateOptionsMenu();
                        return null;
                    }
            });

            if (transactionUid == TRANSACTION_UID_UNDEFINED)
            {
                getSupportActionBar().setTitle( "Add Transaction" );
            }else{
                getSupportActionBar().setTitle( "Update Transaction" );

                final Observer<TransactionEntity> transactionObserver = transactionEntity -> {
                    // Update the UI
                    EditText editTextAmount = findViewById( R.id.editTextAmount);
                    EditText editTextDescription = findViewById( R.id.editTextDescription);

                    editTextAmount.setText( String.format( Locale.ENGLISH, "%.2f", transactionEntity.getAmount() ) );
                    editTextDescription.setText( transactionEntity.getDescription() );
                };
                transactionViewModel.getTransaction( transactionUid ).observe( this, transactionObserver );
            }
        }
    }

    private void validateInputAndSetDisableSaveButton(MenuItem saveItem)  {
        boolean allValid = true;
        EditText editTextAmount = findViewById( R.id.editTextAmount);
        EditText editTextDescription = findViewById( R.id.editTextDescription);

        if( editTextDescription.getText().toString().equals( "" ))
        {
            allValid = false;
        }

        try {
            Double.parseDouble(editTextAmount.getText().toString());
        }catch (NumberFormatException e) {
            allValid = false;
        }

        String regexp = "[-]?[0-9]{0,10}+(\\.[0-9]{0,2})?";
        Matcher matcher = Pattern.compile(regexp).matcher( editTextAmount.getText().toString() );
        if (!matcher.matches()) {
            allValid = false;
        }

        if(saveItem != null) {
            saveItem.setVisible( allValid );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if(accountUid != ACCOUNT_UID_UNDEFINED) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate( R.menu.menu_transaction, menu );
            MenuItem saveItem = menu.findItem( R.id.app_bar_save );
            saveItem.setOnMenuItemClickListener( menuItem -> {

                double amount =  Double.parseDouble(((EditText) findViewById( R.id.editTextAmount )).getText().toString());
                String description = ((EditText) findViewById( R.id.editTextDescription )).getText().toString();

                if (transactionUid == TRANSACTION_UID_UNDEFINED)
                {
                    transactionViewModel.addTransaction( new TransactionEntity( accountUid, amount, description , new Date()) );
                }else{
                    transactionViewModel.updateTransaction(  new TransactionEntity( transactionUid, accountUid, amount, description, new Date() ) );
                }

                finish();
                return true;
            } );
            validateInputAndSetDisableSaveButton(saveItem);
        }
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
