import "./BankCard.css";
function BankCard() {
  return (
    <div className="bankCardContainer">
      <div className="top">
        <h1 className="cardHeader">CARDS</h1>
        <button className="btnConnect">Connect Card</button>
      </div>
      <div className="bank">
        <h2>Name</h2>
        <amount>0.00</amount>
      </div>
      <div className="bank">
        <h2>Name</h2>
        <amount>0.00</amount>
      </div>
    </div>
  );
}

export default BankCard;
