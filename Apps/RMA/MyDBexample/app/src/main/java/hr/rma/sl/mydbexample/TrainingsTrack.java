package hr.rma.sl.mydbexample;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Marko on 6.5.2016..
 */
public class TrainingsTrack implements Parcelable {

    private int id;
    private Date date;
    private String time;
    private String players;

    public TrainingsTrack() {
        super();
    }

    private TrainingsTrack(Parcel in) {
        super();
        this.id = in.readInt();
        this.date = new Date(in.readLong());
        this.time = in.readString();
        this.players = in.readString();
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

    public String getTtime() {
        return time;
    }

    public void setTtime(String time) {
        this.time = time;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }


    @Override
    public String toString() {
        return "Student [id=" + id + ", date=" + date + ", time=" + time +
                ", players=" + players + "]";
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

        TrainingsTrack other = (TrainingsTrack) obj;
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
        parcel.writeString(getTtime());
        parcel.writeString(getPlayers());
    }


    public static final Parcelable.Creator<TrainingsTrack> CREATOR = new Parcelable.Creator<TrainingsTrack>() {
        public TrainingsTrack createFromParcel(Parcel in) {
            return new TrainingsTrack(in);
        }

        public TrainingsTrack[] newArray(int size) {
            return new TrainingsTrack[size];
        }
    };

}