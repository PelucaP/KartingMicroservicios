import httpClient from "../http-common";

const descargarReporteVueltas = (fechaInicio, fechaFin) => {
  return httpClient.post(
    "/api/reporte/vueltas",
    { fechaInicio, fechaFin },
    { responseType: "blob" } // Important for file download
  );
};

const descargarReporteCantidadGente = (fechaInicio, fechaFin) => {
  return httpClient.post(
    "/api/reporte/cantidadgente",
    { fechaInicio, fechaFin },
    { responseType: "blob" } // Important for file download
  );
};

export default {
  descargarReporteVueltas,
  descargarReporteCantidadGente,
};