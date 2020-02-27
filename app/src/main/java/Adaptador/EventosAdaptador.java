package Adaptador;

import com.iracema.ffik.R;

import Utils.ConexaoBD;
import Utils.Evento;
import Utils.GetImageURL;
import android.graphics.Color;
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

public class EventosAdaptador extends RecyclerView.Adapter<EventosAdaptador.ViewHolder> {
    Context context;
    ArrayList<Evento> list  = new ArrayList<>();
    ClickListnerForRecyclerView clickListnerForRecyclerView;

    public EventosAdaptador(Context context, ArrayList<Evento> list) {
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
            //Picasso.with(context).load(evento.imagem).into(holder.imageView);
        if (evento.status.equals("promotor"))
        {
            holder.txt_lugares.setText("Bilhetes Disponíveis: " + evento.lugares);
        }else {
            holder.txt_lugares.setText("Bilhetes Disponíveis: " + (Integer.parseInt(evento.lugares) - Integer.parseInt(evento.total_reservas)));
        }

        holder.txt_tot_geral.setText("Bilhetes Total: " + evento.lugares);
        holder.txt_localizacao.setText("Local:" + evento.localizacao);
        //holder.txt_description.setText(evento.descricao);
        holder.txt_name.setText(evento .tipo + " : "+evento.titulo);
        holder.txt_preco_normal.setText("Preço Normal: " + evento.preco_normal+" Kz");
        holder.txt_preco_vip.setText("Preço Vip: " + evento.preco_vip+" Kz");
        holder.txt_data_i.setText("Data Inicio: " + evento.data_inicio + " " +evento.tempo_inicio);
        holder.txt_data_f.setText("Data Fim: " +evento.data_fim + " "+evento.tempo_fim);

        holder.txt_tot_vendidos.setVisibility(View.GONE);
        holder.txt_tot_geral.setVisibility(View.GONE);


        if (evento.favourito.equals("1"))
        {
            holder.btn_favoritar.setBackgroundColor(Color.GRAY);
            holder.btn_favoritar.setEnabled(false);
            holder.btn_favoritar.setText("Favoritado");

        }

        /*if (evento.reserva.equals("1"))
        {
            holder.btn_reservar.setBackgroundColor(Color.GRAY);
            holder.btn_reservar.setEnabled(false);
            holder.btn_reservar.setText("Reservado");
            holder.btn_favoritar.setVisibility(View.GONE);
        }*/

        if (evento.status.equals("promotor"))
        {
            holder.btn_favoritar.setVisibility(View.GONE);
            holder.btn_reservar.setVisibility(View.GONE);
            holder.lay_des.setOnClickListener(null);

            if (!evento.ganho.equals(""))
            {
                int ganho = Integer.parseInt(evento.ganho);
                int reservas = Integer.parseInt(evento.preco_normal);

                holder.txt_preco_normal.setText("Total Ganho: " + ganho * reservas +" Kz");

                holder.txt_preco_vip.setVisibility(View.GONE);
                holder.txt_data_i.setVisibility(View.GONE);
                holder.txt_data_f.setVisibility(View.GONE);
            }

            if (!evento.total_reservas.equals(""))
            {
                holder.txt_lugares.setText("Bilhetes Disponíveis: " + (Integer.parseInt(evento.lugares) - Integer.parseInt(evento.total_reservas)));
                holder.txt_tot_vendidos.setText("Bilhetes Vendidos: " + evento.total_reservas+"");
                //holder.txt_preco_normal.setText("Total reservas: " + evento.total_reservas+"");

                holder.txt_tot_vendidos.setVisibility(View.VISIBLE);
                holder.txt_tot_geral.setVisibility(View.VISIBLE);
                //holder.txt_preco_vip.setVisibility(View.GONE);
                holder.txt_data_i.setVisibility(View.GONE);
                holder.txt_data_f.setVisibility(View.GONE);
            }


        }

        holder.lay_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v, position);
                }
            }
        });

        holder.btn_reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v, position);
                }
            }
        });

        holder.btn_favoritar.setOnClickListener(new View.OnClickListener() {
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
        Button btn_reservar, btn_favoritar;
        LinearLayout lay_des;
        TextView txt_name, txt_localizacao, txt_lugares, txt_tot_vendidos, txt_tot_geral, txt_description, txt_data_i, txt_data_f, txt_preco_normal, txt_preco_vip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.home_imageview);

            lay_des = (LinearLayout)itemView.findViewById(R.id.home_lay_des);

            btn_reservar = (Button)itemView.findViewById(R.id.home_btn_reservar);
            btn_favoritar = (Button)itemView.findViewById(R.id.home_btn_favorito);

            //btn_book_now.setVisibility(View.GONE);

            txt_data_i = (TextView)itemView.findViewById(R.id.home_txt_data_i);
            txt_data_f = (TextView)itemView.findViewById(R.id.home_txt_data_f);
            txt_name = (TextView)itemView.findViewById(R.id.home_txt_nome);
            txt_localizacao = (TextView)itemView.findViewById(R.id.home_txt_localizacao);
            txt_tot_vendidos = (TextView)itemView.findViewById(R.id.home_txt_tot_vendidos);
            txt_tot_geral = (TextView)itemView.findViewById(R.id.home_txt_tot_geral);
            txt_preco_normal = (TextView)itemView.findViewById(R.id.home_txt_preco_normal);
            txt_preco_vip = (TextView)itemView.findViewById(R.id.home_txt_preco_vip);
            txt_lugares = (TextView)itemView.findViewById(R.id.home_txt_lugares);
            //txt_description = (TextView)itemView.findViewById(R.id.home_txt_description);



        }

    }

    public interface ClickListnerForRecyclerView{
        Void layout_clicked(View view, int position);
    }

    public void setClickListner(ClickListnerForRecyclerView itemClickListner){
        this.clickListnerForRecyclerView = itemClickListner;
    }



}
