package model;

import java.sql.Date;

public class Usuario {
    private int id_usuario;
    private String email;
    private String senha;
    private Date nascimento;
    private boolean tipo;
    private String cpf;

    // CONSTRUCTOR
    public Usuario(int id_usuario, String email, String senha,Date nascimento, boolean tipo, String cpf){
        this.id_usuario = id_usuario;
        this.email = email;
        this.senha = senha;
        this.nascimento = nascimento;
        this.tipo = tipo;
        this.cpf = cpf;
    }
    public Usuario(){

    }

    // GETTERS
    public int getId_usuario() {
        return id_usuario;
    }
    public String getEmail() {
        return email;
    }
    public String getSenha() {
        return senha;
    }
    public Date getDataNascimento() {
        return nascimento;
    }
    public String getCpf() {
        return cpf;
    }
    public Boolean getTipo(){
        return tipo;
    }
    // SETTERS
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }
    public void setDataNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }
}
