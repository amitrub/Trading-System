import React, { useState } from "react";
import createApiClient from "../../ApiClient";
import "../../Design/grid.css";
import "../../Design/style.css";

const api = createApiClient();

function Register(props) {
  // const [regConnID, setConnIDState] = useState("");
  const [enteredName, setNameState] = useState("");
  const [enteredPass, setPassState] = useState("");

  async function submitHandler(event) {
    event.preventDefault();
    // setConnIDState(props.connID);

    await api.register(
      props.clientConnection,
      props.connID,
      enteredName,
      enteredPass
    );

    props.onSubmitRegister(enteredName, enteredPass);
    setNameState("");
    setPassState("");
  }

  function nameChangeHandler(event) {
    setNameState(event.target.value);
  }

  function passChangeHandler(event) {
    setPassState(event.target.value);
  }

  return (
    <section className="section-form" id="sign">
      <div className="row">
        <h2>Join Us!</h2>
      </div>
      <div className="row">
        <form
          method="post"
          action="#"
          className="contact-form"
          onSubmit={submitHandler}
        >
          <div className="row">
            <div className="col span-1-of-3">
              <label htmlFor="name">User Name</label>
            </div>
            <div className="col span-2-of-3">
              <input
                type="text"
                name="name"
                id="name"
                placeholder="Enter your username"
                required
                onChange={nameChangeHandler}
                value={enteredName}
              />
            </div>
          </div>
          <div className="row">
            <div className="col span-1-of-3">
              <label htmlFor="password">Password</label>
            </div>
            <div className="col span-2-of-3">
              <input
                type="text"
                name="pass"
                id="pass"
                placeholder="Enter your password"
                required
                onChange={passChangeHandler}
                value={enteredPass}
              />
            </div>
            <div className="col span-1-of-3">
              <label>&nbsp;</label>
            </div>
            <div className="col span-1-of-3">
              <input type="submit" value="Register" />
            </div>
          </div>
        </form>
      </div>
    </section>
  );
}

export default Register;
