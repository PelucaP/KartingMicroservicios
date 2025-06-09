import httpClient from "../http-common";

const getAllReservasForRack = () => {
  // Corresponds to @GetMapping("/reservas") in RackController
  // which is /api/rack/reservas
  return httpClient.get("/api/rack/reservas");
};

export default {
  getAllReservasForRack,
};