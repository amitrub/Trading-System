import React, { useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function Add_RemoveEmployee(props) {
  const [employeeID, setEmployeeID] = useState(-1);
  const [popupRemove, setPopupRemove] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  // const workersResponse = await apiHttp.GetAllSubscribers(props.connID);
  const workers = [
    { id: 1, name: "roee" },
    { id: 2, name: "hadas" },
    { id: 3, name: "chango" },
  ];

  async function submitRemoveHandler(event) {
    event.preventDefault();
    // console.log("before submitRemoveHandler");

    const removeResponse = await props.action(
      props.connID,
      props.userID,
      props.storeID,
      employeeID
    );

    if (removeResponse) {
      setPopupMsg(removeResponse.message);
      setPopupRemove(true);
    }
    if (removeResponse.isErr) {
      console.log(removeResponse.message);
    }
    props.onRefresh();
  }

  function updateEmployeeID(event) {
    setEmployeeID(event.target.value);
  }

  function onClosePopup() {
    setPopupRemove(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <div className="row">
          <h2>{props.job}</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitRemoveHandler}
          >
            {/* product id */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Employee ID</label>
              </div>
              <div className="col span-2-of-3">
                <select
                  className="select"
                  value={employeeID}
                  onChange={(e) => updateEmployeeID(e)}
                  about="Show number of results:"
                >
                  {/* TOOD: change map to workersResponse.workers */}
                  {workers.map((currWorker) => (
                    <option value={currWorker.id}>{currWorker.name}</option>
                  ))}
                </select>
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              {props.label === "addOwner" ? (
                <div className="col span-1-of-3">
                  <input type="submit" value="Add Owner!" />
                </div>
              ) : props.label === "removeOwner" ? (
                <div className="col span-1-of-3">
                  <input type="submit" value="Remove Owner!" />
                </div>
              ) : props.label === "addManager" ? (
                <div className="col span-1-of-3">
                  <input type="submit" value="Add Manager!" />
                </div>
              ) : (
                <div className="col span-1-of-3">
                  <input type="submit" value="Remove Manager!" />
                </div>
              )}
            </div>
          </form>
        </div>
      </div>
      {popupRemove ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default Add_RemoveEmployee;
