import { Route, Router } from "react-router-dom";
import "./App.css";
import "./components/Dashboard";
import "./components/Report";

function App() {
  <div className="app">
    <Router>
      <Route path="/" element={<Dashboard />} />
      <Route path="/accounting" element={<Accounting />} />
      <Route path="/reports" element={<Report />} />
      <Route path="/accounting" element={<Accounting />} />
      <Route path="/accounting" element={<Accounting />} />
    </Router>
  </div>;
}

export default App;
