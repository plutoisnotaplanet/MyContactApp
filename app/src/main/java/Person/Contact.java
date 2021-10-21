package Person;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    private long id;

    @ColumnInfo(name = "contact_firstName")
    private String firstName;

    @ColumnInfo(name = "contact_lastName")
    private String lastName;

    @ColumnInfo(name = "contact_email")
    private String email;

    @ColumnInfo(name ="contact_number")
    private String number;

    public Contact(long id, String firstName, String lastName, String email, String number) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
