create database sistema_nutricionista;

use sistema_nutricionista;

-- ----------------------------------------------------------------------------------------------------
create table nutricionista(
	idNutricionista int primary key,
	nmNome varchar(100) not null,
	nmEmail varchar(150) not null unique,
	nmSenha varchar(100) not null,
	stAtivo boolean not null
);

create table paciente(
	idNutricionista int,
	idPaciente int,
	nmNome varchar(100) not null,
	nmEmail varchar(150) not null unique,
	nmSenha varchar(100) not null,
	stAtivo boolean not null,
	primary key (idNutricionista, idPaciente),
	foreign key (idNutricionista) references nutricionista (idNutricionista)
);

create table rotina(
	idNutricionista int,
	idReceita int,
	idPaciente int,
	nmTitulo varchar(150) not null,
	nmDescricao varchar(500) not null,
	diaSemana varchar(15) not null,
	primary key (idNutricionista, idReceita, idPaciente),
	foreign key (idNutricionista) references nutricionista (idNutricionista),
	foreign key (idNutricionista, idPaciente) references paciente (idNutricionista, idPaciente)
);

create table consulta(
	idNutricionista int,
	idConsulta int,
	idPaciente int,
	dtConsulta date not null,
	hrConsulta time not null,
	dsConsulta varchar(500),
	stConsulta varchar(20) not null,
	valorConsulta decimal(10,2) not null,
	stPagamento varchar(20) not null,
	primary key (idNutricionista, idConsulta, idPaciente),
	foreign key (idNutricionista) references nutricionista (idNutricionista),
	foreign key (idNutricionista, idPaciente) references paciente (idNutricionista, idPaciente)
);

-- ----------------------------------------------------------------------------------------------------
-- Dados iniciais (os testes do Teste.java usam o nutricionista 1 e o paciente 1 dele).
-- Repare que cada nutricionista começa os seus pacientes no código 1.
insert into nutricionista (idNutricionista, nmNome, nmEmail, nmSenha, stAtivo)
values (1, 'Ana', 'ana@gmail.com', '1234', 1);
insert into nutricionista (idNutricionista, nmNome, nmEmail, nmSenha, stAtivo)
values (2, 'Bruno', 'bruno@gmail.com', '1234', 1);

insert into paciente (idNutricionista, idPaciente, nmNome, nmEmail, nmSenha, stAtivo)
values (1, 1, 'juca', 'juca@gmail', '1234', 1);
insert into paciente (idNutricionista, idPaciente, nmNome, nmEmail, nmSenha, stAtivo)
values (2, 1, 'lucas', 'lucas@gmail', '1234', 1);

select * from nutricionista;
select * from paciente;
