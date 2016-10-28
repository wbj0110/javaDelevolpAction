CREATE TABLE address (
  addressID varchar(50) NOT NULL default '0',
  street varchar(50) default '',
  city varchar(50) default '',
  zip varchar(50) default '',
  state varchar(50) default '',
  customerID varchar(50) default '',
  PRIMARY KEY  (addressID),
  KEY customerID (customerID)
) TYPE=InnoDB;
# --------------------------------------------------------

#
# 表的结构 `customer`
#

CREATE TABLE customer (
  id varchar(50) NOT NULL default '',
  firstName varchar(50) default '',
  lastName varchar(50) default '',
  UNIQUE KEY customerID (id)
) TYPE=InnoDB;
# --------------------------------------------------------

#
# 表的结构 `customer_subscriptions_subscription_customers`
#
# 创建时间: 2003 年 08 月 29 日 10:33
# 最后更新时间: 2003 年 09 月 02 日 17:10
#

CREATE TABLE customer_subscriptions_subscription_customers (
  Customer varchar(250) binary NOT NULL default '',
  Subscription varchar(250) binary NOT NULL default '',
  PRIMARY KEY  (Customer,Subscription)
) TYPE=MyISAM;
# --------------------------------------------------------

#
# 表的结构 `subscription`
#

CREATE TABLE subscription (
  title varchar(50) NOT NULL default '',
  type varchar(50) default '',
  PRIMARY KEY  (title)
) TYPE=InnoDB;

