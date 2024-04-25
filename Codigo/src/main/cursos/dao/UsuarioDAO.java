package dao;

// IMPORTS
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.time.LocalDate;

import model.Perguntas;
import model.Usuario;
import dao.DAO;
// END_IMPORTS
public class UsuarioDAO {	
	private DAO conexao;

	public Usuario[] getAllUsuarios() {
		Usuario[] usuario = null;
		conexao = new DAO();
		try {
			ResultSet rs = conexao.executeQuery("SELECT * FROM usuarios");	
	         if(rs.next()){
	             rs.last();
	             usuario = new Usuario[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                usuario[i] = new Usuario(rs.getInt("id_usuario"),
                    rs.getString("email"), rs.getString("senha"), rs.getDate("nascimento"),
                    rs.getBoolean("tipo"), rs.getString("cpf"));
	             }
	          }
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		conexao.close();
		return usuario;
	}
	public Usuario getUsuario(String email, String senha) {
		Usuario usuario = null;
		conexao = new DAO();
		try {
			String sql = String.format("SELECT * FROM usuarios WHERE email = '%s' AND senha = '%s';", email, senha);
			ResultSet rs = conexao.executeQuery(sql);	
			if(rs.next()){
				usuario = new Usuario(rs.getInt("id_usuario"),
				rs.getString("email"),rs.getString("senha"),rs.getDate("nascimento"),
				rs.getBoolean("tipo"),rs.getString("cpf"));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		conexao.close();
		return usuario;
	}
	public Boolean inserirUsuario(String email,String senha,String dat_nascimento,String tipo,String cpf){
		conexao = new DAO();
		try {
			String sql = String.format("insert into usuarios(email,senha,nascimento,tipo,cpf) values('%s','%s','%s','%s','%s')", email, senha,dat_nascimento,tipo,cpf);
			PreparedStatement st = conexao.prepareStatement(sql);
			Boolean retorno = (conexao.executeUpdate(st)!=1) ? false : true;	
			return retorno;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		conexao.close();
		return false;
	}
	public boolean excluirUsuario(int id) {
        conexao = new DAO();
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM usuarios WHERE id_usuario = " + id);
            st.close();
            status = true;
        } catch (SQLException u) {
        	throw new RuntimeException(u);
        }
        return status;
    }
	public boolean atualizarUsuario(int id, String email, String senha) {
        conexao = new DAO();
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE usuarios SET email = '" + email + "', senha = '" + senha + "' WHERE id_usuario = " + id;
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

	// public boolean inserirCurso(Curso curso) {
	// 	boolean status = false;
	// 	conexao = new DAO();
	// 	try {  
	// 		String query = "INSERT INTO cursos (id_curso, preco, id_usuario, categoria, nome, descricao, imagem, banner) "
	// 			       + "VALUES ("+curso.getId_curso()+ ", '" + curso.getPreco() + "', '"  
	// 			       + curso.getId_usuario() + "', '" + curso.getCategoria() + "', '"
	// 				   + curso.getNome() + "', '" + curso.getDescricao() + "', ?, ?);";
	// 		PreparedStatement st = conexao.prepareStatement(query);
	// 		st.setBytes(1, curso.getImagem());
	// 		st.setBytes(2, curso.getBanner());
	// 		conexao.executeUpdate(st);
	// 		st.close();
	// 		status = true;
	// 	} catch (SQLException u) {  
	// 		throw new RuntimeException(u);
	// 	}
	// 	conexao.close();
	// 	return status;
	// }
	
	// public boolean excluirCurso(int id_curso) {
	// 	boolean status = false;
	// 	try {  
	// 		Statement st = conexao.createStatement();
	// 		st.executeUpdate("DELETE FROM cursos WHERE id_curso = " + id_curso);
	// 		st.close();
	// 		status = true;
	// 	} catch (SQLException u) {  
	// 		throw new RuntimeException(u);
	// 	}
	// 	return status;
	// }
	
	// public boolean atualizarCurso(Curso curso, int id_cursoAlvo) {
	// 	boolean status = false;
	// 	try {  
	// 		Statement st = conexao.createStatement();
	// 		String sql = "UPDATE clientes SET id_curso = '" + curso.getId_curso() + "', preco = '"  
	// 			       + curso.getPreco() + "', id_usuario = '" + curso.getId_usuario() + "', categoria = '"
	// 			       + curso.getCategoria() + "', nome = '" + curso.getNome() + "', descricao = '"
	// 			       + curso.getDescricao() + "', imagem = '" + curso.getImagem() + "', banner = '"
	// 			       + curso.getBanner() + "'" + " WHERE id_curso = " + id_cursoAlvo;
	// 		st.executeUpdate(sql);
	// 		st.close();
	// 		status = true;
	// 	} catch (SQLException u) {  
	// 		throw new RuntimeException(u);
	// 	}
	// 	return status;
	// }
	
}