import React, { useState } from "react";
import createApiClient from "../../../src/ApiClient";
import "./Register.css";

const api = createApiClient();

function Register(props) {
  const [regConnID, setConnIDState] = useState("");
  const [enteredName, setNameState] = useState("");
  const [enteredPass, setPassState] = useState("");

  async function submitHandler(event) {
    event.preventDefault();

    const registerData = {
      name: enteredName,
      pass: enteredPass,
    };
    setConnIDState(props.connID);

    props.onSubmitRegister(registerData);
    setNameState("");
    setPassState("");

    // const res = await api.register(props.clientConnection);
    await api.getTest(props.clientConnection, props.connID);

    // console.log("after register post: \n");
  }

  function nameChangeHandler(event) {
    setNameState(event.target.value);
  }

  function passChangeHandler(event) {
    setPassState(event.target.value);
  }

  return (
    <div>
      <p> Register Component</p>
      <form onSubmit={submitHandler}>
        <div className="new-expense__controls">
          <div className="new-expense__control">
            <label>Name</label>
            <input
              type="text"
              value={enteredName}
              onChange={nameChangeHandler}
            />
          </div>
          <div className="new-expense__control">
            <label>Password</label>
            <input
              type="text"
              value={enteredPass}
              onChange={passChangeHandler}
            />
          </div>
        </div>
        <div className="new-expense__actions">
          <button type="submit">Register</button>
        </div>
      </form>
    </div>
  );
}

export default Register;
