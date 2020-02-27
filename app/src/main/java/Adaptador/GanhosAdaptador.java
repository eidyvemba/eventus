package Adaptador;

import com.iracema.ffik.R;
import android.view.View;
import java.util.ArrayList;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import Utils.ConexaoBD;
import Utils.Evento;
import Utils.GetImageURL;

public class GanhosAdaptador extends RecyclerView.Adapter<GanhosAdaptador.ViewHolder> {
    Context context;
    ArrayList<Evento> list  = new ArrayList<>();

    public GanhosAdaptador(Context context, ArrayList<Evento> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_para_ganhos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Evento evento = list.get(position);

        if (!evento.imagem.equals("") && !evento.imagem.equals(" ")) new GetImageURL(holder.imageView).execute(ConexaoBD.Host+"/webservice/"+evento.imagem);

        holder.txt_name.setText(evento.titulo);
        holder.txt_location.setText(evento.localizacao);
        if (!evento.ganho.equals(""))
            holder.btn_earning.setText("Total Ganhos : Kz " +evento.ganho);
        holder.txt_description.setText(evento.descricao);
        holder.txt_status.setText("Status : " +evento.status);
        holder.txt_amount.setText("Kz" +evento.preco_normal + " (Normal)  " +" Kz" + evento.preco_vip + " (Vip)");
        holder.txt_date.setText(evento.data_inicio + " "+evento.tempo_inicio + " to " +evento.data_fim + " "+evento.tempo_fim);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button btn_earning;
        LinearLayout lay_des;
        TextView txt_name, txt_location, txt_description, txt_date, txt_amount, txt_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_date = (TextView)itemView.findViewById(R.id.home_txt_date);
            txt_name = (TextView)itemView.findViewById(R.id.home_txt_name);
            txt_amount = (TextView)itemView.findViewById(R.id.home_txt_price);
            txt_status = (TextView)itemView.findViewById(R.id.home_txt_status);
            txt_status.setVisibility(View.VISIBLE);

            lay_des = (LinearLayout)itemView.findViewById(R.id.home_lay_des);

            imageView = (ImageView)itemView.findViewById(R.id.home_imageview);

            btn_earning = (Button)itemView.findViewById(R.id.home_btn_booknow);

            txt_location = (TextView)itemView.findViewById(R.id.home_txt_location);
            txt_description = (TextView)itemView.findViewById(R.id.home_txt_description);
        }
    }

}
