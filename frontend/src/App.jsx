import { Route, Router } from "react-router-dom";
import "./App.css";
import Accounting from "./components/Accounting";
import Dashboard from "./components/Dashboard";
import Report from "./components/Report";
import Statements from "./components/Statements";

function App() {
  <div className="app">
    <Router>
      <Route path="/" element={<Dashboard />} />
      <Route path="/accounting" element={<Accounting />} />
      <Route path="/reports" element={<Report />} />
      <Route path="/statements" element={<Statements />} />
      <Route path="/accounting" element={<Accounting />} />
    </Router>
  </div>;
}

export default App;
