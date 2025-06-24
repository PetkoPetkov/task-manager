INSERT INTO t_task (title, description, due_date, status, user_id, depends_on)
     VALUES ('First task', 'Task not depend on another one', '2025-06-24', '0', null, null);
INSERT INTO t_task (title, description, due_date, status, user_id, depends_on)
     VALUES ('Second task', '', '2025-06-24', '0', null, null);
INSERT INTO t_task (title, description, due_date, status, user_id, depends_on)
     VALUES ('Third task', 'Task depend on second one', '2025-06-24', '0', null, 2);
