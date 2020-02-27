package Adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iracema.ffik.R;

import java.util.ArrayList;

import Utils.Codigo;
import Utils.Usuario;

public class CodigosAdaptador extends RecyclerView.Adapter<CodigosAdaptador.ViewHolder>{
    Context context;
    ArrayList<Codigo> list  = new ArrayList<>();
    ClickListnerForRecyclerView clickListnerForRecyclerView;

    public CodigosAdaptador(Context context, ArrayList<Codigo> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_para_codigos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Codigo codigo = list.get(position);

        //if (!evento.imagem.equals(""))
        // Picasso.with(context).load(evento.imagem).into(holder.imageView);

        if (!codigo.status.equals(""))
        {
            holder.txt_nome.setText("Código: " +codigo.codigo + "\nStatus: Utilizado \nIDCliente: "+codigo.cliente_id);
        }else{
            holder.txt_nome.setText("Código: " +codigo.codigo + "\nStatus: Livre");
        }

        //holder.txt_email.setText("Email: " + usuario.email);


    }

    @Override
    public long getItemId(int position){ return position; }

    @Override
    public int getItemCount() { return list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button btn_banir;
        LinearLayout lay_des;
        TextView txt_nome, txt_email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.home_imageview);

            lay_des = (LinearLayout)itemView.findViewById(R.id.home_lay_des);

            btn_banir = (Button)itemView.findViewById(R.id.home_btn_banir);


            //btn_book_now.setVisibility(View.GONE);

            txt_nome = (TextView)itemView.findViewById(R.id.home_txt_nome);
            //txt_email = (TextView)itemView.findViewById(R.id.home_txt_email);


        }

    }

    public interface ClickListnerForRecyclerView{
        Void layout_clicked(View view, int position);
    }

    public void setClickListner(ClickListnerForRecyclerView itemClickListner){
        this.clickListnerForRecyclerView = itemClickListner;
    }
}
