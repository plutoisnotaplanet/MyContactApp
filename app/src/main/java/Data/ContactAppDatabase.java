package Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import Person.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactAppDatabase extends RoomDatabase {

    public abstract ContactDAO getContactDao();
}
