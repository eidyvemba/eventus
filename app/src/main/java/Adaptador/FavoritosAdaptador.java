package Adaptador;

import com.iracema.ffik.R;
import com.squareup.picasso.Picasso;

import android.view.View;
import java.util.ArrayList;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import Utils.ConexaoBD;
import Utils.Evento;
import Utils.GetImageURL;

public class FavoritosAdaptador extends RecyclerView.Adapter<FavoritosAdaptador.ViewHolder> {
    Context context;
    ArrayList<Evento> list  = new ArrayList<>();
    ClickListnerForRecyclerView clickListnerForRecyclerView;

    public FavoritosAdaptador(Context context, ArrayList<Evento> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_para_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Evento evento = list.get(position);

        holder.txt_name.setText(evento.titulo);
        holder.txt_location.setText(evento.localizacao);
        holder.txt_description.setText(evento.descricao);
        holder.txt_amount.setText("Kz" +evento.preco_normal + " (Normal)  " +" Kz" + evento.preco_vip + " (Vip)");
        holder.txt_date.setText(evento.data_inicio + " "+evento.tempo_inicio + " to " +evento.data_fim + " "+evento.tempo_fim);

        if (!evento.imagem.equals("") && !evento.imagem.equals(" ")) new GetImageURL(holder.imageView).execute(ConexaoBD.Host+"/webservice/"+evento.imagem);

        holder.lay_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v, position);
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v , position);
                }
            }
        });

        holder.btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v , position);
                }
            }
        });

        holder.lay_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v , position);
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
        ImageView img_fav;
        LinearLayout lay_des, lay_fav;
        TextView txt_name, txt_location, txt_description, txt_date, txt_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.home_imageview);

            btn_book_now = (Button)itemView.findViewById(R.id.home_btn_booknow);

            lay_des = (LinearLayout)itemView.findViewById(R.id.home_lay_des);
            lay_fav = (LinearLayout)itemView.findViewById(R.id.home_lay_fav);
            lay_fav.setVisibility(View.VISIBLE);

            img_fav = (ImageView)itemView.findViewById(R.id.home_img_fav);
            img_fav.setImageDrawable(context.getResources().getDrawable(R.drawable.fav_fill));

            txt_date = (TextView)itemView.findViewById(R.id.home_txt_date);
            txt_name = (TextView)itemView.findViewById(R.id.home_txt_name);
            txt_amount = (TextView)itemView.findViewById(R.id.home_txt_price);
            txt_location = (TextView)itemView.findViewById(R.id.home_txt_location);
            txt_description = (TextView)itemView.findViewById(R.id.home_txt_description);
        }
    }


    public interface ClickListnerForRecyclerView{
        Void layout_clicked(View view, int position);
    }

    public void setClickListner(ClickListnerForRecyclerView itemClickListner){ this.clickListnerForRecyclerView = itemClickListner; }

}
