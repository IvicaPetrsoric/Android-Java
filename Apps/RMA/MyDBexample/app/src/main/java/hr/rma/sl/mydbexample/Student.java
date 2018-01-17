package hr.rma.sl.mydbexample;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/**
 * Created by Sandi on 14.4.2016..
 */

// This class implements Parcelable interface because we need to bundle the student object
// as argument when sending between activities via intents.
// It requires a little more effort to use than using Java's native serialization,
// but it's way faster.

// Interface for classes whose instances can be written to and restored from a Parcel.
// Classes implementing the Parcelable interface must also have a non-null static field called
// CREATOR of a type that implements the Parcelable.Creator interface.

public class Student implements Parcelable {

    private int id;
    private String name;
    private String surname;
    private Date dateOfBirth;
    private int rmaMark;
    private String picture;
    private int money;

    public Student() {
        super();
    }

    private Student(Parcel in) {
        super();
        this.id = in.readInt();
        this.name = in.readString();
        this.surname = in.readString();
        this.dateOfBirth = new Date(in.readLong());
        this.rmaMark = in.readInt();
        this.picture = in.readString();
        this.money = in.readInt();
    }

    // Getters and Setters:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getRmaMark() {
        return rmaMark;
    }

    public void setRmaMark(int rmaMark) {
        this.rmaMark = rmaMark;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", surname=" + surname +
                ", dateOfBirth=" + dateOfBirth + ", RMA=" + rmaMark + "]";
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

        Student other = (Student) obj;
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
        parcel.writeString(getName());
        parcel.writeString(getSurnname());
        parcel.writeLong(getDateOfBirth().getTime());
        parcel.writeInt(getRmaMark());
        parcel.writeString(getPicture());
    }


    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

}

