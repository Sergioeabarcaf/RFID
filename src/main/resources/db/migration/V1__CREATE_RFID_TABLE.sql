CREATE TABLE IF NOT EXISTS rfid (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    epc VARCHAR(255) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);