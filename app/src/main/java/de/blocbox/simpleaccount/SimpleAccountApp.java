package de.blocbox.simpleaccount;

import android.app.Application;
import de.blocbox.simpleaccount.db.AppDatabase;

public class SimpleAccountApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDatabase getAppDatabase(){
        return AppDatabase.getInstance(this);
    }

    public DataRepository getDataRepository() {
        return DataRepository.getInstance(getAppDatabase());
    }

}
