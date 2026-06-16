select setval(
    pg_get_serial_sequence('interactions', 'id'),
    coalesce((select max(id) from interactions), 0) + 1,
    false
);
