import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import employeeService from "../services/employee.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import axios from "axios"; // For making HTTP requests
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import CircularProgress from "@mui/material/CircularProgress";

// Configuration for the API Gateway
// Adjust this URL if your API Gateway is accessible differently
// For local development with port-forwarding or minikube tunnel for the gateway:
const API_GATEWAY_URL = "http://api-gateway:8085";
// If your frontend is running in k8s and can resolve service names,
// you might use something like: "http://api-gateway-service-name:8085"
// Or the specific IP: "http://10.101.84.211:8085" (ensure this is reachable from where the frontend JS executes)

const EmployeeList = () => {
  const [employees, setEmployees] = useState([]);
  const [tipoTarifaInput, setTipoTarifaInput] = useState("");
  const [tarifaData, setTarifaData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

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
    init();
  }, []);

  const handleDelete = (id) => {
    console.log("Printing id", id);
    const confirmDelete = window.confirm(
      "¿Esta seguro que desea borrar este empleado?"
    );
    if (confirmDelete) {
      employeeService
        .remove(id)
        .then((response) => {
          console.log("empleado ha sido eliminado.", response.data);
          init();
        })
        .catch((error) => {
          console.log(
            "Se ha producido un error al intentar eliminar al empleado",
            error
          );
        });
    }
  };

  const handleEdit = (id) => {
    console.log("Printing id", id);
    navigate(`/employee/edit/${id}`);
  };

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
      // The endpoint in your TarifaController is "api/tarifa/{tipoTarifa}"
      const response = await axios.get(
        `${API_GATEWAY_URL}/api/tarifa/${tipoTarifaInput}`
      );
      setTarifaData(response.data);
    } catch (err) {
      console.error("Error al obtener la tarifa:", err);
      if (err.response) {
        if (err.response.status === 404) {
          setError(`No se encontró una tarifa para el tipo: ${tipoTarifaInput}.`);
        } else {
          setError(
            `Error del servidor: ${err.response.status} - ${
              err.response.data.message || "Intente nuevamente."
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
    <TableContainer component={Paper}>
      <br />
      <Link
        to="/employee/add"
        style={{ textDecoration: "none", marginBottom: "1rem" }}
      >
        <Button
          variant="contained"
          color="primary"
          startIcon={<PersonAddIcon />}
        >
          Añadir Empleado
        </Button>
      </Link>
      <br /> <br />
      <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
        <TableHead>
          <TableRow>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Rut
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Nombre
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Sueldo
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Nro.Hijos
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Categoria
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Operaciones
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {employees.map((employee) => (
            <TableRow
              key={employee.id}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell align="left">{employee.rut}</TableCell>
              <TableCell align="left">{employee.name}</TableCell>
              <TableCell align="right">{employee.salary}</TableCell>
              <TableCell align="right">{employee.children}</TableCell>
              <TableCell align="right">{employee.category}</TableCell>
              <TableCell>
                <Button
                  variant="contained"
                  color="info"
                  size="small"
                  onClick={() => handleEdit(employee.id)}
                  style={{ marginLeft: "0.5rem" }}
                  startIcon={<EditIcon />}
                >
                  Editar
                </Button>

                <Button
                  variant="contained"
                  color="error"
                  size="small"
                  onClick={() => handleDelete(employee.id)}
                  style={{ marginLeft: "0.5rem" }}
                  startIcon={<DeleteIcon />}
                >
                  Eliminar
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Box
        sx={{
          marginTop: 4,
          padding: 3,
          border: "1px solid #ddd",
          borderRadius: 1,
        }}
      >
        <Typography variant="h6" gutterBottom>
          Consultar Tarifa
        </Typography>
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            gap: 2,
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
          />
          <Button
            variant="contained"
            color="primary"
            onClick={handlePreguntarTarifa}
            disabled={isLoading}
          >
            {isLoading ? (
              <CircularProgress size={24} color="inherit" />
            ) : (
              "Preguntar Tarifa"
            )}
          </Button>
        </Box>

        {error && (
          <Typography color="error" sx={{ marginTop: 2 }}>
            {error}
          </Typography>
        )}

        {tarifaData && (
          <Box
            sx={{
              marginTop: 3,
              padding: 2,
              border: "1px solid #ddd",
              borderRadius: 1,
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
      </Box>
    </TableContainer>
  );
};

export default EmployeeList;
