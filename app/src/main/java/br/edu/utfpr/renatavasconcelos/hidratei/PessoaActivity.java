package br.edu.utfpr.renatavasconcelos.hidratei;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;

public class PessoaActivity extends AppCompatActivity {
    public static final String KEY_NOME = "KEY_NOME";
    public static final String KEY_PESO = "KEY_PESO";
    public static final String KEY_SUGESTAO = "KEY_SUGESTAO";
    public static final String KEY_TIPO = "KEY_TIPO";
    public static final String KEY_GENERO = "KEY_GENERO";


    private EditText editTextNome, editTextPeso;
    private CheckBox checkBoxSugestao;
    private RadioGroup radioGroupGenero;
    private Spinner spinnerTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa);

        setTitle(getString(R.string.novo_cadastro));

        editTextNome = findViewById(R.id.editTextNome);
        editTextPeso = findViewById(R.id.editTextPeso);
        checkBoxSugestao = findViewById(R.id.checkBoxSugestao);
        radioGroupGenero = findViewById(R.id.radioGroupGenero);
        spinnerTipo = findViewById(R.id.spinnerTipo);

       // popularSpinner();

    }

    public void limparDados(){
        editTextNome.setText(null);
        editTextPeso.setText(null);
        checkBoxSugestao.setChecked(false);
        radioGroupGenero.clearCheck();
        spinnerTipo.setSelection(0);

        editTextNome.requestFocus();

        Toast.makeText(this,
                R.string.as_entradas_foram_apagadas,
                Toast.LENGTH_LONG).show();
    }
    public void cadastro(){
        String nome = editTextNome.getText().toString();
        if (nome == null || nome.trim().isEmpty()){
            Toast.makeText( this,
                    R.string.faltou_entrar_com_o_nome,
                    Toast.LENGTH_LONG).show();
            editTextNome.requestFocus();
            return;
        }
        nome =nome.trim();

        String pesoString = editTextPeso.getText().toString();
        if (pesoString == null || pesoString.trim().isEmpty()) {
            Toast.makeText( this,
                    R.string.faltou_colocar_o_peso_atual,
                    Toast.LENGTH_LONG).show();
            editTextPeso.requestFocus();
            return;
        }
        int peso = 0;
        try {
            peso = Integer.parseInt(pesoString);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    R.string.peso_deve_ser_um_numero_inteiro,
                    Toast.LENGTH_LONG).show();
            editTextPeso.requestFocus();
            editTextPeso.setSelection(0, editTextPeso.getText().toString().length());
            return;
        }
        if (peso <= 0){
            Toast.makeText(this,
                    "Peso deve ser maior que 0!",
                    Toast.LENGTH_LONG).show();
            editTextPeso.requestFocus();
            editTextPeso.setSelection(0, editTextPeso.getText().toString().length());
            return;
        }
        int radioButtonId = radioGroupGenero.getCheckedRadioButtonId();

        Genero genero;

        if (radioButtonId == R.id.radioButtonFeminino){
            genero = Genero.Feminino;
        } else
            if (radioButtonId == R.id.radioButtonMasculino){
                genero = Genero.Masculino;
        } else{
                Toast.makeText(this,
                        R.string.faltou_preencher_o_genero,
                        Toast.LENGTH_LONG).show();
                return;
        }
        int tipo = spinnerTipo.getSelectedItemPosition();
        if (tipo == AdapterView.INVALID_POSITION){
            Toast.makeText(this,
                            R.string.faltou_exibir_o_tipo,
                             Toast.LENGTH_LONG).show();
            return;
        }
        boolean sugestao = checkBoxSugestao.isChecked();

        Intent intentResposta = new Intent();

        intentResposta.putExtra(KEY_NOME, nome);
        intentResposta.putExtra(KEY_PESO, peso);
        intentResposta.putExtra(KEY_SUGESTAO, sugestao);
        intentResposta.putExtra(KEY_TIPO, tipo);
        intentResposta.putExtra(KEY_GENERO, genero.toString());

        setResult(PessoaActivity.RESULT_OK, intentResposta);

        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pessoas_opcoes, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemCadastrar){
            cadastro();
            return true;
        }else{
            if (idMenuItem == R.id.menuItemLimpar){
                limparDados();
                return true;
            }else{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}