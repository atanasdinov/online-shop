
create table if not exists sales (
	id BIGSERIAL PRIMARY KEY,
	username VARCHAR(50) not null,
	product_name VARCHAR(250) not null,
	quantity INTEGER not null,
	price DOUBLE PRECISION not null,
	total_price DOUBLE PRECISION not null
);

create table if not exists roles (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(30) not null
);

create table if not exists carts (
	id BIGSERIAL PRIMARY KEY,
	total_price DOUBLE PRECISION not null
);

create table if not exists users (
	id BIGSERIAL PRIMARY KEY,
	username VARCHAR(30) not null,
	password VARCHAR(255) not null,
	email VARCHAR(50) not null,
	first_name VARCHAR(30) not null,
	last_name VARCHAR(30) not null,
	address VARCHAR(30),
	token VARCHAR(255),
	enabled BOOLEAN ,
	cart_id BIGINT not null,
	role_id BIGINT not null,
	constraint fk_user_cart_id foreign key(cart_id) references carts(id),
	constraint fk_user_role_id foreign key(role_id) references roles(id)
);

create table if not exists comments (
	id BIGSERIAL PRIMARY KEY,
	message VARCHAR(250) not null,
	user_id BIGINT not null,
	constraint fk_comment_user_id foreign key(user_id) references users(id)
);

create table if not exists categories (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(50) unique not null
);

create table if not exists products (
	id BIGSERIAL,
	name VARCHAR(250) not null,
	price DOUBLE PRECISION not null,
	available_quantity INTEGER,
	requested_quantity INTEGER,
	picture_name VARCHAR(128),
	category_id BIGINT not null
) PARTITION BY RANGE (price);

create table if not exists prod_part_1 partition of products (constraint prod_part_1_pk PRIMARY KEY (id)) FOR VALUES FROM (0) to (10);
create table if not exists prod_part_2 partition of products (constraint prod_part_2_pk PRIMARY KEY (id)) FOR VALUES FROM (10) to (20);
create table if not exists prod_part_3 partition of products (constraint prod_part_3_pk PRIMARY KEY (id)) FOR VALUES FROM (20) to (30);
create table if not exists prod_part_4 partition of products (constraint prod_part_4_pk PRIMARY KEY (id)) FOR VALUES FROM (30) to (40);
create table if not exists prod_part_5 partition of products (constraint prod_part_5_pk PRIMARY KEY (id)) FOR VALUES FROM (40) to (50);
create table if not exists prod_part_6 partition of products (constraint prod_part_6_pk PRIMARY KEY (id)) FOR VALUES FROM (50) to (60);
create table if not exists prod_part_7 partition of products (constraint prod_part_7_pk PRIMARY KEY (id)) FOR VALUES FROM (60) to (70);
create table if not exists prod_part_8 partition of products (constraint prod_part_8_pk PRIMARY KEY (id)) FOR VALUES FROM (70) to (80);
create table if not exists prod_part_9 partition of products (constraint prod_part_9_pk PRIMARY KEY (id)) FOR VALUES FROM (80) to (90);
create table if not exists prod_part_10 partition of products (constraint prod_part_10_pk PRIMARY KEY (id)) FOR VALUES FROM (90) to (100);
create table if not exists prod_part_11 partition of products (constraint prod_part_11_pk PRIMARY KEY (id)) FOR VALUES FROM (100) to (MAXVALUE);

create table if not exists verification_tokens (
	id BIGSERIAL PRIMARY KEY,
	token VARCHAR(255) not null,
	expiry_date TIMESTAMP,
	user_id BIGINT not null,
	constraint fk_verification_token_user_id foreign key(user_id) references users(id)
);

create table if not exists product_comments (
	product_id BIGINT not null,
	comment_id BIGINT not null,
	constraint fk_product_comments_comment_id foreign key(comment_id) references comments(id)
);

create table if not exists carts_products (
	cart_id BIGINT not null,
	product_id BIGINT not null,
	constraint fk_carts_products_cart_id foreign key(cart_id) references carts(id)
);