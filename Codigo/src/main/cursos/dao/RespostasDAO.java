package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Respostas;

public class RespostasDAO {
    private DAO conexao;

    public RespostasDAO() {
    }

    public Respostas[] getRespostas() {
        conexao = new DAO();
        Respostas[] respostas = null;

        try {
            ResultSet rs = conexao.executeQuery("SELECT * FROM respostas WHERE boo_ativo");
            if (rs.next()) {
                rs.last();
                respostas = new Respostas[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    respostas[i] = new Respostas(rs.getInt("id_pergunta"), rs.getInt("id_resposta"),
                            rs.getString("descricao"), rs.getInt("id_usuario"));
                }
            }
            conexao.closeStatement();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        conexao.close();
        return respostas;
    }

    public Respostas inserirResposta(String id_pergunta, String descricao, int id_usuario) {
        conexao = new DAO();
        Respostas resposta = null;
        try {
            String query = "INSERT INTO respostas (id_pergunta, descricao, id_usuario) "
                    + "VALUES (" + id_pergunta + ", '"
                    + descricao + "', " + id_usuario + ") returning id_pergunta, id_resposta, descricao, id_usuario;";
            ResultSet rs = conexao.executeQuery(query);
            if (rs.next()) {
                resposta = new Respostas(rs.getInt("id_pergunta"), rs.getInt("id_resposta"),
                            rs.getString("descricao"), rs.getInt("id_usuario"));
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        conexao.close();
        return resposta;
    }
    public boolean atualizarResposta(Respostas resposta, int id_pergunta, int id_respostaAlvo) {
        conexao = new DAO();
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE respostas SET id_pergunta = " + resposta.getId_pergunta() + ", id_resposta = " + resposta.getId_resposta()
                    + ", descricao = '" + resposta.getDescricao() + "', id_usuario = " + resposta.getId_usuario()
                    + " WHERE id_pergunta = " + id_pergunta + " AND id_resposta = " + id_respostaAlvo;
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    public boolean excluirResposta(int id_resposta, int id_usuario) {
        conexao = new DAO();
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("UPDATE respostas SET boo_ativo = false WHERE id_resposta = " + id_resposta  + " AND id_usuario = " + id_usuario);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}
