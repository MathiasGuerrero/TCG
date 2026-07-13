CREATE TABLE torneos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombreTorneo VARCHAR(50),
    participantes INT(3),
    premio VARCHAR(50),
    duracion INT(1)
);