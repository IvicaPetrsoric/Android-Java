package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Marko on 23.5.2016..
 */
public class FinanceListAdapter extends ArrayAdapter<FinanceTrack> {

    private Context context;
    List<FinanceTrack> finances;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");


    public FinanceListAdapter(Context context, List<FinanceTrack> finances) {
        super(context, R.layout.list_item_layout_3, finances);
        this.context = context;
        this.finances = finances;
    }


    private class ViewHolder {
        TextView date_txt;
        TextView name_txt;
        TextView money_txt;
    }


    @Override
    public int getCount() {
        return finances.size();
    }


    @Override
    public FinanceTrack getItem(int position) {
        return finances.get(position);
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
            convertView = inflater.inflate(R.layout.list_item_layout_3, null);

            holder = new ViewHolder();
            //holder.id_txt = (TextView) convertView.findViewById(R.id.txt_id);
            holder.date_txt = (TextView) convertView.findViewById(R.id.date_view);
            holder.name_txt = (TextView) convertView.findViewById(R.id.name_view);
            holder.money_txt = (TextView) convertView.findViewById(R.id.money_view);
            //holder.date_text = (TextView) convertView.findViewById(R.id.txt_dateofbirth);
            //holder.mark_text = (TextView) convertView.findViewById(R.id.txt_mark);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FinanceTrack fTrack = (FinanceTrack) getItem(position);
        //holder.id_txt.setText(mStudent.getId() + "");
        holder.date_txt.setText(dateFormatter.format(fTrack.getDate()));
        holder.name_txt.setText(fTrack.getName());
        holder.money_txt.setText("" + fTrack.getMoney() + "kn");
        //holder.mark_text.setText("RMA grade: " + mStudent.getRmaMark() + "");
        //holder.date_text.setText(dateFormatter.format(mStudent.getDateOfBirth()));

        return convertView;
    }


    @Override
    public void add(FinanceTrack inFinances) {
        finances.add(inFinances);
        notifyDataSetChanged();
        super.add(inFinances);
    }

    @Override
    public void remove(FinanceTrack inFinances) {
        finances.remove(inFinances);
        notifyDataSetChanged();
        super.remove(inFinances);
    }
}
