import "./sideBar.css";
function NavBar() {
  return (
    <nav className="sideBarContainer">
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
