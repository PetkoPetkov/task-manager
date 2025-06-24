CREATE TABLE IF NOT EXISTS T_TASK
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    title       VARCHAR(250)                      NOT NULL,
    description VARCHAR(500)                      NULL,
    due_date    TIMESTAMP                         NOT NULL,
    status      VARCHAR(20)                       NOT NULL,
    user_id     BIGINT                            NULL,
    depends_on  BIGINT                            NULL,
    FOREIGN KEY (user_id) REFERENCES T_USER (id),
    FOREIGN KEY (depends_on) REFERENCES T_TASK (id)
);
