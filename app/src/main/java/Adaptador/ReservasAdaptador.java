package Adaptador;

import Utils.ConexaoBD;
import Utils.Evento;
import Utils.GetImageURL;

import com.iracema.ffik.R;
import android.view.View;
import java.util.ArrayList;
import android.widget.Button;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public class ReservasAdaptador extends RecyclerView.Adapter<ReservasAdaptador.ViewHolder>{
    Context context;
    ArrayList<Evento> list  = new ArrayList<>();
    ClickListnerForRecyclerView clickListnerForRecyclerView;

    public ReservasAdaptador(Context context, ArrayList<Evento> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_para_reserva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Evento eventos = list.get(position);

        holder.txt_name.setText(eventos.titulo);
        holder.txt_location.setText(eventos.localizacao);
        holder.txt_description.setText(eventos.descricao);
        holder.txt_seats.setText("Sem Reserva: " + eventos.lugares);
        holder.txt_amount.setText("Preço : Kz" +eventos.preco_normal);
        holder.txt_date.setText(eventos.data_inicio + " "+eventos.tempo_inicio + " to " +eventos.data_fim + " "+eventos.tempo_fim);

        if (!eventos.imagem.equals("") && !eventos.imagem.equals(" ")) new GetImageURL(holder.imageView).execute(ConexaoBD.Host+"/webservice/"+eventos.imagem);

        holder.btn_book_now.setText(context.getResources().getString(R.string.download));

        holder.btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v, position);
                }
            }
        });
    }

    @Override
    public long getItemId(int position){ return position; }

    @Override
    public int getItemCount() { return list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button btn_book_now;
        LinearLayout lay_des;
        TextView txt_name, txt_location, txt_description, txt_date, txt_amount, txt_seats;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lay_des = (LinearLayout)itemView.findViewById(R.id.home_lay_des);

            imageView = (ImageView)itemView.findViewById(R.id.home_imageview);

            btn_book_now = (Button)itemView.findViewById(R.id.home_btn_booknow);

            txt_date = (TextView)itemView.findViewById(R.id.home_txt_date);
            txt_name = (TextView)itemView.findViewById(R.id.home_txt_name);
            txt_seats = (TextView)itemView.findViewById(R.id.home_txt_seats);
            txt_amount = (TextView)itemView.findViewById(R.id.home_txt_price);
            txt_location = (TextView)itemView.findViewById(R.id.home_txt_location);
            txt_description = (TextView)itemView.findViewById(R.id.home_txt_description);
        }
    }

    public interface ClickListnerForRecyclerView{
        void layout_clicked(View view , int position);
    }

    public void setClickListner(ClickListnerForRecyclerView itemclickListner){ this.clickListnerForRecyclerView = itemclickListner; }
}
