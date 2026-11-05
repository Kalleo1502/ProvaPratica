import connections.ConexaoMySQL;
import models.Rotina;
import tests.ConsultaTest;
import tests.NutricionistaTest;
import tests.PacienteTest;
import tests.RotinaTest;

import java.sql.Connection;

public class Teste {
    public static void main(String[] args) {
        Connection conexao = null;
        try {
            // Os testes rodam dentro de uma transação que no final é desfeita (rollback),
            // então nada do que eles cadastram fica gravado no banco.
            conexao = ConexaoMySQL.get();
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
