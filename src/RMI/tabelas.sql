


--selects
SELECT * FROM mensagem;
SELECT * FROM utilizadores;
SELECT * FROM projects;
SELECT * FROM votos;
SELECT * FROM recompensas;
SELECT * FROM depositos;
SELECT * FROM mensagem;


--drops

drop table utilizadores;
drop table projects;
drop table votos;
drop table recompensas;
drop table mensagem;
drop table depositos;

--tabelas ja com primary key
Create table Utilizadores
(
 nomeU varchar2(200),
pass varchar(200),
saldoU float,
constraint PK_Utilizadores primary key (nomeU)
);


Create table Projects
(
idP int,
nomeP varchar2(200),
dono  varchar2(200),
descricao varchar2(200),
saldoP Float,
pretendido Float,
inicio Date,
finall Date,
validade int,
hoje Date,
constraint PK_Projects primary key (idP)
);


Create table Recompensas
(
idProj int,
idR int,
nomeR varchar2(200),
valor float,
ativa int,
constraint PK_Recompensas primary key (idR)
);


Create table votos
(
idV int,
n int,
nomeV varchar(255),
idProje int,
ativo int,
constraint PK_votos primary key (idV)
);


Create table mensagem
(
idProjet int,
nomU varchar2(200),
idM int,
mensagem varchar(255),
resposta varchar (255),
constraint PK_mensagem primary key (idM)

);


Create table depositos
(
idD int,
nomeUser varchar(255),
idPro int,
nomeVo varchar2(200),
idRe int,
recebido float,
EntregueR int,
constraint PK_depositos primary key (idD)

);


--procedures que sao criadas na oracle--> nao no mesmo script das tabelas--> da merda
CREATE OR REPLACE PROCEDURE REPOR_SALDO 
(
  NOMEUSER IN VARCHAR2  
, IDPROJ IN NUMBER  
) AS 
BEGIN
 UPDATE Utilizadores set saldoU = (SELECT recebido FROM depositos WHERE idPro=idProj AND nomeUser LIKE nomeU) + (SELECT saldoU FROM Utilizadores WHERE nomeU=nomeUser) WHERE nomeU=nomeUser;
  NULL;
END REPOR_SALDO;


CREATE OR REPLACE PROCEDURE REPOR_N 
(
  IDVO IN NUMBER  
, IDPP IN NUMBER  
, NOME IN VARCHAR2  
) AS 
BEGIN

UPDATE votos SET n =(SELECT n FROM votos WHERE idV=idVo)-(SELECT recebido FROM depositos WHERE idPro=" +idPp + " AND nomeUser LIKE nome)  WHERE idV=idVo;	
  NULL;
END REPOR_N;