package de.blocbox.simpleaccount.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.blocbox.simpleaccount.DataRepository;
import de.blocbox.simpleaccount.R;
import de.blocbox.simpleaccount.SimpleAccountApp;
import de.blocbox.simpleaccount.db.AppDatabase;
import de.blocbox.simpleaccount.db.entity.AccountWithTransactionEntity;
import de.blocbox.simpleaccount.viewmodel.AccountsViewModel;

public class MainActivity extends AppCompatActivity {

    private AccountAdapter accountAdapter;
    private AccountsViewModel mAccountsViewModel;

    private static final int REQUEST_CODE_ACTION_CREATE_DOCUMENT = 11;
    private static final int REQUEST_CODE_ACTION_GET_CONTENT = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        final Toolbar toolbar = findViewById( R.id.toolbar );
        //toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        setSupportActionBar( toolbar );

        FloatingActionButton fab = findViewById( R.id.floatingActionButtonAddAccount );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( view.getContext(), AccountActivity.class ) );
            }
        } );

        accountAdapter = new AccountAdapter();
        final RecyclerView recyclerView = findViewById( R.id.recyclerViewAccounts );
        recyclerView.setAdapter( accountAdapter );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        final Observer<List<AccountWithTransactionEntity>> accountsWithTransactionsObserver = new Observer<List<AccountWithTransactionEntity>>() {
            @Override
            public void onChanged(@Nullable final List<AccountWithTransactionEntity> accountsWithTransactions) {
                // Update the UI
                Collections.sort( accountsWithTransactions );
                accountAdapter.setAccountWithTransactionsList( accountsWithTransactions );
                //final MenuItem searchItem = getSupportActionBar().getfindViewById(R.id.app_bar_search);
                final MenuItem searchItem = toolbar.getMenu().findItem( R.id.app_bar_search );
                if (searchItem != null) { //null within first call since onCreateOptionsMenu() not yet called
                    final SearchView searchView = (SearchView) searchItem.getActionView();
                    String query = "";
                    if (searchView.isIconified()) {
                        query = searchView.getQuery().toString();
                    }
                    accountAdapter.getFilter().filter( query );
                }
            }
        };
        mAccountsViewModel = new ViewModelProvider(this).get( AccountsViewModel.class );
        mAccountsViewModel.getLiveDataAccountsWithTransactions().observe( this, accountsWithTransactionsObserver );

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback( this ) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final AccountWithTransactionEntity accountWithTransactionEntity = accountAdapter.getViewData().get( position );

                mAccountsViewModel.deletePerson( accountWithTransactionEntity.accountEntity );

                Snackbar snackbar = Snackbar.make( recyclerView, "Item was removed from the list.", Snackbar.LENGTH_INDEFINITE );

                snackbar.setActionTextColor( Color.WHITE );
                snackbar.setAction( "UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAccountsViewModel.addPerson( accountWithTransactionEntity.accountEntity );
                        recyclerView.scrollToPosition( position );
                    }
                } );
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper( swipeToDeleteCallback );
        itemTouchHelper.attachToRecyclerView( recyclerView );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );

        final MenuItem exportItem = menu.findItem( R.id.action_export );
        exportItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent( Intent.ACTION_CREATE_DOCUMENT );
                intent.addCategory( Intent.CATEGORY_OPENABLE );
                intent.setType("application/octet-stream"); //or application/vnd.sqlite3
                String fileName = new SimpleDateFormat( "yyyy-MM-dd_HH:mm", Locale.ENGLISH ).format( new Date() );
                fileName += "_SimpleAccount.db";
                intent.putExtra( Intent.EXTRA_TITLE, fileName );
                startActivityForResult( intent, REQUEST_CODE_ACTION_CREATE_DOCUMENT );
                return false;
            }
        } );

        final MenuItem importItem = menu.findItem( R.id.action_import );
        importItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/octet-stream");
                startActivityForResult( intent, REQUEST_CODE_ACTION_GET_CONTENT );
                return false;
            }
        } );

        final MenuItem searchItem = menu.findItem( R.id.app_bar_search );
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault( false );
        searchView.setImeOptions( EditorInfo.IME_ACTION_DONE );

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                accountAdapter.getFilter().filter( newText );
                return false;
            }
        } );

        searchItem.setOnActionExpandListener( new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                final SearchView searchView = (SearchView) item.getActionView();
                accountAdapter.getFilter().filter( searchView.getQuery() );
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                accountAdapter.getFilter().filter( "" );
                //https://stackoverflow.com/questions/1109022/close-hide-android-soft-keyboard
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
                    inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(), 0 );
                }
                return true;
            }
        } );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ACTION_CREATE_DOCUMENT) {
            //https://androidexplained.github.io/android/room/2020/10/03/room-backup-restore.html
            mAccountsViewModel.checkpoint();
            while(mAccountsViewModel.getCheckpointInProgress())
            {
            }
            try{
                OutputStream outputStream = getContentResolver().openOutputStream(data.getData());
                File sourceFile = new File( (((SimpleAccountApp)mAccountsViewModel.getApplication()).getAppDatabase().getOpenHelper().getWritableDatabase().getPath()) );
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                int length;
                byte[] bytes = new byte[1024];

                while ((length = fileInputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, length);
                }

                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
                Toast.makeText( this, "Success: File saved", Toast.LENGTH_LONG ).show(); //Uri.decode( data.getData().getPath())
            }catch (IOException e)
            {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ACTION_GET_CONTENT) {
            finish();
            try {
                InputStream inputStream = getContentResolver().openInputStream( data.getData() );
                File destFile = new File(((SimpleAccountApp)mAccountsViewModel.getApplication()).getAppDatabase().getOpenHelper().getWritableDatabase().getPath());
                FileOutputStream fileOutputStream = new FileOutputStream( destFile );

                AppDatabase.destroyInstance();
                DataRepository.destroyInstance();

                int length;
                byte[] bytes = new byte[1024];

                while ((length = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, length);
                }

                inputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();

                finish();
                Intent intent = getPackageManager().getLaunchIntentForPackage("de.blocbox.simpleaccount");
                startActivity(intent);

            }catch (IOException e) {
                AppDatabase.destroyInstance();
                DataRepository.destroyInstance();
                finish();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
