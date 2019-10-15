create database mycash_db;
use mycash_db;

create table users(
id int(10) not null auto_increment,
login varchar(15) not null unique,
pass varchar(15) not null,
mail varchar(20) not null unique,
is_active bool not null default false,
primary key (id)
);

create table counts(
id int(10) not null auto_increment,
count_name varchar(20) not null,
balance decimal(10, 2) unsigned not null,
currency varchar(3) not null,
is_active bool not null default false,
user_id int(10) not null,
foreign key (user_id) references users (id),
primary key (id)
);

create table income_categories(
id int(10) not null auto_increment,
category_name varchar(15) not null,
is_active bool not null default false,
user_id int(10) not null,
foreign key (user_id) references users (id),
primary key (id)
);

create table incomes(
id int(10) not null auto_increment,
amount decimal(10,2) not null,
annotation varchar(50),
inc_date date,
is_active bool not null default false,
income_category_id int(10) not null,
count_id int(10) not null,
user_id int(10) not null,
foreign key (income_category_id) references income_categories (id) ,
foreign key (count_id) references counts(id),
foreign key (user_id) references users(id),
primary key (id)
);

create table expense_categories(
id int(10) not null auto_increment,
category_name varchar(15) not null,
is_active bool not null default false,
user_id int(10) not null,
foreign key (user_id) references users (id),
primary key (id)
);

create table expenses(
id int(10) not null auto_increment,
amount decimal(10,2) not null,
annotation varchar(50),
exp_date date,
is_active bool not null default false,
expense_category_id int(10) not null,
count_id int(10) not null,
user_id int(10) not null,
foreign key (expense_category_id) references expense_categories (id),
foreign key (count_id) references counts(id),
foreign key (user_id) references users(id),
primary key (id)
);

create table budget(
id int(10) not null auto_increment,
amount int(10) not null,
start_date date not null,
end_date date not null,
expense_category_id int(10) not null,
user_id int(10) not null,
foreign key (user_id) references users (id),
foreign key (expense_category_id) references expense_categories (id),
primary key (id)
);