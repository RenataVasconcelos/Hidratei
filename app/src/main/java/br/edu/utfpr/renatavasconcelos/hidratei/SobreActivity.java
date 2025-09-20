package br.edu.utfpr.renatavasconcelos.hidratei;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        setTitle(R.string.sobre);
    }

    public void abrirSiteAutoria(View view){
        abrirSite("https://github.com/RenataVasconcelos");
    }
    private void abrirSite(String endereco){
        Intent intentAbertura = new Intent(Intent.ACTION_VIEW);

        intentAbertura.setData(Uri.parse(endereco));

        if(intentAbertura.resolveActivity(getPackageManager()) != null ){
            startActivity(intentAbertura);
        }else {
            Toast.makeText(this,
                    R.string.nenhum_aplicativo_para_abrir_paginas_web,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void enviarEmailAutor(View view){
        enviarEmail(new String[]{"renatavasconcelos@alunos.utfpr.edu.br"},
                          getString(R.string.contato_pelo_aplicativo));


    }

    private void enviarEmail(String[] endereco, String assunto) {
        Intent intentAbertura = new Intent(Intent.ACTION_SENDTO);

        intentAbertura.setData(Uri.parse("mailto:"));
        intentAbertura.putExtra(Intent.EXTRA_EMAIL, endereco);
        intentAbertura.putExtra(Intent.EXTRA_SUBJECT, assunto);

        if (intentAbertura.resolveActivity(getPackageManager()) != null) {
            startActivity(intentAbertura);
        } else {
            Toast.makeText(this,
                    R.string.nenhum_aplicativo_para_enviar_um_email,
                    Toast.LENGTH_LONG).show();
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == android.R.id.home){
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }*/
}