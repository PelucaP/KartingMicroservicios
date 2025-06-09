import * as React from "react";
// Removed BarChart import: import { BarChart } from "@mui/x-charts/BarChart";
import Box from "@mui/material/Box";
import { useState } from "react";
import reservasService from "../services/reservas.service";
import Paper from "@mui/material/Paper";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import CircularProgress from "@mui/material/CircularProgress";
import Alert from "@mui/material/Alert"; // For success/error messages

// Removed dataset constant
// const dataset = [ ... ];

// Removed valueFormatter constant
// const valueFormatter = (value) => `$ ${value.toLocaleString("en-US")}`;

const initialFormState = {
  cantidadPersonas: "",
  duenoReserva: "",
  tipoReserva: "", // e.g., 1 for 10 vueltas, 2 for 15, etc.
  fechaReserva: "", // Format: "YYYY-MM-DDTHH:mm" for LocalDateTime or "YYYY-MM-DD" for Date
  email: "",
  tarifaEspecial: "", // ID of the special tariff, 0 or empty if none
  cantidadFrecuencia: "",
};

// Changed component name from AnualReport to ReservasPage for consistency
const ReservasPage = () => {
  const [formData, setFormData] = useState(initialFormState);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);
    setSuccessMessage(null);
    setIsLoading(true);

    // Basic Validation (can be expanded)
    if (
      !formData.cantidadPersonas ||
      !formData.duenoReserva ||
      !formData.tipoReserva ||
      !formData.fechaReserva ||
      !formData.email
    ) {
      setError(
        "Por favor, complete todos los campos obligatorios: Cantidad de Personas, Dueño de Reserva, Tipo de Reserva, Fecha y Email."
      );
      setIsLoading(false);
      return;
    }
    if (isNaN(parseInt(formData.cantidadPersonas)) || parseInt(formData.cantidadPersonas) <= 0) {
      setError("La cantidad de personas debe ser un número positivo.");
      setIsLoading(false);
      return;
    }
     if (isNaN(parseInt(formData.tipoReserva))) {
      setError("El tipo de reserva debe ser un número.");
      setIsLoading(false);
      return;
    }
    if (formData.tarifaEspecial && isNaN(parseInt(formData.tarifaEspecial))) {
      setError("La tarifa especial debe ser un número (ID) o dejarse vacía.");
      setIsLoading(false);
      return;
    }
    if (formData.cantidadFrecuencia && (isNaN(parseInt(formData.cantidadFrecuencia)) || parseInt(formData.cantidadFrecuencia) < 0 )) {
      setError("La cantidad de frecuencia debe ser un número positivo o cero.");
      setIsLoading(false);
      return;
    }

    const reservaPayload = {
      ...formData,
      cantidadPersonas: parseInt(formData.cantidadPersonas),
      tipoReserva: parseInt(formData.tipoReserva),
      fechaReserva: formData.fechaReserva,
      tarifaEspecial: formData.tarifaEspecial ? parseInt(formData.tarifaEspecial) : 0,
      cantidadFrecuencia: formData.cantidadFrecuencia ? parseInt(formData.cantidadFrecuencia) : 0,
    };

    try {
      await reservasService.createReserva(reservaPayload);
      setSuccessMessage("El comprobante ha sido enviado al correo.");
      setFormData(initialFormState); // Reset form on success
    } catch (err) {
      console.error("Error al crear la reserva:", err);
      if (err.response && err.response.data) {
        setError(err.response.data.message || "Error del servidor al crear la reserva.");
      } else {
        setError("Ocurrió un error al crear la reserva. Intente nuevamente.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    // The main Box can be simplified or removed if Paper is the top-level desired layout
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center" // This will center the Paper component
      sx={{ width: '100%', mt: 2 }} // Added margin top to the Box
    >
      {/* Removed BarChart component */}
      <Paper sx={{ padding: 3, /* margin: "auto", // Not needed if Box centers it */ marginTop: 2, maxWidth: "md", width: '100%' }}>
        <Typography variant="h5" gutterBottom align="center">
          Crear Nueva Reserva
        </Typography>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
          <TextField
            label="Cantidad de Personas"
            name="cantidadPersonas"
            type="number"
            value={formData.cantidadPersonas}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
            inputProps={{ min: 1 }}
          />
          <TextField
            label="Dueño de la Reserva (Nombre)"
            name="duenoReserva"
            type="text"
            value={formData.duenoReserva}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Tipo de Reserva (Número, ej: 1=10 vueltas)"
            name="tipoReserva"
            type="number"
            value={formData.tipoReserva}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Fecha y Hora de Reserva"
            name="fechaReserva"
            type="datetime-local"
            value={formData.fechaReserva}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            label="Email de Contacto"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="ID Tarifa Especial (Opcional, 0 si no aplica)"
            name="tarifaEspecial"
            type="number"
            value={formData.tarifaEspecial}
            onChange={handleChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="Cantidad de Frecuencia (Visitas Anteriores)"
            name="cantidadFrecuencia"
            type="number"
            value={formData.cantidadFrecuencia}
            onChange={handleChange}
            fullWidth
            margin="normal"
            inputProps={{ min: 0 }}
          />

          {error && (
            <Alert severity="error" sx={{ mt: 2, mb: 1 }}>
              {error}
            </Alert>
          )}
          {successMessage && (
            <Alert severity="success" sx={{ mt: 2, mb: 1 }}>
              {successMessage}
            </Alert>
          )}

          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            disabled={isLoading}
            sx={{ mt: 2, mb: 2 }}
          >
            {isLoading ? (
              <CircularProgress size={24} color="inherit" />
            ) : (
              "Crear Reserva y Enviar Comprobante"
            )}
          </Button>
        </Box>
      </Paper>
    </Box>
  );
};

export default ReservasPage; // Changed export name
