CREATE DATABASE IF NOT EXISTS sistema_nutricional;
USE sistema_nutricional;

CREATE TABLE IF NOT EXISTS nutricionista (
    idNutricionista INT AUTO_INCREMENT PRIMARY KEY,
    nmNome VARCHAR(100) NOT NULL,
    nmEmail VARCHAR(150) NOT NULL UNIQUE,
    nmSenha VARCHAR(100) NOT NULL,
    stAtivo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS paciente (
    idPaciente INT AUTO_INCREMENT PRIMARY KEY,
    idNutricionista INT NOT NULL,
    nmNome VARCHAR(100) NOT NULL,
    nmEmail VARCHAR(150) NOT NULL UNIQUE,
    nmSenha VARCHAR(100) NOT NULL,
    stAtivo BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uq_paciente_id_nutricionista (idPaciente, idNutricionista),
    CONSTRAINT fk_paciente_nutricionista
        FOREIGN KEY (idNutricionista) REFERENCES nutricionista(idNutricionista)
);

CREATE TABLE IF NOT EXISTS receita (
    idReceita INT AUTO_INCREMENT PRIMARY KEY,
    idNutricionista INT NOT NULL,
    idPaciente INT NOT NULL,
    nmTitulo VARCHAR(150) NOT NULL,
    nmDescricao TEXT NOT NULL,
    diaSemana VARCHAR(15) NOT NULL,
    CONSTRAINT fk_receita_nutricionista
        FOREIGN KEY (idNutricionista) REFERENCES nutricionista(idNutricionista),
    CONSTRAINT fk_receita_paciente_nutricionista
        FOREIGN KEY (idPaciente, idNutricionista)
        REFERENCES paciente(idPaciente, idNutricionista)
);

CREATE TABLE IF NOT EXISTS consulta (
    idConsulta INT AUTO_INCREMENT PRIMARY KEY,
    idNutricionista INT NOT NULL,
    idPaciente INT NOT NULL,
    dtConsulta DATE NOT NULL,
    hrConsulta TIME NOT NULL,
    dsConsulta VARCHAR(500),
    stConsulta VARCHAR(20) NOT NULL DEFAULT 'Agendada',
    valorConsulta DECIMAL(10,2) NOT NULL DEFAULT 0,
    stPagamento VARCHAR(20) NOT NULL DEFAULT 'Pendente',
    CONSTRAINT fk_consulta_nutricionista
        FOREIGN KEY (idNutricionista) REFERENCES nutricionista(idNutricionista),
    CONSTRAINT fk_consulta_paciente_nutricionista
        FOREIGN KEY (idPaciente, idNutricionista)
        REFERENCES paciente(idPaciente, idNutricionista)
);

UPDATE receita r
JOIN paciente p ON p.idPaciente = r.idPaciente
SET r.idNutricionista = p.idNutricionista
WHERE r.idNutricionista <> p.idNutricionista;

UPDATE consulta c
JOIN paciente p ON p.idPaciente = c.idPaciente
SET c.idNutricionista = p.idNutricionista
WHERE c.idNutricionista <> p.idNutricionista;

ALTER TABLE paciente
    ADD UNIQUE KEY uq_paciente_id_nutricionista (idPaciente, idNutricionista);

ALTER TABLE receita
    ADD CONSTRAINT fk_receita_paciente_nutricionista
    FOREIGN KEY (idPaciente, idNutricionista)
    REFERENCES paciente(idPaciente, idNutricionista);

ALTER TABLE consulta
    ADD CONSTRAINT fk_consulta_paciente_nutricionista
    FOREIGN KEY (idPaciente, idNutricionista)
    REFERENCES paciente(idPaciente, idNutricionista);
