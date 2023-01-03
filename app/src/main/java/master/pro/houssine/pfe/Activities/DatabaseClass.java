package master.pro.houssine.pfe.Activities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import master.pro.houssine.pfe.EntityClass.Contact;
import master.pro.houssine.pfe.DAO.DaoClass;

@Database(entities = {Contact.class}, version = 1)
public abstract class DatabaseClass extends RoomDatabase {

    public abstract DaoClass getDao();

    private static DatabaseClass instance;


    public static DatabaseClass getDatabase(final Context context) {
        if (instance == null) {
            synchronized (DatabaseClass.class) {
                instance = Room.databaseBuilder(context, DatabaseClass.class, "greatejhoussine").allowMainThreadQueries().build();
            }
        }

        return instance;

    }


}
