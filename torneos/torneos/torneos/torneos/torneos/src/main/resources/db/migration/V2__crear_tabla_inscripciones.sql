CREATE TABLE inscripciones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    torneo_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    username_usuario VARCHAR(50) NOT NULL,
    FOREIGN KEY (torneo_id) REFERENCES torneos(id) ON DELETE CASCADE
);