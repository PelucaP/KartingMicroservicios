import httpClient from "../http-common";

const getTarifaByTipo = (tipoTarifa) => {
  // The endpoint in your TarifaController is "api/tarifa/{tipoTarifa}"
  // and http-common should be configured to point to your API Gateway
  return httpClient.get(`/api/tarifa/${tipoTarifa}`);
};

export default { getTarifaByTipo };