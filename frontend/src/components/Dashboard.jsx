import BankCard from "./BankCard";
import Transaction from "./Transactions";
import Charts from "./Charts";
import Mathew from "./Mathew";
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
