import { useState } from "react";
import { useNavigate } from "react-router-dom";
import tarifasEspecialesService from "../services/tarifasEspeciales.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import CircularProgress from "@mui/material/CircularProgress";
import ListAltIcon from '@mui/icons-material/ListAlt';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline'; // Icon for creating

const TarifasEspecialesPage = () => {
  const [tarifasEspecialesList, setTarifasEspecialesList] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showList, setShowList] = useState(false);

  // State for creating a new Tarifa Especial
  const [newTipoTarifa, setNewTipoTarifa] = useState("");
  const [newDescuento, setNewDescuento] = useState("");
  const [isCreating, setIsCreating] = useState(false);
  const [createError, setCreateError] = useState(null);

  const navigate = useNavigate();

  const handleObtenerTarifasEspeciales = async () => {
    setIsLoading(true);
    setError(null);
    // setTarifasEspecialesList([]); // Keep existing list or clear, depends on desired UX
    setShowList(true);

    try {
      const response = await tarifasEspecialesService.getAllTarifasEspeciales();
      setTarifasEspecialesList(response.data);
    } catch (err) {
      console.error("Error al obtener las tarifas especiales:", err);
      if (err.response) {
        setError(`Error del servidor: ${err.response.status} - ${err.response.data.message || 'Intente nuevamente.'}`);
      } else if (err.request) {
        setError("No se pudo conectar al servidor.");
      } else {
        setError("Ocurrió un error al procesar la solicitud.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleCrearTarifaEspecial = async (event) => {
    event.preventDefault(); // Prevent form submission if wrapped in a form
    if (!newTipoTarifa.trim() || newDescuento === "" || isNaN(parseFloat(newDescuento))) {
      setCreateError("Por favor, ingrese un tipo de tarifa válido y un descuento numérico.");
      return;
    }
    setIsCreating(true);
    setCreateError(null);
    try {
      const newTarifaData = {
        tipoTarifa: newTipoTarifa,
        descuento: parseFloat(newDescuento),
        // id is not sent, it's generated by the backend
      };
      await tarifasEspecialesService.createTarifaEspecial(newTarifaData);
      setNewTipoTarifa(""); // Clear inputs
      setNewDescuento("");
      // Optionally, refresh the list after creation
      handleObtenerTarifasEspeciales(); 
    } catch (err) {
      console.error("Error al crear la tarifa especial:", err);
      if (err.response) {
        setCreateError(`Error del servidor al crear: ${err.response.status} - ${err.response.data.message || 'Intente nuevamente.'}`);
      } else if (err.request) {
        setCreateError("No se pudo conectar al servidor para crear la tarifa.");
      } else {
        setCreateError("Ocurrió un error al procesar la creación.");
      }
    } finally {
      setIsCreating(false);
    }
  };

  return (
    <Paper sx={{ padding: 3, margin: 2 }}>
      <Typography variant="h5" gutterBottom>
        Gestión de Tarifas Especiales
      </Typography>

      {/* Section to Create Tarifa Especial */}
      <Box component="form" onSubmit={handleCrearTarifaEspecial} sx={{ border: '1px solid #ccc', padding: 2, marginBottom: 3, borderRadius: 1 }}>
        <Typography variant="h6" gutterBottom>Crear Nueva Tarifa Especial</Typography>
        <TextField
          label="Tipo de Tarifa"
          variant="outlined"
          value={newTipoTarifa}
          onChange={(e) => setNewTipoTarifa(e.target.value)}
          fullWidth
          margin="normal"
          required
          disabled={isCreating}
        />
        <TextField
          label="Descuento (ej: 0.1 para 10%)"
          variant="outlined"
          type="number" // Allows for decimal input
          inputProps={{ step: "0.01" }} // Suggests step for number input
          value={newDescuento}
          onChange={(e) => setNewDescuento(e.target.value)}
          fullWidth
          margin="normal"
          required
          disabled={isCreating}
        />
        {createError && (
          <Typography color="error" sx={{ marginTop: 1, marginBottom:1 }}>
            {createError}
          </Typography>
        )}
        <Button
          type="submit" // Make it a submit button for the form
          variant="contained"
          color="secondary"
          startIcon={isCreating ? <CircularProgress size={20} color="inherit" /> : <AddCircleOutlineIcon />}
          disabled={isCreating}
          sx={{ marginTop: 1 }}
        >
          {isCreating ? "Creando..." : "Guardar Tarifa Especial"}
        </Button>
      </Box>

      {/* Section to List Tarifas Especiales */}
      <Box sx={{ marginBottom: 2 }}>
        <Button
          variant="contained"
          color="primary"
          startIcon={isLoading ? <CircularProgress size={20} color="inherit" /> : <ListAltIcon />}
          onClick={handleObtenerTarifasEspeciales}
          disabled={isLoading}
        >
          {isLoading ? "Cargando Lista..." : "Obtener/Actualizar Lista de Tarifas Especiales"}
        </Button>
      </Box>

      {showList && (
        <Box sx={{ marginTop: 2 }}>
          {isLoading && !isCreating && <CircularProgress />} {/* Show loader only if not creating */}
          {error && (
            <Typography color="error" sx={{ marginTop: 2 }}>
              {error}
            </Typography>
          )}
          {!isLoading && !error && tarifasEspecialesList.length > 0 && (
            <TableContainer component={Paper} sx={{ marginTop: 2 }}>
              <Table sx={{ minWidth: 650 }} size="small" aria-label="tabla de tarifas especiales">
                <TableHead>
                  <TableRow>
                    <TableCell sx={{ fontWeight: 'bold' }}>ID</TableCell>
                    <TableCell sx={{ fontWeight: 'bold' }}>Tipo de Tarifa</TableCell>
                    <TableCell sx={{ fontWeight: 'bold' }} align="right">Descuento</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {tarifasEspecialesList.map((tarifa) => (
                    <TableRow hover key={tarifa.id}>
                      <TableCell>{tarifa.id}</TableCell>
                      <TableCell>{tarifa.tipoTarifa}</TableCell>
                      <TableCell align="right">{(tarifa.descuento * 100).toFixed(0)}%</TableCell> 
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
          {!isLoading && !error && tarifasEspecialesList.length === 0 && (
            <Typography sx={{ marginTop: 2 }}>No se encontraron tarifas especiales o la lista está vacía.</Typography>
          )}
        </Box>
      )}
    </Paper>
  );
};

export default TarifasEspecialesPage;
