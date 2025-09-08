package br.edu.utfpr.renatavasconcelos.hidratei;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class PessoasActivity extends AppCompatActivity {

    private ListView listViewPessoas;

    private List<Pessoa> listaPessoas;

    private PessoaAdapter pessoaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoas);

        listViewPessoas = findViewById(R.id.listViewPessoas);

        listViewPessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                Pessoa pessoa = (Pessoa) listViewPessoas.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.pessoa_de_nome) + pessoa.getNome() + getString(R.string.foi_clicada),
                        Toast.LENGTH_LONG).show();


            }
        });
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

           pessoaAdapter = new PessoaAdapter(this, listaPessoas);
            listViewPessoas.setAdapter(pessoaAdapter);





        }
    }
