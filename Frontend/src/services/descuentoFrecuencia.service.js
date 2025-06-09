import httpClient from "../http-common";

const getDescuentoByVisitas = (cantVisitas) => {
  // The endpoint in your DescuentoFrecuenciaController is "api/descuentofrecuencia/{cantvisitas}"
  return httpClient.get(`/api/descuentofrecuencia/${cantVisitas}`);
};

export default { getDescuentoByVisitas };