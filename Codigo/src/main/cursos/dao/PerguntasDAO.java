package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Perguntas;

public class PerguntasDAO {
    private DAO conexao;

    public PerguntasDAO() {
    }

    public Perguntas[] getPerguntas() {
        conexao = new DAO();
        Perguntas[] perguntas = null;

        try {
            ResultSet rs = conexao.executeQuery("SELECT * FROM perguntas WHERE boo_ativo");
            if (rs.next()) {
                rs.last();
                perguntas = new Perguntas[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    perguntas[i] = new Perguntas(rs.getInt("id_aula"), rs.getInt("id_pergunta"),
                            rs.getString("descricao"), rs.getInt("id_usuario"));
                }
            }
            conexao.closeStatement();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        conexao.close();
        return perguntas;
    }

    public Perguntas inserirPergunta(String id_aula, String descricao, int id_usuario ) {
        conexao = new DAO();
        Perguntas pergunta = null;
        try {
            String query = "INSERT INTO perguntas (id_aula, descricao, id_usuario) "
                    + "VALUES (" + id_aula + ", '"
                    + descricao + "',"+ id_usuario +") returning id_aula,id_pergunta,descricao,id_usuario;";
            ResultSet rs = conexao.executeQuery(query);
            if (rs.next()) {
                pergunta = new Perguntas(rs.getInt("id_aula"), rs.getInt("id_pergunta"),
                            rs.getString("descricao"), rs.getInt("id_usuario"));
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        conexao.close();
        return pergunta;
    }

    public boolean excluirPergunta(int id_pergunta, int id_usuario) {
        conexao = new DAO();
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("UPDATE perguntas SET boo_ativo = false WHERE id_pergunta = " + id_pergunta  + " AND id_usuario = " + id_usuario);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean atualizarPergunta(Perguntas pergunta, int id_aula, int id_perguntaAlvo) {
        conexao = new DAO();
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE perguntas SET id_aula = " + pergunta.getId_aula() + ", id_pergunta = " + pergunta.getId_pergunta() +
                    ", descricao = '" + pergunta.getDescricao() + "' WHERE id_aula = " + id_aula + " AND id_pergunta = " + id_perguntaAlvo;
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}
