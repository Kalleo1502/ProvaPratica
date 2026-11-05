package dao;

import connections.ConexaoMySQL;
import models.Nutricionista;

import java.sql.*;

public class NutricionistaDAO {
    private Nutricionista mapear(ResultSet r) throws SQLException {
        return new Nutricionista(r.getInt("idNutricionista"), r.getString("nmNome"),
                r.getString("nmEmail"), r.getString("nmSenha"), r.getBoolean("stAtivo"));
    }

    public Nutricionista selecionarPorEmail(String email) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "SELECT * FROM nutricionista WHERE nmEmail=? AND stAtivo=1");
            p.setString(1, email);
            ResultSet r = p.executeQuery();
            return r.next() ? mapear(r) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Nutricionista selecionarPorId(Integer id) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "SELECT * FROM nutricionista WHERE idNutricionista=?");
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            return r.next() ? mapear(r) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // O código do nutricionista é sequencial (1, 2, 3...): o próximo é o maior código + 1.
    private Integer proximoId() throws SQLException {
        ResultSet r = ConexaoMySQL.get().prepareStatement(
                "SELECT MAX(idNutricionista) FROM nutricionista").executeQuery();
        r.next();
        return r.getInt(1) + 1; // tabela vazia: MAX é NULL, getInt devolve 0 e o primeiro código é 1
    }

    public Boolean inserir(Nutricionista n) {
        try {
            Integer id = proximoId();
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO nutricionista(idNutricionista,nmNome,nmEmail,nmSenha,stAtivo) VALUES(?,?,?,?,1)");
            p.setInt(1, id);
            p.setString(2, n.getNome());
            p.setString(3, n.getEmail());
            p.setString(4, n.getSenha());
            boolean inseriu = p.executeUpdate() > 0;
            if (inseriu) n.setId(id);
            return inseriu;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Nutricionista n) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE nutricionista SET nmNome=?,nmEmail=?,nmSenha=? WHERE idNutricionista=?");
            p.setString(1, n.getNome());
            p.setString(2, n.getEmail());
            p.setString(3, n.getSenha());
            p.setInt(4, n.getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletar(Integer id) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE nutricionista SET stAtivo=0 WHERE idNutricionista=?");
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
