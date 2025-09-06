package br.edu.utfpr.renatavasconcelos.hidratei;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoas);

        listViewPessoas = findViewById(R.id.listViewPessoas);

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

            ArrayAdapter<Pessoa> adapter = new ArrayAdapter<>(this,
                                                                android.R.layout.simple_list_item_1,
                                                                listaPessoas);
            listViewPessoas.setAdapter(adapter);





        }
    }
