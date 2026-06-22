CREATE TABLE pedido (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT,
    username_usuario VARCHAR(50)
);

CREATE TABLE detalle_pedido (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    producto_id BIGINT NOT NULL,
    nombre_producto VARCHAR(50) NOT NULL,
    cantidad INT NOT NULL,
    pedido_id BIGINT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);