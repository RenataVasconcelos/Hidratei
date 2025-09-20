package br.edu.utfpr.renatavasconcelos.hidratei;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PessoaRecyclerViewAdapter extends RecyclerView.Adapter<PessoaRecyclerViewAdapter.PessoaHolder> {
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnCreateContextMenu onCreateContextMenu;
    private OnContextMenuClickListener onContextMenuClickListener;
    private Context context;
    private List<Pessoa> listaPessoas;

    private String[] tipos;

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    interface OnCreateContextMenu{
        void onCreateContextMenu(ContextMenu menu,
                                 View v,
                                 ContextMenu.ContextMenuInfo menuInfo,
                                 int position,
                                 MenuItem.OnMenuItemClickListener menuItemClickListener);
    }

    interface OnContextMenuClickListener{
        boolean onContextMenuItemClick(MenuItem menuItem, int position);
    }

    public class PessoaHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                                                                         View.OnLongClickListener,
                                                                         View.OnCreateContextMenuListener{
        public TextView textViewValorNome;
        public TextView textViewValorPeso;
        public TextView textViewValorSugestao;
        public TextView textViewValorTipo;
        public TextView textViewValorGenero;

        public PessoaHolder(@NonNull View itemView) {
            super(itemView);

            textViewValorNome = itemView.findViewById(R.id.textViewValorNome);
            textViewValorPeso = itemView.findViewById(R.id.textViewValorPeso);
            textViewValorSugestao = itemView.findViewById(R.id.textViewValorSugestao);
            textViewValorTipo = itemView.findViewById(R.id.textViewValorTipo);
            textViewValorGenero = itemView.findViewById(R.id.textViewValorGenero);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                return true;

            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (onCreateContextMenu != null){
                onCreateContextMenu.onCreateContextMenu(menu,
                                                        v,
                                                        menuInfo,
                                                        getAdapterPosition(),
                                                        onMenuItemClickListener);
            }
        }
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if (onContextMenuClickListener != null){
                    onContextMenuClickListener.onContextMenuItemClick(item, getAdapterPosition());
                    return true;
                }
                return false;
            }
        };
    }

    public PessoaRecyclerViewAdapter(Context context, List<Pessoa> listaPessoas) {
        this.context = context;
        this.listaPessoas = listaPessoas;

        tipos = context.getResources().getStringArray(R.array.tipos);
    }

    @NonNull
    @Override
    public PessoaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.linha_lista_pessoas, parent, false);
        return new PessoaHolder(convertView);
        }

    @Override
    public void onBindViewHolder(@NonNull PessoaHolder holder, int position) {

        Pessoa pessoa = listaPessoas.get(position);
        holder.textViewValorNome.setText(pessoa.getNome());
        holder.textViewValorPeso.setText(String.valueOf(pessoa.getPeso()));

        if (pessoa.isSugestao()) {
            holder.textViewValorSugestao.setText(R.string.sugestao_meta);
        } else {
            holder.textViewValorSugestao.setText(R.string.nao_quer_sugestao_de_meta);
        }

        holder.textViewValorTipo.setText(tipos[pessoa.getTipo()]);

        switch (pessoa.getGenero()) {

            case Feminino:
                holder.textViewValorGenero.setText(R.string.feminino);
                    break;
            case Masculino:
                holder.textViewValorGenero.setText(R.string.masculino);
                    break;
            }
        }
    @Override
    public int getItemCount () {
        return listaPessoas.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnCreateContextMenu getOnCreateContextMenu() {
        return onCreateContextMenu;
    }

    public void setOnCreateContextMenu(OnCreateContextMenu onCreateContextMenu) {
        this.onCreateContextMenu = onCreateContextMenu;
    }

    public OnContextMenuClickListener getOnContextMenuClickListener() {
        return onContextMenuClickListener;
    }

    public void setOnContextMenuClickListener(OnContextMenuClickListener onContextMenuClickListener) {
        this.onContextMenuClickListener = onContextMenuClickListener;
    }
}