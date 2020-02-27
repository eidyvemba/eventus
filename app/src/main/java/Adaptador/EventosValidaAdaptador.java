package Adaptador;

import com.iracema.ffik.R;

import Utils.ConexaoBD;
import Utils.Evento;
import Utils.GetImageURL;

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

public class EventosValidaAdaptador extends RecyclerView.Adapter<EventosValidaAdaptador.ViewHolder>{
    Context context;
    ArrayList<Evento> list  = new ArrayList<>();
    ClickListnerForRecyclerView clickListnerForRecyclerView;
    public EventosValidaAdaptador(Context context, ArrayList<Evento> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_para_eventos_validar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Evento evento = list.get(position);

        //if (!evento.imagem.equals(""))
        // Picasso.with(context).load(evento.imagem).into(holder.imageView);
        if (!evento.imagem.equals("") && !evento.imagem.equals(" ")) new GetImageURL(holder.imageView).execute(ConexaoBD.Host+"/webservice/"+evento.imagem);

        holder.txt_localizacao.setText("Local:" + evento.localizacao);
        //holder.txt_description.setText(evento.descricao);
        holder.txt_name.setText(evento .tipo + " : "+evento.titulo);
        holder.txt_preco_normal.setText("Preço Normal: " + evento.preco_normal+" Kz");
        holder.txt_preco_vip.setText("Preço Vip: " + evento.preco_vip+" Kz");
        holder.txt_data_i.setText("Data Inicio: " + evento.data_inicio + " " +evento.tempo_inicio);
        holder.txt_data_f.setText("Data Fim: " +evento.data_fim + " "+evento.tempo_fim);

        holder.lay_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v, position);
                }
            }
        });

        holder.btn_validar.setOnClickListener(new View.OnClickListener() {
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
        Button btn_validar;
        LinearLayout lay_des;
        TextView txt_name, txt_localizacao, txt_description, txt_data_i, txt_data_f, txt_preco_normal, txt_preco_vip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.home_imageview);

            lay_des = (LinearLayout)itemView.findViewById(R.id.home_lay_des);

            btn_validar = (Button)itemView.findViewById(R.id.home_btn_validar);
            //btn_book_now.setVisibility(View.GONE);

            txt_data_i = (TextView)itemView.findViewById(R.id.home_txt_data_i);
            txt_data_f = (TextView)itemView.findViewById(R.id.home_txt_data_f);
            txt_name = (TextView)itemView.findViewById(R.id.home_txt_nome);
            txt_localizacao = (TextView)itemView.findViewById(R.id.home_txt_localizacao);
            txt_preco_normal = (TextView)itemView.findViewById(R.id.home_txt_preco_normal);
            txt_preco_vip = (TextView)itemView.findViewById(R.id.home_txt_preco_vip);

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
