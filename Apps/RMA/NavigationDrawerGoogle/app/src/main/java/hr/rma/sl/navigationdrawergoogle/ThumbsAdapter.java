package hr.rma.sl.navigationdrawergoogle;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sandi on 21.4.2016..
 */
public class ThumbsAdapter extends BaseAdapter {

    private Context context;
    String[] imagePaths;



    public ThumbsAdapter(Context localContext, String[] imagePaths) {
        this.context = localContext;
        this.imagePaths = imagePaths;
    }


    public int getCount() {
        return imagePaths.length;
    }


    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView picturesView;

        View v = convertView;

        if (v == null) {
            picturesView = new ImageView(context);
        } else {
            picturesView = (ImageView) convertView;
        }

        String input = imagePaths[position];
        picturesView.setImageURI(Uri.withAppendedPath(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + input));
        picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picturesView.setPadding(5, 5, 5, 5);
        picturesView.setLayoutParams(new GridView.LayoutParams(150, 150));

        return picturesView;

    }
}
