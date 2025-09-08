package br.edu.utfpr.renatavasconcelos.hidratei;

import android.content.ContentProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PessoaAdapter extends BaseAdapter {
    private Context context;
    private List<Pessoa> listaPessoas;

    private String[] tipos;

    private static class PessoaHolder{
        public TextView textViewValorNome;
        public TextView textViewValorPeso;
        public TextView textViewValorSugestao;
        public TextView textViewValorTipo;
        public TextView textViewValorGenero;
    }

    public PessoaAdapter(Context context, List<Pessoa> listaPessoas) {
        this.context = context;
        this.listaPessoas = listaPessoas;

        tipos = context.getResources().getStringArray(R.array.tipos);
    }

    @Override
    public int getCount() {
        return listaPessoas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPessoas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PessoaHolder holder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linha_lista_pessoas, parent, false);

            holder = new PessoaHolder();
            holder.textViewValorNome = convertView.findViewById(R.id.textViewValorNome);
            holder.textViewValorPeso = convertView.findViewById(R.id.textViewValorPeso);
            holder.textViewValorSugestao = convertView.findViewById(R.id.textViewValorSugestao);
            holder.textViewValorTipo = convertView.findViewById(R.id.textViewValorTipo);
            holder.textViewValorGenero = convertView.findViewById(R.id.textViewValorGenero);

            convertView.setTag(holder);
        }else{

            holder = (PessoaHolder) convertView.getTag();

        }

        Pessoa pessoa = listaPessoas.get(position);
        holder.textViewValorNome.setText(pessoa.getNome());
        holder.textViewValorPeso.setText(String.valueOf(pessoa.getPeso()));

        if (pessoa.isSugestao()){
            holder.textViewValorSugestao.setText(R.string.sugestao_meta);
        }else{
            holder.textViewValorSugestao.setText(R.string.nao_quer_sugestao_de_meta);
        }

        holder.textViewValorTipo.setText(tipos[pessoa.getTipo()]);

        switch (pessoa.getGenero()){

            case Feminino:
                holder.textViewValorGenero.setText(R.string.feminino);
                break;
            case Masculino:
                holder.textViewValorGenero.setText(R.string.masculino);
                break;
        }

        return convertView;
    }
}
