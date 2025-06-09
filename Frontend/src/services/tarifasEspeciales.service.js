import httpClient from "../http-common"; // Assuming http-common.js points to your API Gateway

const getAllTarifasEspeciales = () => {
  // This endpoint in your controller is "api/tarifaespecial/obtener"
  return httpClient.get("/api/tarifaespecial/obtener");
};

const getTarifaEspecialById = (id) => {
  // This endpoint in your controller is "api/tarifaespecial/obtenertarifa/{id}"
  return httpClient.get(`/api/tarifaespecial/obtenertarifa/${id}`);
};

const createTarifaEspecial = (tarifaData) => {
  // This endpoint in your controller is "api/tarifaespecial/creartarifa"
  // tarifaData should be an object like { tipoTarifa: "someType", descuento: 0.10 }
  return httpClient.post("/api/tarifaespecial/creartarifa", tarifaData);
};

export default { 
  getAllTarifasEspeciales,
  getTarifaEspecialById,
  createTarifaEspecial 
};