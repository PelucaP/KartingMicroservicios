import { useState } from "react";
import reportesService from "../services/reportes.service";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import CircularProgress from "@mui/material/CircularProgress";
import DownloadIcon from "@mui/icons-material/Download"; // Icon for download buttons

// Helper function to trigger file download
const downloadFile = (blob, filename) => {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.setAttribute("download", filename);
  document.body.appendChild(link);
  link.click();
  link.parentNode.removeChild(link);
  window.URL.revokeObjectURL(url);
};

const ReportesPage = () => {
  const [fechaInicio, setFechaInicio] = useState("");
  const [fechaFin, setFechaFin] = useState("");
  const [isLoadingVueltas, setIsLoadingVueltas] = useState(false);
  const [isLoadingCantidadGente, setIsLoadingCantidadGente] = useState(false);
  const [error, setError] = useState(null);

  const handleGenerateReport = async (reportType) => {
    if (!fechaInicio || !fechaFin) {
      setError("Por favor, seleccione ambas fechas.");
      return;
    }
    if (new Date(fechaInicio) > new Date(fechaFin)) {
      setError("La fecha de inicio no puede ser posterior a la fecha de fin.");
      return;
    }

    setError(null);
    let filename = "reporte.pdf";

    try {
      let response;
      if (reportType === "vueltas") {
        setIsLoadingVueltas(true);
        filename = "reporte_ingresos_vueltas.pdf";
        response = await reportesService.descargarReporteVueltas(
          fechaInicio,
          fechaFin
        );
      } else if (reportType === "cantidadgente") {
        setIsLoadingCantidadGente(true);
        filename = "reporte_ingresos_asistencia.pdf";
        response = await reportesService.descargarReporteCantidadGente(
          fechaInicio,
          fechaFin
        );
      } else {
        return; // Should not happen
      }

      downloadFile(response.data, filename);
    } catch (err) {
      console.error(`Error al generar el reporte de ${reportType}:`, err);
      let errorMessage = "Ocurrió un error al generar el reporte.";
      if (
        err.response &&
        err.response.data &&
        err.response.data.type === "application/json"
      ) {
        // If the error response is JSON, try to parse it
        // This might happen if the server sends a JSON error instead of a PDF for some reason
        try {
          const errorData = await err.response.data.text(); // Read blob as text
          const parsedError = JSON.parse(errorData);
          errorMessage = parsedError.message || "Error del servidor.";
        } catch (parseError) {
          errorMessage = "Error del servidor al procesar la respuesta de error.";
        }
      } else if (err.response && err.response.status) {
        errorMessage = `Error del servidor: ${err.response.status}. Intente nuevamente.`;
      }
      setError(errorMessage);
    } finally {
      if (reportType === "vueltas") setIsLoadingVueltas(false);
      if (reportType === "cantidadgente") setIsLoadingCantidadGente(false);
    }
  };

  return (
    <Paper
      sx={{
        padding: 3,
        margin: "auto",
        marginTop: 4,
        maxWidth: "md",
        backgroundColor: "#f5f5f5",
        borderRadius: "8px",
        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
      }}
    >
      <Typography variant="h5" gutterBottom align="center">
        Generar Reportes PDF
      </Typography>
      <Box
        component="form"
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 2,
        }}
      >
        <TextField
          id="fechaInicio"
          label="Fecha de Inicio"
          type="date"
          value={fechaInicio}
          onChange={(e) => setFechaInicio(e.target.value)}
          InputLabelProps={{ shrink: true }}
          required
          sx={{ width: "80%", maxWidth: "300px" }}
        />
        <TextField
          id="fechaFin"
          label="Fecha de Fin"
          type="date"
          value={fechaFin}
          onChange={(e) => setFechaFin(e.target.value)}
          InputLabelProps={{ shrink: true }}
          required
          sx={{ width: "80%", maxWidth: "300px" }}
        />

        {error && (
          <Typography color="error" sx={{ mt: 1 }}>
            {error}
          </Typography>
        )}

        <Box
          sx={{
            display: "flex",
            gap: 2,
            marginTop: 2,
            flexWrap: "wrap",
            justifyContent: "center",
          }}
        >
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleGenerateReport("vueltas")}
            disabled={isLoadingVueltas || isLoadingCantidadGente}
            startIcon={
              isLoadingVueltas ? (
                <CircularProgress size={20} color="inherit" />
              ) : (
                <DownloadIcon />
              )
            }
            sx={{ minWidth: "250px" }}
          >
            {isLoadingVueltas
              ? "Generando..."
              : "Reporte Ingresos por Vueltas"}
          </Button>
          <Button
            variant="contained"
            color="secondary"
            onClick={() => handleGenerateReport("cantidadgente")}
            disabled={isLoadingVueltas || isLoadingCantidadGente}
            startIcon={
              isLoadingCantidadGente ? (
                <CircularProgress size={20} color="inherit" />
              ) : (
                <DownloadIcon />
              )
            }
            sx={{ minWidth: "250px" }}
          >
            {isLoadingCantidadGente
              ? "Generando..."
              : "Reporte Ingresos por Asistencia"}
          </Button>
        </Box>
      </Box>
    </Paper>
  );
};

export default ReportesPage;
