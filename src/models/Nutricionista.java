package models;

import java.util.Objects;

public class Nutricionista {
    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private Boolean ativo;

    public Nutricionista() {
    }

    public Nutricionista(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }

    public Nutricionista(Integer id, String nome, String email, String senha, Boolean ativo) {
        this.id = id;
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
        Nutricionista outro = (Nutricionista) objeto;
        return id != null && Objects.equals(id, outro.id);
    }

    @Override
    public int hashCode() {
        // Hash estável enquanto o ID é atribuído após a inserção no banco.
        return getClass().hashCode();
    }
}
