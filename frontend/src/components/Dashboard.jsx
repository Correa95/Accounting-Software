import BankCard from "./BankCard";
import Transaction from "./Transaction";
import Charts from "./Charts.";
import Mathew from "./Mathew";

function Dashboard() {
  return (
    <div className="dashboard">
      <h1>DASHBOARD</h1>
      <div className="cards">
        <div className="card"></div>
        <div className="card"></div>
        <div className="card"></div>
        <div className="card">
          <Mathew />
        </div>
        <BankCard />
        <Transaction />
        <Charts />
      </div>
    </div>
  );
}

export default Dashboard;
