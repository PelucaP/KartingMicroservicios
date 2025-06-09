import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import Tarifas from './components/Tarifas';
import AddEditEmployee from './components/AddEditEmployee';
import TarifasEspeciales from './components/TarifasEspeciales';
import AddEditExtraHours from './components/AddEditExtraHours';
import NotFound from './components/NotFound';
import DescuentoPersonas from './components/DescuentoPersonas';
import ReportesGenerar from './components/Reportes';
import AnualReport from './components/AnualReport';

function App() {
  return (
      <Router>
          <div className="container">
          <Navbar></Navbar>
            <Routes>
              <Route path="/home" element={<Home/>} />
              <Route path="/tarifas/consultar" element={<Tarifas/>} />
              <Route path="/employee/add" element={<AddEditEmployee/>} />
              <Route path="/employee/edit/:id" element={<AddEditEmployee/>} />
              <Route path="/descuentoPersonas/calcular" element={<DescuentoPersonas/>} />
              <Route path="/reportes/generar" element={<ReportesGenerar/>} />
              <Route path="/reports/AnualReport" element={<AnualReport/>} />
              <Route path="/tarifasEspeciales/consultar" element={<TarifasEspeciales/>} />
              <Route path="/extraHours/add" element={<AddEditExtraHours/>} />
              <Route path="/extraHours/edit/:id" element={<AddEditExtraHours/>} />
              <Route path="*" element={<NotFound/>} />
            </Routes>
          </div>
      </Router>
  );
}

export default App
