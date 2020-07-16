select avg(salario) from empregado;
--35125.000000000000

select count(distinct e.pnome) from trabalha_em tr
inner join empregado e on tr.essn = e.ssn
inner join projeto pr on pr.pnumero = tr.pno
where e.dno = 5 and pr.pjnome like 'ProductX' and tr.horas > 10
--2

select e.pnome, de.nome_dependente from empregado e
INNER join dependente de on e.ssn = de.essn
where e.pnome = de.nome_dependente
--0

select e.pnome from empregado e
where e.superssn in (
	select de1.gerssn from departamento de1
	INNER join empregado e1 on e1.ssn = de1.gerssn 
	where e1.pnome = 'Franklin' and e1.unome = 'Wong'
)
--Joyce, Ramesh

select e.pnome, sum(tr.horas) from empregado e 
INNER join trabalha_em tr on e.ssn = tr.essn
where tr.pno in (
  SELECT pnumero FROM projeto 
	where pjnome = 'Newbenefits'
)
group by e.pnome
--Ahmad 5, Alicia 30, Jennifer 20

select sum(e.salario) from empregado e
where e.dno in(
  SELECT dnumero FROM departamento where dnome='Research'
) 
--133000.00

select sum(e.salario)+sum(e.salario)*10/100 from trabalha_em tr
inner join empregado e on tr.essn = e.ssn
inner join projeto pr on pr.pnumero = tr.pno
where pr.pjnome like 'ProductX'
--60500

select da.dnome, avg(e.salario) from  empregado e
inner join departamento da on e.dno = da.dnumero
group by 1
--Headquarters 55000, Research 33250, Administration 31000