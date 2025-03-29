CREATE TABLE public.categories (
                                   category_id integer NOT NULL,
                                   name character varying(255)
);

CREATE SEQUENCE public.categories_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.clients (
                                client_id integer NOT NULL,
                                email character varying(255),
                                first_name character varying(255),
                                last_name character varying(255),
                                password character varying(255),
                                username character varying(255),
                                role character varying(255)
);

CREATE SEQUENCE public.clients_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.manufacturers (
                                      manufacturer_id integer NOT NULL,
                                      country character varying(255),
                                      name character varying(255)
);

CREATE SEQUENCE public.manufacturers_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public."order_product" (
                                        order_product_id integer NOT NULL,
                                        count integer NOT NULL,
                                        order_id integer NOT NULL,
                                        product_id integer NOT NULL
);

CREATE SEQUENCE public."order_product_seq"
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.orders (
                               order_id integer NOT NULL,
                               client_id integer NOT NULL,
                               shipping_id integer NOT NULL,
                               total_cost integer NOT NULL,
                               track_number character varying(255),
                               date timestamp(6) without time zone
);

CREATE SEQUENCE public.orders_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.products (
                                 product_id integer NOT NULL,
                                 category_id integer NOT NULL,
                                 description character varying(255),
                                 manufacturer_id integer NOT NULL,
                                 name character varying(255),
                                 price integer NOT NULL
);

CREATE SEQUENCE public.products_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.shipping (
                                 shipping_id integer NOT NULL,
                                 address character varying(255),
                                 city character varying(255),
                                 client_id integer NOT NULL,
                                 post_code character varying(255)
);

CREATE SEQUENCE public.shipping_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (category_id);

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (client_id);

ALTER TABLE ONLY public.manufacturers
    ADD CONSTRAINT manufacturers_pkey PRIMARY KEY (manufacturer_id);

ALTER TABLE ONLY public."order_product"
    ADD CONSTRAINT "order_product_pkey" PRIMARY KEY (order_product_id);

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (product_id);

ALTER TABLE ONLY public.shipping
    ADD CONSTRAINT shipping_pkey PRIMARY KEY (shipping_id);

ALTER TABLE public.order_product
    ADD CONSTRAINT order_product_orders_fk FOREIGN KEY (order_id) REFERENCES orders(order_id);

ALTER TABLE public.order_product
    ADD CONSTRAINT order_product_products_fk FOREIGN KEY (product_id) REFERENCES products(product_id);

ALTER TABLE public.orders
    ADD CONSTRAINT orders_clients_fk FOREIGN KEY (client_id) REFERENCES clients(client_id);

ALTER TABLE public.orders
    ADD CONSTRAINT orders_shipping_fk FOREIGN KEY (shipping_id) REFERENCES shipping(shipping_id);

ALTER TABLE public.products
    ADD CONSTRAINT products_categories_fk FOREIGN KEY (category_id) REFERENCES categories(category_id);

ALTER TABLE public.products
    ADD CONSTRAINT products_manufacturers_fk FOREIGN KEY (manufacturer_id) REFERENCES manufacturers(manufacturer_id);

ALTER TABLE public.shipping
    ADD CONSTRAINT shipping_clients_fk FOREIGN KEY (client_id) REFERENCES clients(client_id);