-- Execute uma vez se você JÁ criou as tabelas com a versão anterior do banco.sql.
-- Faça backup antes. Este script alinha receitas/consultas ao nutricionista responsável pelo paciente.
USE sistema_nutricional;

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
