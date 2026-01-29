import { IoNotificationsOutline } from "react-icons/io5";
import { FaRegMessage } from "react-icons/fa6";
import "./NavBar.css";
function NavBar() {
  return (
    <div className="navBarContainer">
      <div className="logo">
        <h1>Tech</h1>
      </div>
      <div className="notifications">
        <button className="btnNot">
          <IoNotificationsOutline size={30} />
        </button>
        <button className="btnMessage">
          <FaRegMessage size={30} />
        </button>
      </div>
    </div>
  );
}

export default NavBar;
