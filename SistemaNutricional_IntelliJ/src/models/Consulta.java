package models;

import java.sql.Date;
import java.sql.Time;

import java.util.Objects;

public class Consulta {
    private Integer id;
    private Nutricionista nutricionista;
    private Paciente paciente;
    private Date data;
    private Time hora;
    private String descricao;
    private String status;
    private Double valor;
    private String statusPagamento;

    public Consulta() {
    }

    public Consulta(Nutricionista nutricionista, Paciente paciente, Date data,
                    Time hora, String descricao, Double valor) {
        this.nutricionista = nutricionista;
        this.paciente = paciente;
        this.data = data;
        this.hora = hora;
        this.descricao = descricao;
        this.valor = valor;
        this.status = "Agendada";
        this.statusPagamento = "Pendente";
    }

    public Consulta(Integer id, Nutricionista nutricionista, Paciente paciente, Date data,
                    Time hora, String descricao, String status,
                    Double valor, String statusPagamento) {
        this.id = id;
        this.nutricionista = nutricionista;
        this.paciente = paciente;
        this.data = data;
        this.hora = hora;
        this.descricao = descricao;
        this.status = status;
        this.valor = valor;
        this.statusPagamento = statusPagamento;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    @Override
    public String toString() {
        String nomePaciente = paciente == null ? "não informado" : paciente.getNome();
        String idPaciente = paciente == null ? "?" : String.valueOf(paciente.getId());
        return "Id: " + id + " | Paciente: " + nomePaciente + " (ID " + idPaciente + ") | "
                + data + " " + hora + " | " + status + " | R$ " + valor
                + " | Pagamento: " + statusPagamento + " | " + descricao;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Consulta outro = (Consulta) objeto;
        return id != null && Objects.equals(id, outro.id);
    }

    @Override
    public int hashCode() {
        // Hash estável enquanto o ID é atribuído após a inserção no banco.
        return getClass().hashCode();
    }
}
