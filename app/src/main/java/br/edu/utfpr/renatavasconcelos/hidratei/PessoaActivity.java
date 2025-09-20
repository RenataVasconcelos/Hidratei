package br.edu.utfpr.renatavasconcelos.hidratei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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
    public static final String KEY_MODO = "MODO";
    public static final String KEY_SUGERIR_TIPO = "SUGERIR_TIPO";
    public static final String KEY_ULTIMO_TIPO = "ULTIMO_TIPO";

    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;

    private EditText editTextNome, editTextPeso;
    private CheckBox checkBoxSugestao;
    private RadioGroup radioGroupGenero;
    private RadioButton radioButtonFeminino, radioButtonMasculino;
    private Spinner spinnerTipo;
    private int modo;
    private Pessoa pessoaOriginal;
    private boolean sugerirTipo = false;
    private int ultimoTipo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa);

        editTextNome = findViewById(R.id.editTextNome);
        editTextPeso = findViewById(R.id.editTextPeso);
        checkBoxSugestao = findViewById(R.id.checkBoxSugestao);
        radioGroupGenero = findViewById(R.id.radioGroupGenero);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        radioButtonFeminino = findViewById(R.id.radioButtonFeminino);
        radioButtonMasculino = findViewById(R.id.radioButtonMasculino);

        lerPreferencias();

        Intent intentAbertura = getIntent();
        Bundle bundle = intentAbertura.getExtras();

        if (bundle != null){
            modo = bundle.getInt(KEY_MODO);
            if (modo == MODO_NOVO){

                setTitle(getString(R.string.novo_cadastro));

                if (sugerirTipo){
                    spinnerTipo.setSelection(ultimoTipo);
                }

            }else {
                setTitle(getString(R.string.editar_pessoa));

                String nome      = bundle.getString(PessoaActivity.KEY_NOME);
                int peso         = bundle.getInt(PessoaActivity.KEY_PESO);
                boolean sugestao = bundle.getBoolean(PessoaActivity.KEY_SUGESTAO);
                int tipo         = bundle.getInt(PessoaActivity.KEY_TIPO);
                String generoTexto    = bundle.getString(PessoaActivity.KEY_GENERO);

                Genero genero = Genero.valueOf(generoTexto);

                pessoaOriginal = new Pessoa(nome, peso, sugestao, tipo, genero);

                editTextNome.setText(nome);
                editTextPeso.setText(String.valueOf(peso));
                checkBoxSugestao.setChecked(sugestao);
                spinnerTipo.setSelection(tipo);

                if (genero == Genero.Feminino){
                    radioButtonFeminino.setChecked(true);

                }else{
                    if (genero == Genero.Masculino){
                        radioButtonMasculino.setChecked(true);
                    }else{

                    }
                }


            }
        }

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
                    R.string.peso_deve_ser_maior_que_0,
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

        if(modo == MODO_EDITAR &&
                nome.equalsIgnoreCase(pessoaOriginal.getNome()) &&
                peso == pessoaOriginal.getPeso() &&
                sugestao == pessoaOriginal.isSugestao() &&
                tipo == pessoaOriginal.getTipo() &&
                genero == pessoaOriginal.getGenero()){

            setResult(PessoaActivity.RESULT_CANCELED);
            finish();
            return;

        }

        salvarUltimoTipo(tipo);

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
        getMenuInflater().inflate(R.menu.pessoa_opcoes, menu );
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuItemSugerirTipo);

        item.setChecked(sugerirTipo);

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
                if (idMenuItem == R.id.menuItemSugerirTipo){

                    boolean valor = !item.isChecked();
                    salvarSugerirTipo(valor);
                    item.setChecked(valor);

                    if (sugerirTipo){
                        spinnerTipo.setSelection(ultimoTipo);
                    }
                    return true;
                }else{
                    return super.onOptionsItemSelected(item);
                }
            }
        }
    }

    private void lerPreferencias(){

        SharedPreferences shared = getSharedPreferences(PessoasActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        sugerirTipo= shared.getBoolean(KEY_SUGERIR_TIPO, sugerirTipo);
        ultimoTipo = shared.getInt(KEY_ULTIMO_TIPO, ultimoTipo);
    }

    private void salvarSugerirTipo(boolean novoValor){

        SharedPreferences shared = getSharedPreferences(PessoasActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_SUGERIR_TIPO, novoValor);

        editor.commit();

        sugerirTipo = novoValor;
    }

    private void salvarUltimoTipo(int novoValor){
        SharedPreferences shared = getSharedPreferences(PessoasActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(KEY_ULTIMO_TIPO, novoValor);
        editor.commit();

        ultimoTipo = novoValor;
    }
}