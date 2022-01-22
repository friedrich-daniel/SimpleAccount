package de.blocbox.simpleaccount.db;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import de.blocbox.simpleaccount.db.converter.AccountTypeConverter;
import de.blocbox.simpleaccount.db.converter.DateConverter;
import de.blocbox.simpleaccount.db.dao.AccountDao;
import de.blocbox.simpleaccount.db.dao.TransactionDao;
import de.blocbox.simpleaccount.db.entity.AccountEntity;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;


@Database(
        version = 2,
        entities = {
                AccountEntity.class,
                TransactionEntity.class
        },
//        autoMigrations = {
//                @AutoMigration(
//                        from = 1,
//                        to = 2,
//                        spec = AppDatabase.AutoMigrationFrom1To2.class
//                )
//        },
        exportSchema = true
)
@TypeConverters({AccountTypeConverter.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // Refer to https://stackoverflow.com/questions/805363/how-do-i-rename-a-column-in-a-sqlite-database-table

    // 1. Solution (requires androidx.room:room-runtime:2.4.1 which would require Android 12 (API level 31))
    //  @RenameColumn(
    //          tableName = "accounts",
    //          fromColumnName = "last_name",
    //          toColumnName = "description"
    //  )
    //  @RenameColumn(
    //          tableName = "accounts",
    //          fromColumnName = "first_name",
    //          toColumnName = "name"
    //  )
    //  static class AutoMigrationFrom1To2 implements AutoMigrationSpec {
    //      @Override
    //      public void onPostMigrate(@NonNull SupportSQLiteDatabase db) {
    //          // Invoked once auto migration is done
    //      }
    //  }

    // 2. Solution
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL( "CREATE TABLE IF NOT EXISTS accounts_new (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `description` TEXT, `accountType` INTEGER);" );
                database.execSQL( "INSERT INTO accounts_new (name, description, accountType) SELECT first_name, last_name, accountType FROM accounts;");
                database.execSQL( "DROP TABLE accounts;" );
                database.execSQL( "ALTER TABLE accounts_new RENAME TO accounts;");
            }catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    };


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
                                    .addMigrations( MIGRATION_1_2 )
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
