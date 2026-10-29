package models;

import java.util.Objects;

public class Paciente {
    private Integer id;
    private Nutricionista nutricionista;
    private String nome;
    private String email;
    private String senha;
    private Boolean ativo;

    public Paciente() {
    }

    public Paciente(Nutricionista nutricionista, String nome, String email, String senha) {
        this.nutricionista = nutricionista;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }

    public Paciente(Integer id, Nutricionista nutricionista, String nome, String email,
                    String senha, Boolean ativo) {
        this.id = id;
        this.nutricionista = nutricionista;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Nutricionista getNutricionista() {
        return nutricionista;
    }

    public void setNutricionista(Nutricionista nutricionista) {
        this.nutricionista = nutricionista;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Id: " + id + " | Nome: " + nome + " | E-mail: " + email + " | Ativo: " + ativo;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Paciente outro = (Paciente) objeto;
        return id != null && Objects.equals(id, outro.id);
    }

    @Override
    public int hashCode() {
        // Hash estável enquanto o ID é atribuído após a inserção no banco.
        return getClass().hashCode();
    }
}
