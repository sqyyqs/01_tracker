CREATE TABLE IF NOT EXISTS measure(
    id bigserial primary key,
    value float,
    is_successful boolean
);