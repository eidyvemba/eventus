package Adaptador;

import com.iracema.ffik.R;
import Utils.Evento;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdaptador extends ArrayAdapter<Evento>{
    LayoutInflater flater;

    public SpinnerAdaptador(Activity context, int resouceId, int textviewId, List<Evento> list){
        super(context,resouceId,textviewId, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){
        Evento rowItem = getItem(position);
        viewHolder holder ;
        View rowview = convertView;

        if (rowview==null) {
            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.inflator_for_spinner, null, false);

            holder.txtTitle = (TextView) rowview.findViewById(R.id.spinner_ttx_title);
            rowview.setTag(holder);

        }else{
            holder = (viewHolder) rowview.getTag();
        }

        holder.txtTitle.setText(rowItem.titulo);

        return rowview;
    }

    private class viewHolder{
        TextView txtTitle;
    }
}
