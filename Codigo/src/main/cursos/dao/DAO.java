package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DAO {
    private Connection conexao;
    private Statement stmt;
	
	public DAO() {
		conexao = null;
		conectar();
	}
	
	public boolean conectar() {
		String driverName = "org.postgresql.Driver";                    
		String serverName = "paulopuc.postgres.database.azure.com";
		String mydatabase = "postgres";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
		String username = "paulo";
		String password = "Ph09351234";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
		} catch (ClassNotFoundException e) { 
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}

    public Statement createStatement() throws SQLException {
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt;
    }
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        createStatement();
        return conexao.prepareStatement(sql);
    }
    
    public int executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        // Func para CREATE, INSERT E DELETE
        return preparedStatement.executeUpdate();
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        // Func apenas para READ
        createStatement();
        return stmt.executeQuery(sql);
    }

    public boolean closeStatement() {
        boolean status = false;

        try {
            if (stmt != null) {
                stmt.close();
                status = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    public boolean close() {
        boolean status = false;

        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conexao != null) {
                conexao.close();
            }
            status = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return status;
    }
    
}
