package hr.rma.sl.mydbexample;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Marko on 23.5.2016..
 */

public class FinanceTrack implements Parcelable {

    private int id;
    private Date date;
    private String name;
    private int money;

    public FinanceTrack() {
        super();
    }

    private FinanceTrack(Parcel in) {
        super();
        this.id = in.readInt();
        this.date = new Date(in.readLong());
        this.name = in.readString();
        this.money = in.readInt();
    }

    // Getters and Setters:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    @Override
    public String toString() {
        return "Finance [id=" + id + ", date=" + date + ", name=" + name +
                ", money=" + money + "]";
    }

    /*
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
    */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        FinanceTrack other = (FinanceTrack) obj;
        if (this.id != other.id) return false;
        return true;
    }

    // Doesn't matter for us:
    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // "serialization"
        parcel.writeInt(getId());
        parcel.writeLong(getDate().getTime());
        parcel.writeString(getName());
        parcel.writeInt(getMoney());
    }


    public static final Parcelable.Creator<FinanceTrack> CREATOR = new Parcelable.Creator<FinanceTrack>() {
        public FinanceTrack createFromParcel(Parcel in) {
            return new FinanceTrack(in);
        }

        public FinanceTrack[] newArray(int size) {
            return new FinanceTrack[size];
        }
    };

}
