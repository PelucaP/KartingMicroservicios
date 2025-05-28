INSERT INTO karts(estado)
VALUES ('ACTIVO');
INSERT INTO karts(estado)
VALUES ('ACTIVO');
INSERT INTO karts(estado)
VALUES ('ACTIVO');
INSERT INTO karts(estado)
VALUES ('EN_MANTENCION');

INSERT INTO comprobante(totalvisita,tiporeserva,email)
VALUES (12321.03,'MINUTOS20','tomascarcamogonzalez@yahoo.cl'),
(312312.97,'MINUTOS30','tomascarcamogonzalez@yahoo.cl'),
(312324.97,'MINUTOS30','tomascarcamogonzalez@yahoo.cl'),
(12342.12,'MINUTOS10','tomascarcamogonzalez@yahoo.cl'),
(3122.97,'MINUTOS10','tomascarcamogonzalez@yahoo.cl');


INSERT INTO reserva (fechainicio, fechafin, email, duenoreserva,tiporeserva,total,cantidadpersonas) VALUES
('2023-10-01', '2023-10-05', 'john@example.com', 'John Doe','MINUTOS10',12321.03,1),
('2023-10-10', '2023-10-15', 'jane@example.com', 'Jane Smith','MINUTOS20',312312.97,4),
('2023-09-28', '2023-10-02', 'mike@example.com', 'Mike Johnson','MINUTOS20',312324.97,6),
('2023-09-15', '2023-09-20', 'sarah@example.com', 'Sarah Wilson','MINUTOS30',12342.12,8),
('2023-10-30', '2023-11-05', 'chris@example.com', 'Chris Brown','MINUTOS20',3122.97,10);