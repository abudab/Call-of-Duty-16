# Trzeba utworzyć bazę danych i połączenie do niej
# W pliku persistence.xml będzie mój "PersistenceUnit" Call16PU, który
# korzysta z "Data Source" siu, trzeba zrobić żeby to wskazywało na Twoją bazę danych
# poniżej są komendy które należy wykonać na bazie którą utworzysz.
# wszystko tworzone w domyślnym schemie APP
create table wynik(id integer not null,nick varchar(100) not null,punkty integer not null,
		primary key (id));
create table highscores(id integer not null,wynik integer not null,pozycja integer,
		primary key (id), foreign key (wynik) references wynik);

create table teren(id integer not null,nazwa varchar(255) not null unique,
                    xres integer not null,
                    kolorziemi varchar(255) not null,
                    kolornieba varchar(255) not null,
                    primary key (id));
		    
insert into teren values(0,'Wzgórza beznadzieji',66,
        'rgb(150,200,100)','rgb(150,200,250)');
insert into teren values(1,'Wyżyny nikczemności',20,
        'rgb(125,125,125)','rgb(237,164,85)');

create table pozycja(id integer not null,x integer not null,teren integer not null,
	primary key (id), foreign key (teren) references teren);
insert into pozycja values(0,150,0);
insert into pozycja values(1,400,0);
insert into pozycja values(2,900,0);
insert into pozycja values(3,1500,0);
insert into pozycja values(4,1900,0);
insert into pozycja values(5,200,1);
insert into pozycja values(6,600,1);
insert into pozycja values(7,900,1);

create table igrek(id integer not null,y integer not null,teren integer not null,
    primary key (id), foreign key (teren) references teren);

insert into igrek values(0,130,0);
insert into igrek values(1,260,0);
insert into igrek values(2,400,0);
insert into igrek values(3,450,0);
insert into igrek values(4,430,0);
insert into igrek values(5,410,0);
insert into igrek values(6,370,0);
insert into igrek values(7,270,0);
insert into igrek values(8,190,0);
insert into igrek values(9,130,0);
insert into igrek values(10,100,0);
insert into igrek values(11,90,0);
insert into igrek values(12,120,0);
insert into igrek values(13,180,0);
insert into igrek values(14,260,0);
insert into igrek values(15,430,0);
insert into igrek values(16,530,0);
insert into igrek values(17,480,0);
insert into igrek values(18,390,0);
insert into igrek values(19,400,0);
insert into igrek values(20,500,0);


insert into igrek values(21,50,1);
insert into igrek values(22,128,1);
insert into igrek values(23,191,1);
insert into igrek values(24,240,1);
insert into igrek values(25,277,1);
insert into igrek values(26,302,1);
insert into igrek values(27,317,1);
insert into igrek values(28,323,1);
insert into igrek values(29,322,1);
insert into igrek values(30,315,1);
insert into igrek values(31,303,1);
insert into igrek values(32,287,1);
insert into igrek values(33,267,1);
insert into igrek values(34,246,1);
insert into igrek values(35,223,1);
insert into igrek values(36,199,1);
insert into igrek values(37,176,1);
insert into igrek values(38,154,1);
insert into igrek values(39,133,1);
insert into igrek values(40,114,1);
insert into igrek values(41,98,1);
insert into igrek values(42,84,1);
insert into igrek values(43,74,1);
insert into igrek values(44,67,1);
insert into igrek values(45,64,1);
insert into igrek values(46,65,1);
insert into igrek values(47,70,1);
insert into igrek values(48,79,1);
insert into igrek values(49,92,1);
insert into igrek values(50,109,1);
insert into igrek values(51,129,1);
insert into igrek values(52,154,1);
insert into igrek values(53,181,1);
insert into igrek values(54,212,1);
insert into igrek values(55,245,1);
insert into igrek values(56,280,1);
insert into igrek values(57,317,1);
insert into igrek values(58,355,1);
insert into igrek values(59,393,1);
insert into igrek values(60,430,1);
insert into igrek values(61,467,1);
insert into igrek values(62,501,1);
insert into igrek values(63,533,1);
insert into igrek values(64,560,1);
insert into igrek values(65,583,1);
insert into igrek values(66,600,1);
insert into igrek values(67,609,1);
insert into igrek values(68,610,1);
insert into igrek values(69,601,1);
insert into igrek values(70,582,1);
insert into igrek values(71,550,1);

insert into igrek values(72,530,0);
insert into igrek values(73,400,0);
insert into igrek values(74,290,0);
insert into igrek values(75,200,0);
insert into igrek values(76,130,0);
insert into igrek values(77,40,0);
insert into igrek values(78,50,0);
insert into igrek values(79,160,0);
insert into igrek values(80,280,0);
insert into igrek values(81,270,0);



delete from igrek where y>0;
insert into igrek values(0,130,0);
insert into igrek values(1,185,0);
insert into igrek values(2,260,0);
insert into igrek values(3,336,0);
insert into igrek values(4,400,0);
insert into igrek values(5,437,0);
insert into igrek values(6,450,0);
insert into igrek values(7,443,0);
insert into igrek values(8,430,0);
insert into igrek values(9,419,0);
insert into igrek values(10,410,0);
insert into igrek values(11,396,0);
insert into igrek values(12,370,0);
insert into igrek values(13,322,0);
insert into igrek values(14,270,0);
insert into igrek values(15,226,0);
insert into igrek values(16,190,0);
insert into igrek values(17,157,0);
insert into igrek values(18,130,0);
insert into igrek values(19,111,0);
insert into igrek values(20,100,0);
insert into igrek values(21,91,0);
insert into igrek values(22,90,0);
insert into igrek values(23,99,0);
insert into igrek values(24,120,0);
insert into igrek values(25,148,0);
insert into igrek values(26,180,0);
insert into igrek values(27,210,0);
insert into igrek values(28,260,0);
insert into igrek values(29,340,0);
insert into igrek values(30,430,0);
insert into igrek values(31,497,0);
insert into igrek values(32,530,0);
insert into igrek values(33,519,0);
insert into igrek values(34,480,0);
insert into igrek values(35,429,0);
insert into igrek values(36,390,0);
insert into igrek values(37,379,0);
insert into igrek values(38,400,0);
insert into igrek values(39,447,0);
insert into igrek values(40,500,0);
insert into igrek values(41,534,0);
insert into igrek values(42,530,0);
insert into igrek values(43,474,0);
insert into igrek values(44,400,0);
insert into igrek values(45,339,0);
insert into igrek values(46,290,0);
insert into igrek values(47,242,0);
insert into igrek values(48,200,0);
insert into igrek values(49,166,0);
insert into igrek values(50,130,0);
insert into igrek values(51,81,0);
insert into igrek values(52,40,0);
insert into igrek values(53,29,0);
insert into igrek values(54,50,0);
insert into igrek values(55,96,0);
insert into igrek values(56,160,0);
insert into igrek values(57,226,0);
insert into igrek values(58,280,0);
insert into igrek values(59,300,0);
insert into igrek values(60,270,0);


insert into igrek values (72,50,1);
insert into igrek values (73,128,1);
insert into igrek values (74,191,1);
insert into igrek values (75,240,1);
insert into igrek values (76,277,1);
insert into igrek values (77,302,1);
insert into igrek values (78,317,1);
insert into igrek values (79,323,1);
insert into igrek values (80,322,1);
insert into igrek values (81,315,1);
insert into igrek values (82,303,1);
insert into igrek values (83,287,1);
insert into igrek values (84,267,1);
insert into igrek values (85,246,1);
insert into igrek values (86,223,1);
insert into igrek values (87,199,1);
insert into igrek values (88,176,1);
insert into igrek values (89,154,1);
insert into igrek values (90,133,1);
insert into igrek values (91,114,1);
insert into igrek values (92,98,1);
insert into igrek values (93,84,1);
insert into igrek values (94,74,1);
insert into igrek values (95,67,1);
insert into igrek values (96,64,1);
insert into igrek values (97,65,1);
insert into igrek values (98,70,1);
insert into igrek values (99,79,1);
insert into igrek values (100,92,1);
insert into igrek values (101,109,1);
insert into igrek values (102,129,1);
insert into igrek values (103,154,1);
insert into igrek values (104,181,1);
insert into igrek values (105,212,1);
insert into igrek values (106,245,1);
insert into igrek values (107,280,1);
insert into igrek values (108,317,1);
insert into igrek values (109,355,1);
insert into igrek values (110,393,1);
insert into igrek values (111,430,1);
insert into igrek values (112,467,1);
insert into igrek values (113,501,1);
insert into igrek values (114,533,1);
insert into igrek values (115,560,1);
insert into igrek values (116,583,1);
insert into igrek values (117,600,1);
insert into igrek values (118,609,1);
insert into igrek values (119,610,1);
insert into igrek values (120,601,1);
insert into igrek values (121,582,1);
insert into igrek values (122,550,1);

delete from pozycja where teren=0;
insert into pozycja values(0,250,0);
insert into pozycja values(1,500,0);
insert into pozycja values(2,1000,0);
insert into pozycja values(3,1300,0);
insert into pozycja values(4,1700,0);
update teren set xres=33 where id=0;