import BankCard from "./components/BankCard";
import Transaction from "./components/Transaction";
import Charts from "./components/Charts.";
import Mathew from "./components/Mathew";
import "./Dashboard.css";

function Dashboard() {
  return (
    <div className="dashboard">
      <h1>DASHBOARD</h1>
      <div className="cards">
        <div className="card">
          <BankCard />
        </div>

        <div className="card">
          <Transaction />
        </div>

        <div className="card">
          <Charts />
        </div>

        <div className="card">
          <Mathew />
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
