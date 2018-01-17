package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sandi on 14.4.2016..
 */
public class StudentListAdapter extends ArrayAdapter<Student> {

    private Context context;
    List<Student> students;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");


    public StudentListAdapter(Context context, List<Student> students) {
        super(context, R.layout.list_item_layout, students);
        this.context = context;
        this.students = students;
    }


    private class ViewHolder {
        TextView id_txt;
        TextView name_txt;
        TextView surname_txt;
        TextView date_text;
        TextView mark_text;
        ImageView contact_photo;
        Bitmap loadedBitmap;
    }


    @Override
    public int getCount() {
        return students.size();
    }


    @Override
    public Student getItem(int position) {
        return students.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_layout, null);

            holder = new ViewHolder();
            //holder.id_txt = (TextView) convertView.findViewById(R.id.txt_id);
            holder.name_txt = (TextView) convertView.findViewById(R.id.txt_name);
            holder.surname_txt = (TextView) convertView.findViewById(R.id.txt_surname);
            holder.contact_photo = (ImageView) convertView.findViewById(R.id.listPhoto);
            //holder.date_text = (TextView) convertView.findViewById(R.id.txt_dateofbirth);
            //holder.mark_text = (TextView) convertView.findViewById(R.id.txt_mark);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Student mStudent = (Student) getItem(position);
        //holder.id_txt.setText(mStudent.getId() + "");
        holder.name_txt.setText(mStudent.getName());
        holder.surname_txt.setText(mStudent.getSurnname());
        String picturePath = mStudent.getPicture();
        if (picturePath.length() == 0) {
            holder.contact_photo.setImageResource(R.drawable.ic_contact_picture_holo_light);
        } else {
            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
                //Get thumbnail of picture from gallery
                byte[] imageData = exif.getThumbnail();

                try{
                    holder.loadedBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                }
                catch (Exception e){
                    holder.loadedBitmap = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    holder.loadedBitmap = rotateBitmap(holder.loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    holder.loadedBitmap = rotateBitmap(holder.loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    holder.loadedBitmap = rotateBitmap(holder.loadedBitmap, 270);
                    break;
            }
            holder.contact_photo.setImageBitmap(holder.loadedBitmap);
            //holder.mark_text.setText("RMA grade: " + mStudent.getRmaMark() + "");
            //holder.date_text.setText(dateFormatter.format(mStudent.getDateOfBirth()));
        }

        return convertView;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    @Override
    public void add(Student inStudent) {
        students.add(inStudent);
        notifyDataSetChanged();
        super.add(inStudent);
    }

    @Override
    public void remove(Student inStudent) {
        students.remove(inStudent);
        notifyDataSetChanged();
        super.remove(inStudent);
    }
}