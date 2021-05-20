import React, { useState, useEffect } from "react";
import "./PopUp.css";

function PopUp(props) {
  return (
    <div className="popup">
      <div className="overlay">
        <h1> Test Component </h1>
        <p> information from api/test/:</p>
      </div>
    </div>
  );
}

export default PopUp;
