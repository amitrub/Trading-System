import React, { Component } from "react";
import { Button } from "./Button";
import { MenuItems } from "./MenuItems";
import "./Navbar.css";

class Navbar extends Component {
  constructor() {
    super();
    this.state = {
      clicked: false,
      scrolled: false,
    };
  }
  handleClick = () => {
    this.setState({ clicked: !this.state.clicked });
  };
  handleScroll = () => {
    const offset = window.scrollY;
    this.setState({ scrolled: offset > 200 });
  };

  componentDidMount() {
    window.addEventListener("scroll", this.handleScroll);
  }

  render() {
    let navbarClasses = ["navbar"];
    if (this.state.scrolled) {
      navbarClasses.push("scrolled");
    }
    return (
      <nav className="NavbarItems">
        <myh1 className="navbar-logo">
          React<i className="fab fa-react"></i>
        </myh1>
        <div className="menu-icon" onClick={this.handleClick}>
          <myi
            className={this.state.clicked ? "fas fa-times" : "fas fa-bars"}
          ></myi>
        </div>
        <ul className={this.state.clicked ? "nav-menu active" : "nav-menu"}>
          {MenuItems.map((item, index) => {
            return (
              <li key={index}>
                <mya className={item.cName} href={item.url}>
                  {item.title}
                </mya>
              </li>
            );
          })}
        </ul>
        <Button>Sign Up</Button>
      </nav>
    );
  }
}

export default Navbar;
