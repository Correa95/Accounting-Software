import "./NavBar.css";
function NavBar() {
  return (
    <nav className="navbarContainer">
      <ul>
        <li>Dashboard</li>
        <li>Reports</li>
        <li>Accounting</li>
        <li>Vendors</li>
      </ul>
      <ol>
        <li>Help</li>
        <li>Setting</li>
        <li>Log Out</li>
      </ol>
    </nav>
  );
}

export default NavBar;
