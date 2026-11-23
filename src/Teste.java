import connections.ConexaoMySQL;
import tests.ConsultaTest;
import tests.NutricionistaTest;
import tests.PacienteTest;
import tests.RotinaTest;

import java.sql.Connection;
import java.sql.ResultSet;

public class Teste {
    public static void main(String[] args) {
        Connection conexao = null;
        try {
            // Os testes rodam dentro de uma transação que no final é desfeita (rollback),
            // então nada do que eles cadastram fica gravado no banco.
            conexao = ConexaoMySQL.get();

            // Sem InnoDB o rollback não funciona e os dados de teste ficariam gravados.
            ResultSet tabelas = conexao.prepareStatement(
                    "SELECT table_name FROM information_schema.tables " +
                            "WHERE table_schema='sistema_nutricionista' AND engine <> 'InnoDB'").executeQuery();
            if (tabelas.next()) {
                System.err.println("Teste cancelado: a tabela " + tabelas.getString(1) +
                        " não é InnoDB. Rode o arquivo corrigir_banco.sql no MySQL e execute de novo.");
                return;
            }

            conexao.setAutoCommit(false);

            NutricionistaTest nutricionistaTest = new NutricionistaTest();
            nutricionistaTest.iniciar();

            PacienteTest pacienteTest = new PacienteTest();
            pacienteTest.iniciar();

            RotinaTest rotinaTest = new RotinaTest();
            rotinaTest.iniciar();

            ConsultaTest consultaTest = new ConsultaTest();
            consultaTest.iniciar();
        } catch (Exception e) {
            System.err.println("Não foi possível conectar ao banco: " + e.getMessage());
        } finally {
            try {
                if (conexao != null) {
                    conexao.rollback();
                    conexao.setAutoCommit(true);
                    System.out.println("\nBanco restaurado: nenhum dado de teste foi mantido.");
                }
            } catch (Exception e) {
                System.err.println("\nFalha ao desfazer os dados de teste: " + e.getMessage());
            }
        }
    }
}
