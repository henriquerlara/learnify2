package model;

public class Respostas {
    private int id_pergunta;
    private int id_resposta;
    private String descricao;
    private int id_usuario;

    public Respostas(int id_pergunta, int id_resposta, String descricao, int id_usuario) {
        this.id_pergunta = id_pergunta;
        this.id_resposta = id_resposta;
        this.descricao = descricao;
        this.id_usuario = id_usuario;
    }

    public int getId_pergunta() {
        return id_pergunta;
    }

    public void setId_pergunta(int id_pergunta) {
        this.id_pergunta = id_pergunta;
    }

    public int getId_resposta() {
        return id_resposta;
    }

    public void setId_resposta(int id_resposta) {
        this.id_resposta = id_resposta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
