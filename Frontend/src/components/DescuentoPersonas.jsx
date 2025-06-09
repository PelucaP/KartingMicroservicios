import { useState } from "react";
import Paper from "@mui/material/Paper";
import descuentoPersonasService from "../services/descuentoPersonas.service";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import CircularProgress from "@mui/material/CircularProgress";

const DescuentoPersonasComponent = () => {
  const [personasInput, setPersonasInput] = useState("");
  const [descuentoData, setDescuentoData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [personasConsultadas, setPersonasConsultadas] = useState(""); // New state variable

  const handleInputChange = (event) => {
    setPersonasInput(event.target.value);
  };

  const handleConsultarDescuento = async () => {
    if (!personasInput || isNaN(parseInt(personasInput)) || parseInt(personasInput) <= 0) {
      setError("Por favor, ingrese un número válido de personas (mayor que 0).");
      setDescuentoData(null);
      setPersonasConsultadas(""); // Clear previous consulted value on error
      return;
    }
    setIsLoading(true);
    setError(null);
    setDescuentoData(null);
    // setPersonasConsultadas(""); // Clear here or only on success/error

    try {
      const response = await descuentoPersonasService.getDescuentoByPersonas(personasInput);
      setDescuentoData(response.data);
      setPersonasConsultadas(personasInput); // SET IT HERE
    } catch (err) {
      console.error("Error al obtener el descuento por personas:", err);
      setPersonasConsultadas(""); // Clear it on error too, or if the input was invalid
      if (err.response) {
        if (err.response.status === 404) {
          setError(`No se pudo calcular un descuento para ${personasInput} personas.`);
        } else {
          setError(
            `Error del servidor: ${err.response.status} - ${
              err.response.data?.message || err.response.data || "Intente nuevamente."
            }`
          );
        }
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
    <Paper
      sx={{
        padding: 3,
        margin: "auto",
        marginTop: 4,
        maxWidth: "md",
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
        Consultar Descuento por Número de Personas
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
          label="Número de Personas"
          variant="outlined"
          value={personasInput}
          onChange={handleInputChange}
          type="number"
          size="small"
          sx={{ width: "100%" }}
          inputProps={{ min: 1 }} // Basic HTML5 validation
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handleConsultarDescuento}
          disabled={isLoading}
          sx={{ width: "auto", minWidth: "180px" }} // Adjusted width
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

      {descuentoData !== null && !isLoading && !error && personasConsultadas && ( // Check personasConsultadas
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
            Para <strong>{personasConsultadas}</strong> persona(s), el descuento {/* Use personasConsultadas here */}
            aplicable es:{" "}
            <strong>{(descuentoData * 100).toFixed(0)}%</strong>
          </Typography>
        </Box>
      )}
    </Paper>
  );
};

export default DescuentoPersonasComponent;
