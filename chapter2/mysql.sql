CREATE DATABASE signup;
use signup;
###本数据库是用户详细资料
CREATE TABLE profile (
   userid varchar(20) NOT NULL,            #用户id
   username varchar(50) NOT NULL,          #用户姓名
   email varchar(50),                      #Email地址

   gender tinyint(4) NOT NULL,             #性别
   occupation tinyint(4),                  #职业

   location varchar(200),                  #住址
   city varchar(20),                       #城市
   country tinyint(4),                     #国家

   zipcode varchar(50),                    #邮政编码
   homephone varchar(50),                  #家庭电话或联系电话

   cardnumber varchar(20),                 #身份证号码
   birthday date DEFAULT '0000-00-00',     #出生日期

   regip varchar(20),                       #注册时的IP  公安局追查用
   regdate datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,    #注册日期
   PRIMARY KEY (userid),
   UNIQUE email (email),
   UNIQUE userid (userid)
);
###本数据库是密码验证用
CREATE TABLE password (
  userid varchar(20) DEFAULT '' NOT NULL,       #用户id
  password varchar(16) DEFAULT '' NOT NULL,     #密码 使用PASSWORD()加密后的
  PRIMARY KEY (userid),
  KEY password (password)
);
###本数据库是用来提供用户查询密码用的
CREATE TABLE passwordassit (
  userid varchar(20) DEFAULT '' NOT NULL,       #用户id
  oldpassword varchar(16) DEFAULT '' NOT NULL,  #密码  未加密的
  passwdtype tinyint(4),                        #密码提示问题
  passwdanswer varchar(100),                    #密码回答
  PRIMARY KEY (userid),
  KEY passwdtype (passwdtype)
);

