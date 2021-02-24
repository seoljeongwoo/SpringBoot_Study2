insert into person(`id`,`name`,`age`,`blood_type`,`year_of_birthday`,`month_of_birthday`,`day_of_birthday`) values (1,'martin',10,'A',2020,2,10);
insert into person(`id`,`name`,`age`,`blood_type`,`year_of_birthday`,`month_of_birthday`,`day_of_birthday`) values (2,'david',9,'B',2019,7,10);
insert into person(`id`,`name`,`age`,`blood_type`,`year_of_birthday`,`month_of_birthday`,`day_of_birthday`) values (3,'dennis',8,'O',1995,7,11);
insert into person(`id`,`name`,`age`,`blood_type`,`year_of_birthday`,`month_of_birthday`,`day_of_birthday`) values (4,'sophia',9,'AB',2021,2,14);
insert into person(`id`,`name`,`age`,`blood_type`,`year_of_birthday`,`month_of_birthday`,`day_of_birthday`) values (5,'benny',7,'A',1998,2,11);

insert into block(`id` , `name`) values(1,'dennis');
insert into block(`id` , `name`) values(2,'sophia');

update person set block_id = 1 where id = 3;
update person set block_id = 2 where id = 4;
