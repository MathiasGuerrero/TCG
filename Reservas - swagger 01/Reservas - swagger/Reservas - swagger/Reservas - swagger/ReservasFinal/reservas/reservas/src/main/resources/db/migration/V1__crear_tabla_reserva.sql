CREATE TABLE reservas(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto VARCHAR(40),
    cantidad INT(3),
    tipo_pago VARCHAR(40),
    monto INT(4)
);