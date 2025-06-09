import httpClient from "../http-common";

const getDescuentoByPersonas = (numeroPersonas) => {
  // The endpoint in your DescuentoController is "api/descuento/{personas}"
  return httpClient.get(`/api/descuento/${numeroPersonas}`);
};

export default { getDescuentoByPersonas };