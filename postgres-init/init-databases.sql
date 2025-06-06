-- Create reservacomprobante database
CREATE DATABASE reservacomprobante;

-- Create descuentopersonas database
CREATE DATABASE descuentopersonas;

-- Create tarifas_especiales database
CREATE DATABASE tarifas_especiales;

-- Create tarifas database
CREATE DATABASE tarifas;

-- Create frecuencia_visitas database
CREATE DATABASE frecuencia_visitas;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE reservacomprobante TO postgres;
GRANT ALL PRIVILEGES ON DATABASE descuentopersonas TO postgres;
GRANT ALL PRIVILEGES ON DATABASE tarifas_especiales TO postgres;
GRANT ALL PRIVILEGES ON DATABASE tarifas TO postgres;
GRANT ALL PRIVILEGES ON DATABASE frecuencia_visitas TO postgres;