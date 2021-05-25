package de.blocbox.simpleaccount.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.blocbox.simpleaccount.db.converter.AccountTypeConverter;
import de.blocbox.simpleaccount.db.converter.DateConverter;
import de.blocbox.simpleaccount.db.dao.AccountDao;
import de.blocbox.simpleaccount.db.dao.TransactionDao;
import de.blocbox.simpleaccount.db.entity.AccountEntity;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;

@Database( entities = {AccountEntity.class, TransactionEntity.class}, version = 1)
@TypeConverters({AccountTypeConverter.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabaseInstance;

    public static final String DATABASE_NAME = "SimpleAccount_DB";

    public abstract AccountDao accountDao();

    public abstract TransactionDao transactionDao();

    public static AppDatabase getInstance(Context context){
        if (appDatabaseInstance == null){
            synchronized (AppDatabase.class)
            {
                if( appDatabaseInstance == null)
                {
                    appDatabaseInstance =
                            Room.databaseBuilder( context, AppDatabase.class, DATABASE_NAME )
                                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                                    .build();
                }
            }
        }
        return appDatabaseInstance;
    }

    public static void destroyInstance()
    {
        if(appDatabaseInstance != null)
        {
            synchronized (AppDatabase.class)
            {
                if (appDatabaseInstance.isOpen())
                {
                    appDatabaseInstance.close();
                    appDatabaseInstance = null;
                }
            }
        }
    }
}
