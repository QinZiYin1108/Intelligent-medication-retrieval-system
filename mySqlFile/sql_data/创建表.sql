/*==============================================================*/
/* DBMS name:      Sybase SQL Anywhere 12                       */
/* Created on:     2024/1/7 ������ 17:09:48                        */
/*==============================================================*/
SET FOREIGN_KEY_CHECKS = 0;

DROP table if exists docuter_waiting;

drop table if exists keeper_waiting;

drop table if exists request_employment;

drop table if exists docuter;

drop table if exists drug;

drop table if exists hospital;

drop table if exists keeper;

drop table if exists machine;

drop table if exists machine_drug;

drop table if exists manage;

drop table if exists record;

drop table if exists record_drug;

drop table if exists users;

SET FOREIGN_KEY_CHECKS = 1;
/*==============================================================*/
/* Table: docuter                                               */
/*==============================================================*/
create table docuter (
   dc_code varchar(10) not null,
   hp_code              varchar(10)                    not null,
   dc_emial             varchar(30)                    not null,
   dc_password          varchar(16)                    not null,
   dc_name              varchar(10)                    not null,
   dc_ID                varchar(27)                    not null,
   dc_IDcard            varchar(18)                    not null,
   dc_age               integer                        null,
   dc_sex               char(5)                        null,
   dc_phone             varchar(11)                    null,
   primary key (dc_code)
);

/*==============================================================*/
/* Table: drug                                                  */
/*==============================================================*/
create table drug 
(
   dg_code              varchar(10)                    not null,
   dg_name              varchar(20)                    not null,
   dg_prive             DOUBLE                        not null default 0.0,
   primary key (dg_code)
);

/*==============================================================*/
/* Table: hospital                                              */
/*==============================================================*/
create table hospital 
(
   hp_code              varchar(10)                    not null,
	 hp_emial             varchar(30)                    not null,
   hp_password          varchar(16)                    not null,
   hp_name              varchar(20)                    not null,
   hp_license           varchar(15)                    not null,
   hp_busLi             varchar(18)                    not null,
   hp_legal_person      varchar(10)                    not null,
   hp_ID_legal          varchar(18)                    not null,
   primary key (hp_code)
);

/*==============================================================*/
/* Table: keeper                                                */
/*==============================================================*/
create table keeper 
(
   kp_code              varchar(10)                    not null,
   hp_code              varchar(10)                    not null,
   kp_password          varchar(16)                    not null,
   kp_emial             varchar(30)                    not null,
   kp_name              varchar(10)                    not null,
   kp_IDcard            varchar(18)                    not null,
   kp_age               integer                        null,
   kp_sex               char(5)                        null,
   primary key (kp_code)
);

/*==============================================================*/
/* Table: machine                                               */
/*==============================================================*/
create table machine 
(
   mh_code              varchar(10)                    not null,
   kp_code              varchar(10)                    not null,
   hp_code              varchar(10)                    not null,
   primary key (mh_code)
);

/*==============================================================*/
/* Table: machine_drug                                          */
/*==============================================================*/
create table machine_drug 
(
   md_code              varchar(10)                    not null,
   dg_code              varchar(10)                    not null,
	 mh_code              varchar(10)                    not null,
   md_number            integer                        not null default 0,
   primary key (md_code)
);

/*==============================================================*/
/* Table: manage                                                */
/*==============================================================*/
create table manage 
(
   mg_code              varchar(10)                    not null,
	 kp_code              varchar(10)                    not null,
	 md_code              varchar(10)                    not null,
   mg_time              timestamp                      not null,
   mg_number            integer                        not null default 0,
   primary key (mg_code)
);

/*==============================================================*/
/* Table: record                                                */
/*==============================================================*/
create table record 
(
   re_code              varchar(10)                    not null,
   dc_code              varchar(10)                    not null,
   user_code            varchar(10)                    not null,
   re_time              timestamp                      not null,
   primary key (re_code)
);

/*==============================================================*/
/* Table: record_drug                                           */
/*==============================================================*/
create table record_drug 
(
   rd_code              varchar(10)                    not null,
   re_code              varchar(10)                    not null,
	 dg_code              varchar(10)                    not null,
   rd_number            integer                        not null default 0,
   rd_time              timestamp                      null,
	 rd_type              varchar(5)                     not null default "未付款",
   primary key (rd_code)
);
/*==============================================================*/
/* Table: "user"                                                */
/*==============================================================*/
create table users
(
   user_code            varchar(10)                    not null,
   user_emial           varchar(30)                    not null,
   user_password        varchar(16)                    not null,
   user_name            varchar(16)                    not null,
   user_age             integer                        null,
   user_sex             char(5)                        null,
   user_phone           varchar(11)                    null,
   primary key (user_code)
);

create table request_employment
(
		re_code            varchar(10)                     not null,
		em_code            varchar(10)                     not null,
		hp_code            varchar(10)                     not null,
		re_timeOn          timestamp                       not null,
		re_timeOK          timestamp                       null,
		re_state           varchar(5)                      not null default "未通过",
		primary key(re_code)
);

create table docuter_waiting
(
	 dw_code varchar(10) not null,
   hp_code              varchar(10)                    not null,
   dw_emial             varchar(30)                    not null,
   dw_password          varchar(16)                    not null,
   dw_name              varchar(10)                    not null,
   dw_ID                varchar(27)                    not null,
   dw_IDcard            varchar(18)                    not null,
   dw_age               integer                        null,
   dw_sex               char(5)                        null,
   dw_phone             varchar(11)                    null,
   primary key (dw_code)
);

create table keeper_waiting
(
	 kw_code              varchar(10)                    not null,
   hp_code              varchar(10)                    not null,
   kw_password          varchar(16)                    not null,
   kw_emial             varchar(30)                    not null,
   kw_name              varchar(10)                    not null,
   kw_IDcard            varchar(18)                    not null,
   kw_age               integer                        null,
   kw_sex               char(5)                        null,
   primary key (kw_code)
);

ALTER table docuter_waiting
ADD CONSTRAINT FK_hp_dw
FOREIGN KEY (hp_code) REFERENCES hospital(hp_code);

ALTER table keeper_waiting
ADD CONSTRAINT FK_hp_kw
FOREIGN KEY (hp_code) REFERENCES hospital(hp_code);

ALTER TABLE request_employment
ADD CONSTRAINT FK_re_docuter
FOREIGN KEY (em_code) REFERENCES docuter_waiting(dw_code);

ALTER TABLE request_employment
ADD CONSTRAINT FK_re_keeper
FOREIGN KEY (em_code) REFERENCES keeper_waiting(kw_code);

ALTER TABLE request_employment
ADD CONSTRAINT FK_re_hospital
FOREIGN KEY (hp_code) REFERENCES hospital(hp_code);

ALTER TABLE docuter
ADD CONSTRAINT FK_docuter_hospital_id
FOREIGN KEY (hp_code) REFERENCES hospital(hp_code);

ALTER TABLE keeper
ADD CONSTRAINT FK_keeper_hospital_id
FOREIGN KEY (hp_code) REFERENCES hospital(hp_code);

ALTER TABLE machine
ADD CONSTRAINT FK_machine_hospital_id
FOREIGN KEY (hp_code) REFERENCES hospital(hp_code);

ALTER TABLE machine_drug
ADD CONSTRAINT FK_machine_drug_drug_id
FOREIGN KEY (dg_code) REFERENCES drug(dg_code);

ALTER TABLE machine_drug
ADD CONSTRAINT FK_machine_drug_machine_id
FOREIGN KEY (mh_code) REFERENCES machine(mh_code);

ALTER TABLE manage
ADD CONSTRAINT FK_manage_machine_drug_id
FOREIGN KEY (md_code) REFERENCES machine_drug(md_code);

ALTER TABLE manage
ADD CONSTRAINT FK_manage_keeper_id
FOREIGN KEY (kp_code) REFERENCES keeper(kp_code);

ALTER TABLE record
ADD CONSTRAINT FK_record_docuter_id
FOREIGN KEY (dc_code) REFERENCES docuter(dc_code);

ALTER TABLE record
ADD CONSTRAINT FK_record_users_id
FOREIGN KEY (user_code) REFERENCES users(user_code);

ALTER TABLE record_drug
ADD CONSTRAINT FK_record_drug_record_id
FOREIGN KEY (re_code) REFERENCES record(re_code);

ALTER TABLE record_drug
ADD CONSTRAINT FK_record_drug_drug_id
FOREIGN KEY (dg_code) REFERENCES drug(dg_code);


