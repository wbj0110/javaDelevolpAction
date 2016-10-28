CREATE DATABASE signup;
use signup;
###�����ݿ����û���ϸ����
CREATE TABLE profile (
   userid varchar(20) NOT NULL,            #�û�id
   username varchar(50) NOT NULL,          #�û�����
   email varchar(50),                      #Email��ַ

   gender tinyint(4) NOT NULL,             #�Ա�
   occupation tinyint(4),                  #ְҵ

   location varchar(200),                  #סַ
   city varchar(20),                       #����
   country tinyint(4),                     #����

   zipcode varchar(50),                    #��������
   homephone varchar(50),                  #��ͥ�绰����ϵ�绰

   cardnumber varchar(20),                 #���֤����
   birthday date DEFAULT '0000-00-00',     #��������

   regip varchar(20),                       #ע��ʱ��IP  ������׷����
   regdate datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,    #ע������
   PRIMARY KEY (userid),
   UNIQUE email (email),
   UNIQUE userid (userid)
);
###�����ݿ���������֤��
CREATE TABLE password (
  userid varchar(20) DEFAULT '' NOT NULL,       #�û�id
  password varchar(16) DEFAULT '' NOT NULL,     #���� ʹ��PASSWORD()���ܺ��
  PRIMARY KEY (userid),
  KEY password (password)
);
###�����ݿ��������ṩ�û���ѯ�����õ�
CREATE TABLE passwordassit (
  userid varchar(20) DEFAULT '' NOT NULL,       #�û�id
  oldpassword varchar(16) DEFAULT '' NOT NULL,  #����  δ���ܵ�
  passwdtype tinyint(4),                        #������ʾ����
  passwdanswer varchar(100),                    #����ش�
  PRIMARY KEY (userid),
  KEY passwdtype (passwdtype)
);

