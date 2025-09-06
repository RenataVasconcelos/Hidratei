package br.edu.utfpr.renatavasconcelos.hidratei;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PessoaActivity extends AppCompatActivity {
    private EditText editTextNome, editTextPeso;
    private CheckBox checkBoxSugestao;
    private RadioGroup radioGroupGenero;
    private Spinner spinnerTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa);

        editTextNome = findViewById(R.id.editTextNome);
        editTextPeso = findViewById(R.id.editTextPeso);
        checkBoxSugestao = findViewById(R.id.checkBoxSugestao);
        radioGroupGenero = findViewById(R.id.radioGroupGenero);
        spinnerTipo = findViewById(R.id.spinnerTipo);

       // popularSpinner();

    }
//        private void popularSpinner(){
//            ArrayList<String> lista = new ArrayList<>();
//            lista.add(getString(R.string.ml));
//            lista.add(getString(R.string.litros));
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                                                            android.R.layout.simple_list_item_1,
//                                                            lista);
//            spinnerTipo.setAdapter(adapter);
//        }
    public void limparDados(View view){
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
    public void cadastro(View view){
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
        String genero;
        if (radioButtonId == R.id.radioButtonFeminino){
            genero = getString(R.string.feminino);
        } else
            if (radioButtonId == R.id.radioButtonMasculino){
                genero = getString(R.string.masculino);
        } else{
                Toast.makeText(this,
                        R.string.faltou_preencher_o_genero,
                        Toast.LENGTH_LONG).show();
                return;
        }
        String tipo = (String) spinnerTipo.getSelectedItem();
        if (tipo == null){
            Toast.makeText(this,
                            R.string.faltou_exibir_o_tipo,
                             Toast.LENGTH_LONG).show();
            return;
        }
        boolean sugestao = checkBoxSugestao.isChecked();

        Toast.makeText(this,
                    getString(R.string.nome_valor) + nome + "\n" +
                        getString(R.string.peso_valor) + peso + "\n" +
                        (sugestao ? getString(R.string.sugestao_meta) : getString(R.string.nao_quer_sugestao_de_meta)) + "\n" +
                        getString(R.string.genero_usado) + genero + "\n" +
                        getString(R.string.tipo_valor) + tipo,
                        Toast.LENGTH_LONG).show();
    }
}