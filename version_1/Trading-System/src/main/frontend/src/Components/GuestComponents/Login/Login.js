import React, { useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function Login(props) {
  const [enteredName, setNameState] = useState("");
  const [enteredPass, setPassState] = useState("");
  const [popUpLogin, setPopUpLogin] = useState(false);
  const [popupMsg, setPopMsg] = useState("");
  const [response, setResponse] = useState("");

  function onClosePopupLogin() {
    setPopUpLogin(false);
    props.onSubmitLogin(enteredName, enteredPass, response);
    setNameState("");
    setPassState("");
    props.onRefresh();
  }

  async function submitHandler(event) {
    event.preventDefault();

    const loginResponse = await apiHttp.Login(
      props.connID,
      enteredName,
      enteredPass
    );
    // console.log(res);

    if (loginResponse) {
      setPopMsg(loginResponse.message);
      setPopUpLogin(true);
    }
    if (loginResponse.isErr) {
      console.log(loginResponse.message);
    }

    setResponse(loginResponse);

    // props.onSubmitLogin(enteredName, enteredPass, loginResponse);
    // setNameState("");
    // setPassState("");
  }

  function nameChangeHandler(event) {
    setNameState(event.target.value);
  }

  function passChangeHandler(event) {
    setPassState(event.target.value);
  }

  return (
    <section className="section-form" id="login">
      <div className="row">
        <h2>not your first time?</h2>
      </div>
      <div className="row">
        <form method="post" className="contact-form" onSubmit={submitHandler}>
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
              <input type="submit" value="Login" />
            </div>
          </div>
        </form>
      </div>
      {popUpLogin ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopupLogin}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default Login;
