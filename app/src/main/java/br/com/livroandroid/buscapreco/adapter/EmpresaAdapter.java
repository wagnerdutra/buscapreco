package br.com.livroandroid.buscapreco.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.domain.EmpresaFavDB;
import br.com.livroandroid.buscapreco.model.Empresa;
import livroandroid.lib.utils.Prefs;

public class EmpresaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final String TAG = "livroandroid";
    private final List<Empresa> Empresas;
    private final Context context;
    private final EmpresaOnClickListener onClickListener;
    private final EmpresaFavDB empresaFavDB;

    public interface EmpresaOnClickListener {
        public void onClickEmpresa(EmpresasViewHolder holder, int idx);
        public void onClickCheckBox(EmpresasViewHolder holder, int idx, CheckBox cb);
        public void onClickInfo(EmpresasViewHolder holder, int idx);
    }

    public EmpresaAdapter(Context context, List<Empresa> Empresas, EmpresaOnClickListener onClickListener) {
        this.context = context;
        this.Empresas = Empresas;
        this.onClickListener = onClickListener;
        this.empresaFavDB = new EmpresaFavDB(context);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        if (viewType==0){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_nome_cidade, viewGroup, false);
            viewHolder = new CidadeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_empresacard, viewGroup, false);
            viewHolder = new EmpresasViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType()==0){
            Empresa e = Empresas.get(0);
            CidadeViewHolder cidadeViewHolder = (CidadeViewHolder) holder;
            cidadeViewHolder.tvCidade.setText(e.getCidade().concat(" - "+e.getEstado()+" " +
                "\n\nÚltima atualização: "+ Prefs.getString(context, "dh"+e.getCidade()+e.getEstado())+"\n"));
        } else {
            Empresa e = Empresas.get(position-1);

            final EmpresasViewHolder empresasViewHolder = (EmpresasViewHolder) holder;

            empresasViewHolder.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(e.getUrlFoto()).fit().into(empresasViewHolder.imagem,
                new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                        empresasViewHolder.progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError() {
                        empresasViewHolder.progressBar.setVisibility(View.GONE);
                        empresasViewHolder.imagem.setImageResource(R.drawable.semimagem);
                    }
                });

            empresasViewHolder.eNome.setText(e.getNome());
            empresasViewHolder.eEnd.setText(e.getRua().concat(", "+e.getNumero()));
            empresasViewHolder.eTel.setText(e.getTel());
            empresasViewHolder.eHora.setText(e.getHoraInicio().concat("-".concat(e.getHoraFim())));
            empresasViewHolder.checkBox.setChecked(empresaFavDB.existeFav(e.getId()));
            empresasViewHolder.eTipo.setText(e.getTipo());

            // Click
            if (onClickListener != null) {
                empresasViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClickEmpresa(empresasViewHolder, position);
                    }
                });

                empresasViewHolder.btInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClickInfo(empresasViewHolder,position);
                    }
                });

                empresasViewHolder.checkBox.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClickCheckBox(empresasViewHolder, position,(CheckBox) v);
                    }
                });
            }
        }
    }

    public List<Empresa> getEmpresas(){
        return Empresas;
    }

    @Override
    public int getItemCount() {
        return this.Empresas != null ? this.Empresas.size()+1 : 0;
    }

    // Subclasse de RecyclerView.ViewHolder. Contém todas as views.
    public static class EmpresasViewHolder extends RecyclerView.ViewHolder {
        public TextView eNome;
        public TextView eEnd;
        public TextView eTel;
        public TextView eHora;
        public ImageButton btInfo;
        private View view;
        public CheckBox checkBox;
        public ImageView imagem;
        public TextView eTipo;
        public ProgressBar progressBar;

        public EmpresasViewHolder(View view) {
            super(view);
            this.view = view;
            // Cria as views para salvar no ViewHolder
            imagem = (ImageView) view.findViewById(R.id.imgTipoEmpresa);
            btInfo = (ImageButton) view.findViewById(R.id.btInfo);
            eNome = (TextView) view.findViewById(R.id.eNome);
            eEnd = (TextView) view.findViewById(R.id.eEnd);
            eTel = (TextView) view.findViewById(R.id.eTel);
            eHora = (TextView) view.findViewById(R.id.eHora);
            eTipo = (TextView) view.findViewById(R.id.eTipo);
            checkBox = (CheckBox) view.findViewById(R.id.chFav);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

    public static class CidadeViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCidade;
        private View view;

        public CidadeViewHolder(View view) {
            super(view);
            this.view = view;

            tvCidade = (TextView) view.findViewById(R.id.tvCidade);
        }
    }
}