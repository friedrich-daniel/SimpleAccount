package de.blocbox.simpleaccount.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;

import de.blocbox.simpleaccount.R;
import de.blocbox.simpleaccount.db.entity.AccountWithTransactionEntity;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;
import de.blocbox.simpleaccount.viewmodel.AccountViewModel;

public class TransactionsActivity extends AppCompatActivity {
    private final int ACCOUNT_UID_UNDEFINED = -1;
    private int accountUid = ACCOUNT_UID_UNDEFINED;
    private AccountViewModel mAccountViewModel;
    private  TransactionsAdapter transactionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_transactions );

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle( R.string.view_transactions );
        }

        Intent intent = getIntent();
        accountUid = intent.getIntExtra( "AccountEntity.uid", ACCOUNT_UID_UNDEFINED );

        transactionsAdapter = new TransactionsAdapter();
        final RecyclerView recyclerView = findViewById(R.id.recyclerViewTransactions );
        recyclerView.setAdapter( transactionsAdapter );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (accountUid != ACCOUNT_UID_UNDEFINED) {

            final Observer<AccountWithTransactionEntity> accountWithTransactionsObserver = accountWithTransactions -> {
                // Update the UI
                ImageView imageViewIcon = findViewById( R.id.imageViewAccountIcon );
                TextView textViewAccountName = findViewById( R.id.textViewAccountName );
                TextView textViewAccountDescription = findViewById( R.id.textViewTransactionsAccountDescription );
                TextView textViewAccountType = findViewById( R.id.textViewAccountSum );

                textViewAccountDescription.setText( accountWithTransactions.accountEntity.getDescription() );
                if (accountWithTransactions.accountEntity.getDescription().length() != 0) {
                    textViewAccountDescription.setVisibility( View.VISIBLE );
                }else{
                    textViewAccountDescription.setVisibility( View.GONE );
                }

                imageViewIcon.setImageResource( Helper.GetDrawableByAccountType( accountWithTransactions.accountEntity.getAccountType() ) );
                textViewAccountName.setText( accountWithTransactions.accountEntity.getName()  );
                textViewAccountType.setText( Helper.GetSumOfTransitions( accountWithTransactions.transactionEntities ));
                Collections.sort(accountWithTransactions.transactionEntities);
                Collections.reverse( accountWithTransactions.transactionEntities );
                transactionsAdapter.setTransactions( accountWithTransactions.transactionEntities);
            };
            mAccountViewModel = new ViewModelProvider(this).get( AccountViewModel.class );
            mAccountViewModel.getLiveDataAccountWithTransactions( accountUid ).observe( this, accountWithTransactionsObserver );

            SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                    final int position = viewHolder.getAdapterPosition();
                    final TransactionEntity transactionEntity = transactionsAdapter.getTransactions().get( position );

                    mAccountViewModel.deleteTransaction( transactionEntity );

                    String text = transactionEntity.getDescription() + " " +
                            getResources().getString(R.string.item_removed);

                    Snackbar.make( recyclerView, text, Snackbar.LENGTH_INDEFINITE )
                            .setActionTextColor( Color.WHITE )
                            .setAction( R.string.undo, view -> {
                                mAccountViewModel.addTransaction( transactionEntity );
                                recyclerView.scrollToPosition( position );
                                } )
                            .show();
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper( swipeToDeleteCallback );
            itemTouchHelper.attachToRecyclerView(recyclerView);

            FloatingActionButton fab = findViewById(R.id.floatingActionButtonAddTransaction );
            fab.setOnClickListener( view -> {
                Intent i = new Intent(view.getContext(), TransactionActivity.class);
                i.putExtra("AccountEntity.uid", accountUid );
                view.getContext().startActivity(i);
            } );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if(accountUid != ACCOUNT_UID_UNDEFINED) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate( R.menu.menu_transactions, menu );
            MenuItem updateAccountItem = menu.findItem( R.id.edit_account );
            updateAccountItem.setOnMenuItemClickListener( menuItem -> {
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                intent =  intent.putExtra("AccountEntity.uid", accountUid);
                startActivity(intent);
                return true;
            } );
            MenuItem addTransactionItem = menu.findItem( R.id.add_transaction );
            addTransactionItem.setOnMenuItemClickListener( menuItem -> {
                Intent intent = new Intent(getBaseContext(), TransactionActivity.class);
                intent =  intent.putExtra("AccountEntity.uid", accountUid);
                startActivity(intent);
                return true;
            } );
        }
        return super.onCreateOptionsMenu( menu );
    }
}
