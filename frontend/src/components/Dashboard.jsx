import BankCard from "./component/BankCard";
import Transaction from "./component/Transaction";
import Charts from "./component/Charts.";
import Mathew from "./component/Mathew";

function Dashboard() {
  return (
    <div className="dashboard">
      <h1>DASHBOARD</h1>
      <div className="cards">
        <BankCard />
        <Transaction />
        <Charts />
        <Mathew />
      </div>
    </div>
  );
}

export default Dashboard;
