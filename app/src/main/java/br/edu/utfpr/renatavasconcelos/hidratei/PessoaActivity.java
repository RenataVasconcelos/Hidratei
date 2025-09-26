package br.edu.utfpr.renatavasconcelos.hidratei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import br.edu.utfpr.renatavasconcelos.hidratei.modelo.Genero;
import br.edu.utfpr.renatavasconcelos.hidratei.modelo.Pessoa;
import br.edu.utfpr.renatavasconcelos.hidratei.persistencia.PessoasDatabase;
import br.edu.utfpr.renatavasconcelos.hidratei.utils.UtilsAlert;

public class PessoaActivity extends AppCompatActivity {

    public static final String KEY_MODO = "MODO";
    public static final String KEY_ID = "ID";

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

                long id = bundle.getLong(KEY_ID);

                PessoasDatabase database= PessoasDatabase.getInstance(this);

                pessoaOriginal = database.getPessoaDao().queryForId(id);


                editTextNome.setText(pessoaOriginal.getNome());
                editTextPeso.setText(String.valueOf(pessoaOriginal.getPeso()));
                checkBoxSugestao.setChecked(pessoaOriginal.isSugestao());
                spinnerTipo.setSelection(pessoaOriginal.getTipo());

                Genero genero = pessoaOriginal.getGenero();

                if (genero == Genero.Feminino){
                    radioButtonFeminino.setChecked(true);
                }else{
                    if (genero == Genero.Masculino){
                        radioButtonMasculino.setChecked(true);
                    }
                }

                editTextNome.requestFocus();
                editTextNome.setSelection(editTextNome.getText().length());


            }
        }

       // popularSpinner();

    }

    public void limparDados(){

        final String nome = editTextNome.getText().toString();
        final String peso = editTextPeso.getText().toString();
        final boolean sugestao = checkBoxSugestao.isChecked();
        final int radioButtonId = radioGroupGenero.getCheckedRadioButtonId();
        final int tipo = spinnerTipo.getSelectedItemPosition();

        final ScrollView scrollView = findViewById(R.id.main);
        final View viewComFoco = scrollView.findFocus();

        editTextNome.setText(null);
        editTextPeso.setText(null);
        checkBoxSugestao.setChecked(false);
        radioGroupGenero.clearCheck();
        spinnerTipo.setSelection(0);
        editTextNome.requestFocus();

        Snackbar snackbar = Snackbar.make(scrollView,
                                          R.string.as_entradas_foram_apagadas,
                                          Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.desfazer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNome.setText(nome);
                editTextPeso.setText(peso);
                checkBoxSugestao.setChecked(sugestao);


                if (radioButtonId == R.id.radioButtonFeminino){
                    radioButtonFeminino.setChecked(true);

                }else {
                    if (radioButtonId == R.id.radioButtonMasculino) {
                        radioButtonMasculino.setChecked(true);
                    }
                }
                spinnerTipo.setSelection(tipo);

                if (viewComFoco != null){
                    viewComFoco.requestFocus();
                }


            }
        });

        snackbar.show();
    }
    public void cadastro(){

        String nome = editTextNome.getText().toString();

        if (nome == null || nome.trim().isEmpty()){
            UtilsAlert.mostrarAviso(this, R.string.faltou_entrar_com_o_nome);
            editTextNome.requestFocus();
            return;
        }
        nome = nome.trim();

        String pesoString = editTextPeso.getText().toString();

        if (pesoString == null || pesoString.trim().isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.faltou_colocar_o_peso_atual);
            editTextPeso.requestFocus();
            return;
        }
        int peso = 0;

        try {
            peso = Integer.parseInt(pesoString);

        } catch (NumberFormatException e) {
            UtilsAlert.mostrarAviso(this, R.string.peso_deve_ser_um_numero_inteiro);
            editTextPeso.requestFocus();
            editTextPeso.setSelection(0, editTextPeso.getText().toString().length());
            return;
        }
        if (peso <= 0){
            UtilsAlert.mostrarAviso(this, R.string.peso_deve_ser_maior_que_0);

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
                UtilsAlert.mostrarAviso(this, R.string.faltou_preencher_o_genero);
                return;
        }
        int tipo = spinnerTipo.getSelectedItemPosition();
        if (tipo == AdapterView.INVALID_POSITION){
            UtilsAlert.mostrarAviso(this, R.string.faltou_exibir_o_tipo);
            return;
        }
        boolean sugestao = checkBoxSugestao.isChecked();

        Pessoa pessoa = new Pessoa(nome, peso, sugestao, tipo, genero);

        if (pessoa.equals(pessoaOriginal)){

            setResult(PessoaActivity.RESULT_CANCELED);
            finish();
            return;

        }

        Intent intentResposta = new Intent();

        PessoasDatabase database = PessoasDatabase.getInstance(this);

        if (modo == MODO_NOVO){

            long novoId = database.getPessoaDao().insert(pessoa);

            if (novoId <= 0){
                UtilsAlert.mostrarAviso(this,
                                    R.string.erro_ao_tentar_inserir);
                return;
            }
            pessoa.setId(novoId);

        }else{

            pessoa.setId(pessoaOriginal.getId());

            int quantidadeAlterada = database.getPessoaDao().update(pessoa);

            if (quantidadeAlterada != 1){
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_tentar_alterar);
                return;
            }

        }

        salvarUltimoTipo(tipo);

        intentResposta.putExtra(KEY_ID, pessoa.getId());

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