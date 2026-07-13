CREATE TABLE wishlist (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    username_usuario VARCHAR(50) NOT NULL,
    producto_id BIGINT NOT NULL,
    nombre_producto VARCHAR(50) NOT NULL,
    precio_producto DECIMAL(8,2) NOT NULL
);