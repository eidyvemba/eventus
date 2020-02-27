package Adaptador;

import com.iracema.ffik.R;
import com.squareup.picasso.Picasso;

import android.view.View;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import Utils.ConexaoBD;
import Utils.Evento;
import Utils.GetImageURL;

public class TotalReservasAdaptador extends RecyclerView.Adapter<TotalReservasAdaptador.ViewHolder>{
    Context context;
    ArrayList<Evento> list  = new ArrayList<>();

    public TotalReservasAdaptador(Context context, ArrayList<Evento> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_para_eventos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Evento evento = list.get(position);

        if (!evento.imagem.equals("") && !evento.imagem.equals(" ")) new GetImageURL(holder.imageView).execute(ConexaoBD.Host+"/webservice/"+evento.imagem);
        holder.txt_name.setText(evento.titulo);
        holder.txt_location.setText("Lugares Reservados : " +evento.lugares + " (" +evento.tipo + ")");
        holder.txt_date.setText("Total Pago :  Kz" + evento.preco_normal);
    }


    @Override
    public long getItemId(int position){ return position; }

    @Override
    public int getItemCount() { return list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txt_name, txt_date, txt_location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.home_imageview);

            txt_name = (TextView)itemView.findViewById(R.id.home_txt_name);
            txt_date = (TextView)itemView.findViewById(R.id.home_txt_date);
            txt_location = (TextView)itemView.findViewById(R.id.home_txt_location);

        }
    }
}
