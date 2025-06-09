import { useState } from "react";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import descuentoFrecuenciaService from "../services/descuentoFrecuencia.service";
import CircularProgress from "@mui/material/CircularProgress";
import Typography from "@mui/material/Typography";

const DescuentoFrecuenciaComponent = () => {
  const [visitasInput, setVisitasInput] = useState("");
  const [descuentoData, setDescuentoData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [visitasConsultadas, setVisitasConsultadas] = useState("");

  const handleInputChange = (event) => {
    setVisitasInput(event.target.value);
  };

  const handleConsultarDescuento = async () => {
    if (!visitasInput || isNaN(parseInt(visitasInput)) || parseInt(visitasInput) < 0) {
      setError("Por favor, ingrese un número válido de visitas (0 o más).");
      setDescuentoData(null);
      setVisitasConsultadas("");
      return;
    }
    setIsLoading(true);
    setError(null);
    setDescuentoData(null);

    try {
      const response = await descuentoFrecuenciaService.getDescuentoByVisitas(visitasInput);
      setDescuentoData(response.data);
      setVisitasConsultadas(visitasInput);
    } catch (err) {
      console.error("Error al obtener el descuento por frecuencia:", err);
      setVisitasConsultadas("");
      if (err.response) {
        setError(
          `Error del servidor: ${err.response.status} - ${
            err.response.data?.message || err.response.data || "Intente nuevamente."
          }`
        );
      } else if (err.request) {
        setError(
          "No se pudo conectar al servidor. Verifique la URL del API Gateway y que esté funcionando."
        );
      } else {
        setError("Ocurrió un error al procesar la solicitud.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      sx={{ width: '100%', mt: 2 }}
    >
      <Paper
        sx={{
          padding: 3,
          marginTop: 2,
          maxWidth: "md",
          width: '100%',
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography
          variant="h5"
          gutterBottom
          sx={{ textAlign: "center", width: "100%" }}
        >
          Consultar Descuento por Frecuencia de Visitas
        </Typography>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 2,
            padding: 2,
            border: "1px solid #ddd",
            borderRadius: 1,
            width: "fit-content",
            marginBottom: 2,
          }}
        >
          <TextField
            label="Número de Visitas Anteriores"
            variant="outlined"
            value={visitasInput}
            onChange={handleInputChange}
            type="number"
            size="small"
            sx={{ width: "100%" }}
            inputProps={{ min: 0 }}
          />
          <Button
            variant="contained"
            color="primary"
            onClick={handleConsultarDescuento}
            disabled={isLoading}
            sx={{ width: "auto", minWidth: "180px" }}
          >
            {isLoading ? (
              <CircularProgress size={24} color="inherit" />
            ) : (
              "Consultar Descuento"
            )}
          </Button>
        </Box>

        {error && (
          <Typography
            color="error"
            sx={{ marginTop: 2, textAlign: "center" }}
          >
            {error}
          </Typography>
        )}

        {descuentoData !== null && !isLoading && !error && visitasConsultadas && (
          <Box
            sx={{
              marginTop: 2,
              padding: 2,
              border: "1px solid #ddd",
              borderRadius: 1,
              width: "fit-content",
              minWidth: "300px",
            }}
          >
            <Typography variant="h6">Resultado del Descuento:</Typography>
            <Typography>
              Para <strong>{visitasConsultadas}</strong> visita(s) anterior(es), el descuento
              aplicable es:{" "}
              <strong>{(descuentoData * 100).toFixed(0)}%</strong>
            </Typography>
          </Box>
        )}
      </Paper>
    </Box>
  );
};

export default DescuentoFrecuenciaComponent;
