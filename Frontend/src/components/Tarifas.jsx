import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import employeeService from "../services/employee.service"; // Keep if other parts of the page still use it
import tarifaService from "../services/tarifa.service"; // Import the new tarifa service
import TableContainer from "@mui/material/TableContainer";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
// Remove PersonAddIcon, EditIcon, DeleteIcon if no longer used for employee list
// import PersonAddIcon from "@mui/icons-material/PersonAdd";
// import EditIcon from "@mui/icons-material/Edit";
// import DeleteIcon from "@mui/icons-material/Delete";
// Remove direct axios import if http-common is used via service
// import axios from "axios"; 
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import CircularProgress from "@mui/material/CircularProgress";

// Remove API_GATEWAY_URL if all calls go through services using http-common.js
// const API_GATEWAY_URL = "http://api-gateway:8085"; 

// Consider renaming EmployeeList if the component's main focus is now Tarifa Inquiry
const TarifaInquiryComponent = () => { 
  // Remove employee state if the employee list is removed from this component
  // const [employees, setEmployees] = useState([]); 
  const [tipoTarifaInput, setTipoTarifaInput] = useState("");
  const [tarifaData, setTarifaData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  // Remove init, handleDelete, handleEdit if the employee list is removed
  /*
  const init = () => {
    employeeService
      .getAll()
      .then((response) => {
        console.log("Mostrando listado de todos los empleados.", response.data);
        setEmployees(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de todos los empleados.",
          error
        );
      });
  };

  useEffect(() => {
    // init(); // Remove if employee list is gone
  }, []);

  const handleDelete = (id) => { ... };
  const handleEdit = (id) => { ... };
  */

  const handleInputChange = (event) => {
    setTipoTarifaInput(event.target.value);
  };

  const handlePreguntarTarifa = async () => {
    if (!tipoTarifaInput || isNaN(parseInt(tipoTarifaInput))) {
      setError("Por favor, ingrese un número válido para el tipo de tarifa.");
      setTarifaData(null);
      return;
    }
    setIsLoading(true);
    setError(null);
    setTarifaData(null);

    try {
      const response = await tarifaService.getTarifaByTipo(tipoTarifaInput); // Use the service
      setTarifaData(response.data);
    } catch (err) {
      console.error("Error al obtener la tarifa:", err);
      if (err.response) {
        if (err.response.status === 404) {
          setError(`No se encontró una tarifa para el tipo: ${tipoTarifaInput}.`);
        } else {
          setError(
            `Error del servidor: ${err.response.status} - ${
              err.response.data?.message || "Intente nuevamente." // Optional chaining for message
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
    // Changed to Paper to provide a distinct background for the centered content
    <Paper sx={{ 
        padding: 3, 
        margin: 'auto', // For horizontal centering of the Paper itself
        marginTop: 4, 
        maxWidth: 'md', // Optional: constrain the max width
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center' // Center children horizontally
      }}>
      <Typography variant="h5" gutterBottom sx={{ textAlign: 'center', width: '100%' }}>
        Consultar Tarifa
      </Typography>
      <Box
        sx={{
          display: "flex",
          flexDirection: 'column', // Stack input and button vertically
          alignItems: "center", // Center items in the column
          gap: 2,
          padding: 2, // Added padding around the input/button group
          border: "1px solid #ddd",
          borderRadius: 1,
          width: 'fit-content', // Shrink to content width
          marginBottom: 2,
        }}
      >
        <TextField
          label="Tipo de Tarifa (Número)"
          variant="outlined"
          value={tipoTarifaInput}
          onChange={handleInputChange}
          type="number"
          size="small"
          sx={{width: '100%'}} // Make text field take available width
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handlePreguntarTarifa}
          disabled={isLoading}
          sx={{width: 'auto', minWidth: '150px'}} // Give button a decent width
        >
          {isLoading ? (
            <CircularProgress size={24} color="inherit" />
          ) : (
            "Preguntar Tarifa"
          )}
        </Button>
      </Box>

      {error && (
        <Typography color="error" sx={{ marginTop: 2, textAlign: 'center' }}>
          {error}
        </Typography>
      )}

      {tarifaData && (
        <Box
          sx={{
            marginTop: 2,
            padding: 2,
            border: "1px solid #ddd",
            borderRadius: 1,
            width: 'fit-content', // Shrink to content
            minWidth: '300px' // Ensure it has some minimum width
          }}
        >
          <Typography variant="h6">Detalles de la Tarifa:</Typography>
          <Typography>
            <strong>Tipo de Tarifa:</strong> {tarifaData.tipoTarifa}
          </Typography>
          <Typography>
            <strong>Tiempo Total (minutos):</strong> {tarifaData.tiempoTotal}
          </Typography>
          <Typography>
            <strong>Precio por Persona:</strong> ${tarifaData.precioPersona}
          </Typography>
        </Box>
      )}
      {/* The TableContainer for employees is removed. If you still need it, 
          you'll need to manage its state and rendering separately. */}
    </Paper>
  );
};

// If you renamed the component, update the export
export default TarifaInquiryComponent; 
// export default EmployeeList; // If you kept the old name
