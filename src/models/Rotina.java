package models;

import java.util.Objects;

public class Rotina {
    // Id de rotina unico para cada nutricionista e paciente
    private Integer id;
    private Nutricionista nutricionista;
    private Paciente paciente;
    private String titulo;
    private String descricao;
    private String diaSemana;

    public Rotina() {
    }

    public Rotina(Nutricionista nutricionista, Paciente paciente, String titulo,
                  String descricao, String diaSemana) {
        this.nutricionista = nutricionista;
        this.paciente = paciente;
        this.titulo = titulo;
        this.descricao = descricao;
        this.diaSemana = diaSemana;
    }

    public Rotina(Integer id, Nutricionista nutricionista, Paciente paciente, String titulo,
                  String descricao, String diaSemana) {
        this.id = id;
        this.nutricionista = nutricionista;
        this.paciente = paciente;
        this.titulo = titulo;
        this.descricao = descricao;
        this.diaSemana = diaSemana;
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    @Override
    public String toString() {
        String nomePaciente = paciente == null ? "não informado" : paciente.getNome();
        String idPaciente = paciente == null ? "?" : String.valueOf(paciente.getId());
        return "Id: " + id + " | Paciente: " + nomePaciente + " (ID " + idPaciente + ") | "
                + diaSemana + " | " + titulo + " | " + descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rotina rotina = (Rotina) o;
        return Objects.equals(id, rotina.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
