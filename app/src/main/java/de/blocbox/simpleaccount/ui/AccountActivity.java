package de.blocbox.simpleaccount.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import de.blocbox.simpleaccount.R;
import de.blocbox.simpleaccount.db.entity.AccountEntity;
import de.blocbox.simpleaccount.model.AccountType;
import de.blocbox.simpleaccount.viewmodel.AccountViewModel;


public class AccountActivity extends AppCompatActivity {

    private AccountViewModel mAccountViewModel;
    private final int ACCOUNT_UID_UNDEFINED = -1;
    private int accountUid = ACCOUNT_UID_UNDEFINED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_account );

        Intent intent = getIntent();
        accountUid = (int) intent.getIntExtra("AccountEntity.uid", ACCOUNT_UID_UNDEFINED );

        mAccountViewModel = (AccountViewModel) ViewModelProviders.of(this).get( AccountViewModel.class);

        //TODO: Child toolbar?
        //final Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setOnClickListener(  );

        //setSupportActionBar(toolbar);

        if(accountUid == ACCOUNT_UID_UNDEFINED) {

            getSupportActionBar().setTitle( R.string.add_account );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        }else{

            getSupportActionBar().setTitle( R.string.update_account );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );

            ((LinearLayout) findViewById( R.id.linear_layout_person )).setVisibility( View.INVISIBLE );

            final Observer<AccountEntity> accountEntityObserver = new Observer<AccountEntity>() {
                @Override
                public void onChanged(@Nullable final AccountEntity accountEntity) {
                    if (accountEntity.getUid() == accountUid) {
                        ((EditText) findViewById( R.id.editTextFirstName )).setText( accountEntity.getFirstName() );
                        ((EditText) findViewById( R.id.editTextLastName )).setText( accountEntity.getLastName() );
                        AccountType accountType = accountEntity.getAccountType();
                        switch (accountType) {
                            case FEMALE:
                                ((RadioButton) findViewById( R.id.radioButtonFemale )).setChecked( true );
                                break;
                            case MALE:
                                ((RadioButton) findViewById( R.id.radioButtonMale )).setChecked( true );
                                break;
                            case GROUP:
                            default:
                                ((RadioButton) findViewById( R.id.radioButtonGroup )).setChecked( true );
                                break;
                        }
                        ((LinearLayout) findViewById( R.id.linear_layout_person )).setVisibility( View.VISIBLE );
                    }
                }
            };
            mAccountViewModel.getLiveDataAccount( accountUid ).observe( this, accountEntityObserver );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_account, menu);
        MenuItem saveItem = menu.findItem(R.id.app_bar_save);
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                String firstName = ((EditText) findViewById( R.id.editTextFirstName )).getText().toString();
                String lastName = ((EditText) findViewById( R.id.editTextLastName )).getText().toString();
                AccountType accountType = AccountType.GROUP;

                if(((RadioButton) findViewById(R.id.radioButtonMale)).isChecked()) {
                    accountType = AccountType.MALE;
                }
                if(((RadioButton) findViewById(R.id.radioButtonFemale)).isChecked()) {
                    accountType = AccountType.FEMALE;
                }

                if(accountUid == ACCOUNT_UID_UNDEFINED) {
                    mAccountViewModel.addPerson( new AccountEntity(firstName, lastName, accountType ) );
                }else{
                    mAccountViewModel.updatePerson( new AccountEntity( accountUid, firstName, lastName, accountType ) );
                }

                finish();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
