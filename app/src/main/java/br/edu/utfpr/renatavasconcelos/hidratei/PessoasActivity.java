package br.edu.utfpr.renatavasconcelos.hidratei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class PessoasActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPessoas;
    private RecyclerView.LayoutManager layoutManager;
    private PessoaRecyclerViewAdapter pessoaRecyclerViewAdapter;
    private List<Pessoa> listaPessoas;

    private int posicaoSelecionada = -1;
    private ActionMode actionMode;
    private View viewSelecionada;
    private Drawable backgroundDrawable;
    public static final String ARQUIVO_PREFERENCIAS = "br.edu.utfpr.renatavasconcelos.hidratei.PREFERENCIAS";
    public static final String KEY_ORDENACAO_ASCENDENTE = "ORDENACAO_ASCENDENTE";
    public static final boolean PADRAO_INICIAL_ORDENACAO_ASCENDENTE = true;
    private boolean ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;
    private MenuItem menuItemOrdenacao;
    private ActionMode.Callback actionCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.pessoas_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int idMenuItem = item.getItemId();
            if (idMenuItem == R.id.menuItemEditar){
                editarPessoa();
                return true;
            }else {
                if (idMenuItem == R.id.menuItemExcluir){
                    excluirPessoa();
                    mode.finish();
                    return true;
                }else{
                    return false;
                }
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (viewSelecionada != null){
                viewSelecionada.setBackground(backgroundDrawable);
            }

            actionMode = null;
            viewSelecionada = null;
            backgroundDrawable = null;

            recyclerViewPessoas.setEnabled(true);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoas);

        setTitle(getString(R.string.controle_de_pessoas));

        recyclerViewPessoas = findViewById(R.id.recycleViewPessoas);

        layoutManager = new LinearLayoutManager(this);
        recyclerViewPessoas.setLayoutManager(layoutManager);
        recyclerViewPessoas.setHasFixedSize(true);
        recyclerViewPessoas.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        lerPreferencias();

        popularListaPessoas();

        }

        private void popularListaPessoas(){

//            String[] pessoas_nomes = getResources().getStringArray(R.array.pessoas_nome);
//            int[] pessoas_peso = getResources().getIntArray(R.array.pessoas_peso);
//            int[] pessoas_sugestao = getResources().getIntArray(R.array.pessoas_sugestao);
//            int[] pessoas_tipos = getResources().getIntArray(R.array.pessoas_tipos);
//            int[] pessoas_genero = getResources().getIntArray(R.array.pessoas_genero);

            listaPessoas = new ArrayList<>();

           /* Pessoa pessoa;
            boolean sugestao;
            Genero genero;

            Genero[] generos = Genero.values();

            for (int cont = 0; cont < pessoas_nomes.length; cont++){

                sugestao = (pessoas_sugestao[cont] == 1 ? true : false);

                genero = generos[pessoas_genero[cont]];

                pessoa = new Pessoa(pessoas_nomes[cont],
                                    pessoas_peso[cont],
                                    sugestao,
                                    pessoas_tipos[cont],
                                    genero);

                listaPessoas.add(pessoa);
            }*/

           pessoaRecyclerViewAdapter = new PessoaRecyclerViewAdapter(this, listaPessoas);

           pessoaRecyclerViewAdapter.setOnItemClickListener(new PessoaRecyclerViewAdapter.OnItemClickListener() {
               @Override
               public void onItemClick(View view, int position) {
                   posicaoSelecionada = position;
                   editarPessoa();
               }
           });

           pessoaRecyclerViewAdapter.setOnItemLongClickListener(new PessoaRecyclerViewAdapter.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(View view, int position) {

                   if (actionMode != null){
                       return false;
                   }

                   posicaoSelecionada =  position;

                   viewSelecionada = view;
                   backgroundDrawable = view.getBackground();

                   view.setBackgroundColor(Color.LTGRAY);

                   recyclerViewPessoas.setEnabled(false);

                   actionMode = startSupportActionMode(actionCallBack);
                   return true;
               }
           });

           recyclerViewPessoas.setAdapter(pessoaRecyclerViewAdapter);
        }

        public void abrirSobre(){

            Intent intentAbertura = new Intent(this, SobreActivity.class);
            startActivity(intentAbertura);
        }
        ActivityResultLauncher<Intent> launcherNovaPessoa = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == PessoasActivity.RESULT_OK){

                            Intent intent = result.getData();
                            Bundle bundle = intent.getExtras();

                            if (bundle != null){
                                String nome      = bundle.getString(PessoaActivity.KEY_NOME);
                                int peso         = bundle.getInt(PessoaActivity.KEY_PESO);
                                boolean sugestao = bundle.getBoolean(PessoaActivity.KEY_SUGESTAO);
                                int tipo         = bundle.getInt(PessoaActivity.KEY_TIPO);
                                String generoTexto    = bundle.getString(PessoaActivity.KEY_GENERO);

                                Pessoa pessoa = new Pessoa(nome, peso, sugestao, tipo, Genero.valueOf(generoTexto));

                                listaPessoas.add(pessoa);

                                ordenarLista();
                            }
                        }
                    }
                });

    public void abrirNovaPessoa(){

        Intent intentAbertura = new Intent(this, PessoaActivity.class);
        intentAbertura.putExtra(PessoaActivity.KEY_MODO, PessoaActivity.MODO_NOVO);
        launcherNovaPessoa.launch(intentAbertura);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pessoas_opcoes, menu);
        menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        atualizarIconeOrdenacao();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemAdicionar){
            abrirNovaPessoa();
            return true;
        }else{
            if (idMenuItem == R.id.menuItemSobre){
                abrirSobre();
                return true;
            }else{
                if (idMenuItem == R.id.menuItemOrdenacao){
                    salvarPreferenciaOrdenacaoAscendente(!ordenacaoAscendente);
                    atualizarIconeOrdenacao();
                    ordenarLista();
                    return true;
                }else{
                    if (idMenuItem == R.id.menuItemRestaurar){
                        restaurarPadroes();
                        atualizarIconeOrdenacao();
                        ordenarLista();

                        Toast.makeText(this,
                                        R.string.as_configuracoes_voltaram_para_o_padrao_de_instalacao,
                                        Toast.LENGTH_LONG).show();
                        return true;
                    }else{
                        return super.onOptionsItemSelected(item);
                    }
                }
            }
        }
    }


    private void excluirPessoa(){
        listaPessoas.remove(posicaoSelecionada);
        pessoaRecyclerViewAdapter.notifyDataSetChanged();

    }

    ActivityResultLauncher<Intent> launcherEditarPessoa = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == PessoasActivity.RESULT_OK){

                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();

                        if (bundle != null){
                            String nome      = bundle.getString(PessoaActivity.KEY_NOME);
                            int peso         = bundle.getInt(PessoaActivity.KEY_PESO);
                            boolean sugestao = bundle.getBoolean(PessoaActivity.KEY_SUGESTAO);
                            int tipo         = bundle.getInt(PessoaActivity.KEY_TIPO);
                            String generoTexto    = bundle.getString(PessoaActivity.KEY_GENERO);

                            Pessoa pessoa = listaPessoas.get(posicaoSelecionada);
                            pessoa.setNome(nome);
                            pessoa.setPeso(peso);
                            pessoa.setSugestao(sugestao);
                            pessoa.setTipo(tipo);

                            Genero genero = Genero.valueOf(generoTexto);
                            pessoa.setGenero(genero);

                            ordenarLista();
                        }
                    }
                    posicaoSelecionada = -1;
                    if (actionMode != null){
                        actionMode.finish();
                    }
                }
            });
    private void editarPessoa(){

        Pessoa pessoa = listaPessoas.get(posicaoSelecionada);

        Intent intentAbertura = new Intent(this, PessoaActivity.class);

        intentAbertura.putExtra(PessoaActivity.KEY_MODO, PessoaActivity.MODO_EDITAR);
        intentAbertura.putExtra(PessoaActivity.KEY_NOME, pessoa.getNome());
        intentAbertura.putExtra(PessoaActivity.KEY_PESO, pessoa.getPeso());
        intentAbertura.putExtra(PessoaActivity.KEY_SUGESTAO, pessoa.isSugestao());
        intentAbertura.putExtra(PessoaActivity.KEY_TIPO, pessoa.getTipo());
        intentAbertura.putExtra(PessoaActivity.KEY_GENERO, pessoa.getGenero().toString());

        launcherEditarPessoa.launch(intentAbertura);
    }

    private void ordenarLista(){

        if (ordenacaoAscendente){
            Collections.sort(listaPessoas, Pessoa.ordenacaoCrescente);
        }else{
            Collections.sort(listaPessoas, Pessoa.ordenacaoDecrescente);
        }

        pessoaRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void atualizarIconeOrdenacao(){
        if (ordenacaoAscendente){
            menuItemOrdenacao.setIcon(R.drawable.ic_action_ascending_order);
        }else{
            menuItemOrdenacao.setIcon(R.drawable.ic_action_decending_order);
        }
    }
    private void lerPreferencias(){
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        ordenacaoAscendente = shared.getBoolean(KEY_ORDENACAO_ASCENDENTE, ordenacaoAscendente);
    }

    private void salvarPreferenciaOrdenacaoAscendente(boolean novoValor){
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_ORDENACAO_ASCENDENTE, novoValor);

        editor.commit();

        ordenacaoAscendente = novoValor;
    }

    private void restaurarPadroes(){
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.clear();
        editor.commit();

        ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;
    }
}
