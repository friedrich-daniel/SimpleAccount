package de.blocbox.simpleaccount.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.blocbox.simpleaccount.SimpleAccountApp;
import de.blocbox.simpleaccount.DataRepository;
import de.blocbox.simpleaccount.db.entity.AccountEntity;
import de.blocbox.simpleaccount.db.entity.AccountWithTransactionEntity;

import java.io.OutputStream;
import java.util.List;

public class AccountsViewModel extends AndroidViewModel {

    private final DataRepository mDataRepository;
    private final LiveData<List<AccountWithTransactionEntity>> mLiveDataAccountsWithTransactions;

    public AccountsViewModel(@NonNull Application application) {
        super(application);
        mDataRepository = ((SimpleAccountApp)application).getDataRepository();
        mLiveDataAccountsWithTransactions = mDataRepository.getAccountsWithTransactions();
    }

    public LiveData<List<AccountWithTransactionEntity>> getLiveDataAccountsWithTransactions() {
        return mLiveDataAccountsWithTransactions;
    }

    public void checkpoint() {
        mDataRepository.checkpoint();
    }

    public boolean getCheckpointInProgress() {
        return mDataRepository.getCheckpointInProgress();
    }

    public void addPerson(AccountEntity accountEntity) {
        mDataRepository.insertAccount( accountEntity );
    }

    public void deletePerson(AccountEntity accountEntity) {
        mDataRepository.deleteAccount( accountEntity );
    }

}
