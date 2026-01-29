import { Routes, Route } from "react-router-dom";
import "./App.css";
import Accounting from "./components/Accounting";
import Dashboard from "./components/Dashboard";
import Report from "./components/Report";
import Statements from "./components/Statements";
import SideBar from "./components/SideBar";
import NavBar from "./components/NavBar";

function App() {
  return (
    <div className="app">
      <nav className="navBarContainer">
        <NavBar />
      </nav>

      <div className="content">
        <SideBar />

        <main className="main">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/accounting" element={<Accounting />} />
            <Route path="/reports" element={<Report />} />
            <Route path="/statements" element={<Statements />} />
          </Routes>
        </main>
      </div>
    </div>
  );
}

export default App;
