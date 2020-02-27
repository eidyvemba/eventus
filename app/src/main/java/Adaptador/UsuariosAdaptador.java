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

import Utils.Usuario;

public class UsuariosAdaptador extends RecyclerView.Adapter<UsuariosAdaptador.ViewHolder>{
    Context context;
    ArrayList<Usuario> list  = new ArrayList<>();
    ClickListnerForRecyclerView clickListnerForRecyclerView;

    public UsuariosAdaptador(Context context, ArrayList<Usuario> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_para_utilizadores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Usuario usuario = list.get(position);

        //if (!evento.imagem.equals(""))
        // Picasso.with(context).load(evento.imagem).into(holder.imageView);

        holder.txt_nome.setText("Username: " +usuario.nome);
        holder.txt_email.setText("Email: " + usuario.email);

        holder.btn_banir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListnerForRecyclerView!=null){
                    clickListnerForRecyclerView.layout_clicked(v, position);
                }
            }
        });

        holder.btn_editar.setOnClickListener(new View.OnClickListener() {
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
        Button btn_banir,btn_editar;
        LinearLayout lay_des;
        TextView txt_nome, txt_email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.home_imageview);

            lay_des = (LinearLayout)itemView.findViewById(R.id.home_lay_des);

            btn_banir = (Button)itemView.findViewById(R.id.home_btn_banir);
            btn_editar = (Button)itemView.findViewById(R.id.home_btn_editar);

            //btn_book_now.setVisibility(View.GONE);

            txt_nome = (TextView)itemView.findViewById(R.id.home_txt_nome);
            txt_email = (TextView)itemView.findViewById(R.id.home_txt_email);


        }

    }

    public interface ClickListnerForRecyclerView{
        Void layout_clicked(View view, int position);
    }

    public void setClickListner(ClickListnerForRecyclerView itemClickListner){
        this.clickListnerForRecyclerView = itemClickListner;
    }
}
