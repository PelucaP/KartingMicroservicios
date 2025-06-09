import httpClient from "../http-common";

const createReserva = (reservaData) => {
  // reservaData should be an object matching the ReservaRequest DTO
  // { cantidadPersonas, duenoReserva, tipoReserva, fechaReserva, email, tarifaEspecial, cantidadFrecuencia }
  return httpClient.post("/api/reserva/crear", reservaData);
};

export default {
  createReserva,
};