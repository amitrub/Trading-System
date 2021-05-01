import React, { useState, useEffect } from "react";
import createApiClient from "../../../src/ApiClient";
import "./User.css";

const api = createApiClient();

function User() {
  const [connID, setConnIDState] = useState("");
  const [error, setErrorState] = useState("");

  const fetchConnectionSystem = async () => {
    const response = await api.getConnectSystem();
    if (response.isErr) {
      const errMsg = response.message;
      console.log(errMsg);
      setErrorState(errMsg);
    } else {
      setConnIDState(response.returnObject.connID);
    }
  };

  useEffect(() => {
    fetchConnectionSystem();
  }, []);

  return (
    <div>
      <p> User Component</p>
      {error !== "" ? (
        <p>ERROR!!! : {error}</p>
      ) : (
        <p> GUEST CONN ID : {connID}</p>
      )}
    </div>
  );
}

export default User;
