package Adaptador;

import com.iracema.ffik.R;
import Utils.Usuario;
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

public class PromotoresAdaptador extends RecyclerView.Adapter<PromotoresAdaptador.ViewHolder>{
    Context context;
    ArrayList<Usuario> list  = new ArrayList<>();
    ClickListnerForRecyclerView clickListnerForRecyclerView;

    public PromotoresAdaptador(Context context, ArrayList<Usuario> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_para_promotores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Usuario usuario = list.get(position);

        //if (!evento.imagem.equals(""))
        // Picasso.with(context).load(evento.imagem).into(holder.imageView);

        holder.txt_nome.setText("Username: " +usuario.username);
        holder.txt_email.setText("Email: " + usuario.email);

        holder.btn_banir.setOnClickListener(new View.OnClickListener() {
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
