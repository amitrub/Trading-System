import React, { useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function Logout(props) {
  const [logouted, setLogouted] = useState(false);
  const [popupMsg, setPopUpMsg] = useState("");
  const [response, setResponse] = useState("");

  async function submitHandler(event) {
    event.preventDefault();

    const logoutResponse = await apiHttp.Logout(props.connID, props.userID);
    // console.log(logoutResponse);
    if (logoutResponse) {
      setPopUpMsg(logoutResponse.message);
      setLogouted(true);
    }
    if (logoutResponse.isErr) {
      console.log(logoutResponse.message);
    }
    setResponse(logoutResponse);
  }

  function onClosePopupLogout() {
    setLogouted(false);
    props.onLogout(response);
    props.onRefresh();
  }

  return (
    <section className="section-form" id="sign">
      <div className="row">
        <h2>Done? Logout..</h2>
      </div>
      <div className="row">
        <form
          method="post"
          // action="#"
          className="contact-form"
          onSubmit={submitHandler}
        >
          <div className="col span-1-of-3">
            <label>&nbsp;</label>
          </div>
          <div className="col span-1-of-3">
            <input type="submit" value="Bye Bye" />
          </div>
        </form>
      </div>
      {logouted ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopupLogout}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default Logout;
