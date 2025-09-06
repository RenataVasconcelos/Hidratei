package br.edu.utfpr.renatavasconcelos.hidratei;

public class Pessoa {

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

    @Override
    public String toString() {
        return  nome + "\n" +
                peso + "\n" +
                sugestao + "\n" +
                tipo + "\n" +
                genero;
    }
}

