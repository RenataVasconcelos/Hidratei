package br.edu.utfpr.renatavasconcelos.hidratei.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.Objects;

@Entity
public class Pessoa implements Cloneable{
        public static Comparator<Pessoa> ordenacaoCrescente = new Comparator<Pessoa>() {
            @Override
            public int compare(Pessoa pessoa1, Pessoa pessoa2) {
                return pessoa1.getNome().compareToIgnoreCase(pessoa2.getNome());

            }
        };

        public static Comparator<Pessoa> ordenacaoDecrescente = new Comparator<Pessoa>() {
            @Override
            public int compare(Pessoa pessoa1, Pessoa pessoa2) {
                return -1 * pessoa1.getNome().compareToIgnoreCase(pessoa2.getNome());

            }
        };

        @PrimaryKey(autoGenerate = true)
        private long id;

        @NonNull
        @ColumnInfo(index = true)
        private String nome;
        private int peso;
        private boolean sugestao;
        private int tipo;
        private Genero genero;

    public Pessoa(String nome, int peso, boolean sugestao, int tipo, Genero genero) {
        this.nome = nome;
        this.peso = peso;
        this.sugestao = sugestao;
        this.tipo = tipo;
        this.genero = genero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public boolean isSugestao() {
        return sugestao;
    }

    public void setSugestao(boolean sugestao) {
        this.sugestao = sugestao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    @NonNull

    @Override
    public Object clone() throws CloneNotSupportedException {

        return super.clone();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass())
            return false;

        Pessoa pessoa = (Pessoa) o;

        return peso == pessoa.peso &&
                sugestao == pessoa.sugestao &&
                tipo == pessoa.tipo &&
                nome.equals(pessoa.nome) &&
                genero == pessoa.genero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, peso, sugestao, tipo, genero);
    }

    @Override
    public String toString() {
        return  nome + "\n" +
                peso + "\n" +
                sugestao + "\n" +
                tipo + "\n" +
                genero;
    }
}

