import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./App.css";
import Accounting from "./components/Accounting";
import Dashboard from "./components/Dashboard";
import Report from "./components/Report";
import Statements from "./components/Statements";
import NavBar from "./components/NavBar";

function App() {
  return (
    <div className="app">
      <div className="nav">
        <NavBar />
      </div>
      .main
      <Routes>
        <NavBar />
        <Route path="/" element={<Dashboard />} />
        <Route path="/accounting" element={<Accounting />} />
        <Route path="/reports" element={<Report />} />
        <Route path="/statements" element={<Statements />} />
      </Routes>
    </div>
  );
}

export default App;
