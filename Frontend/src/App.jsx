import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import Tarifas from './components/Tarifas';
import DescuentoFrecuencia from './components/DescuentoFrecuencia';
import TarifasEspeciales from './components/TarifasEspeciales';
import Rack from './components/RackReservas';
import NotFound from './components/NotFound';
import DescuentoPersonas from './components/DescuentoPersonas';
import ReportesGenerar from './components/Reportes';
import Reservas from './components/Reservas';

function App() {
  return (
      <Router>
          <div className="container">
          <Navbar></Navbar>
            <Routes>
              <Route path="/home" element={<Home/>} />
              <Route path="/tarifas/consultar" element={<Tarifas/>} />
              <Route path="/descuentoFrecuencia/calcular" element={<DescuentoFrecuencia/>} />
              <Route path="/descuentoPersonas/calcular" element={<DescuentoPersonas/>} />
              <Route path="/reportes/generar" element={<ReportesGenerar/>} />
              <Route path="/reservas/crear" element={<Reservas/>} />
              <Route path="/tarifasEspeciales/consultar" element={<TarifasEspeciales/>} />
              <Route path="/rack/reservas" element={<Rack/>} />
              <Route path="*" element={<NotFound/>} />
            </Routes>
          </div>
      </Router>
  );
}

export default App
