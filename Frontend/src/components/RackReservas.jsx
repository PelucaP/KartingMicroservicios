import { useState, useEffect } from "react";
import rackService from "../services/rack.service"; // Import the new service
import { Calendar, dateFnsLocalizer } from "react-big-calendar";
import format from "date-fns/format";
import parse from "date-fns/parse";
import startOfWeek from "date-fns/startOfWeek";
import getDay from "date-fns/getDay";
import enUS from "date-fns/locale/en-US"; // Or your preferred locale
import "react-big-calendar/lib/css/react-big-calendar.css"; // Default styles
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import CircularProgress from "@mui/material/CircularProgress";
import Alert from "@mui/material/Alert";
import Box from "@mui/material/Box";

// Setup for react-big-calendar localizer
const locales = {
  "en-US": enUS, // Add other locales if needed
};
const localizer = dateFnsLocalizer({
  format,
  parse,
  startOfWeek,
  getDay,
  locales,
});

// Renaming component for clarity
const RackReservasCalendarPage = () => {
  const [reservas, setReservas] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchReservas = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await rackService.getAllReservasForRack();
        // Transform data for react-big-calendar
        // The DTO has: fechaInicio, fechaFin, nombre
        const calendarEvents = response.data.map((reserva) => ({
          title: reserva.nombre, // 'nombre' from ReservaRackRequest
          start: new Date(reserva.fechaInicio), // Ensure these are valid Date objects
          end: new Date(reserva.fechaFin),     // Ensure these are valid Date objects
          allDay: false, // Assuming reservations have specific times
          resource: reserva, // Optional: store original reserva data
        }));
        setReservas(calendarEvents);
      } catch (err) {
        console.error("Error fetching rack reservas:", err);
        setError(
          "No se pudieron cargar las reservas. Intente nuevamente más tarde."
        );
      } finally {
        setIsLoading(false);
      }
    };

    fetchReservas();
  }, []); // Empty dependency array means this runs once on mount

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" sx={{ minHeight: '80vh' }}>
        <CircularProgress />
        <Typography sx={{ ml: 2 }}>Cargando reservas...</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" sx={{ minHeight: '80vh', p: 2 }}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  return (
    <Paper sx={{ margin: "auto", marginTop: 2, padding: 2, maxWidth: "xl" }}>
      <Typography variant="h4" gutterBottom align="center" sx={{ mb: 2 }}>
        Rack de Reservas
      </Typography>
      <Box sx={{ height: "75vh" }}> {/* Adjust height as needed */}
        <Calendar
          localizer={localizer}
          events={reservas}
          startAccessor="start"
          endAccessor="end"
          style={{ height: "100%" }}
          defaultView="month" // Can be 'week', 'day', 'agenda'
          views={["month", "week", "day", "agenda"]}
          selectable
          // onSelectSlot={(slotInfo) => {
          //   // Handle slot selection for creating new events, if needed
          //   console.log("Selected slot:", slotInfo);
          // }}
          // onSelectEvent={(event) => {
          //   // Handle event selection for viewing details, if needed
          //   console.log("Selected event:", event);
          // }}
        />
      </Box>
    </Paper>
  );
};

export default RackReservasCalendarPage;
