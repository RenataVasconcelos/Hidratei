package br.edu.utfpr.renatavasconcelos.hidratei;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PessoasActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPessoas;
    private RecyclerView.LayoutManager layoutManager;
    private PessoaRecyclerViewAdapter pessoaRecyclerViewAdapter;
    private PessoaRecyclerViewAdapter.OnItemClickListener onItemClickListener;
    private List<Pessoa> listaPessoas;


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

//        listViewPessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent,
//                                    View view,
//                                    int position,
//                                    long id) {
//                Pessoa pessoa = (Pessoa) listViewPessoas.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        getString(R.string.pessoa_de_nome) + pessoa.getNome() + getString(R.string.foi_clicada),
//                        Toast.LENGTH_LONG).show();
//
//
//            }
//        });
        onItemClickListener = new PessoaRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Pessoa pessoa = listaPessoas.get(position);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.pessoa_de_nome) + pessoa.getNome() + getString(R.string.foi_clicada),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Pessoa pessoa = listaPessoas.get(position);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.pessoa_de_nome) + pessoa.getNome() + getString(R.string.recebeu_um_click_longo),
                        Toast.LENGTH_LONG).show();
            }
        };

        popularListaPessoas();
        }

        private void popularListaPessoas(){

            String[] pessoas_nomes = getResources().getStringArray(R.array.pessoas_nome);
            int[] pessoas_peso = getResources().getIntArray(R.array.pessoas_peso);
            int[] pessoas_sugestao = getResources().getIntArray(R.array.pessoas_sugestao);
            int[] pessoas_tipos = getResources().getIntArray(R.array.pessoas_tipos);
            int[] pessoas_genero = getResources().getIntArray(R.array.pessoas_genero);

            listaPessoas = new ArrayList<>();

            Pessoa pessoa;
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
            }

           pessoaRecyclerViewAdapter = new PessoaRecyclerViewAdapter(this, listaPessoas, onItemClickListener);
            recyclerViewPessoas.setAdapter(pessoaRecyclerViewAdapter);
        }

        public void abrirSobre(View view){

            Intent intentAbertura = new Intent(this, SobreActivity.class);
            startActivity(intentAbertura);
        }
    }
