package connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {
    private static final String url = "jdbc:mysql://localhost:3306/sistema_nutricional";
    private static final String usuario = "root";
    private static final String senha = "";
    private static Connection conexao;

    public static Connection get() {
        try {
            // Mantém explícito o carregamento do driver JDBC do MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");

            if (conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(url, usuario, senha);
            }
            return conexao;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Driver MySQL não encontrado. Adicione libs/mysql-connector-j-26.7.0.jar " +
                            "às dependências do módulo no IntelliJ.", e);
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Não foi possível conectar ao MySQL. Verifique se o MySQL está ligado, " +
                            "se o banco sistema_nutricional existe e se usuário/senha estão corretos.", e);
        }
    }
}
