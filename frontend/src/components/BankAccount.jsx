import "./BankCard.css";
function BankAccount() {
  return (
    <div className="bankAccountContainer">
      <div className="top">
        <h1 className="accountName">Bank Account</h1>
        <button className="btnConnect">Connect Account</button>
      </div>
      <div className="accountContainer">
        <h2 className="accountName">Name</h2>
        <amount className="accountBalance">0.00</amount>
      </div>
      <div className="accountContainer">
        <h2 className="accountName">Name</h2>
        <amount className="accountBalance">0.00</amount>
      </div>
    </div>
  );
}

export default BankAccount;
