import React from "react";
import "./MyPopup.css";

const MyPopup = (props) => {
  function onClosePopup() {
    props.onClosePopup();
  }

  return (
    <div className="alert">
      <div>
        <p>{props.errMsg}</p>
        <button className="buttunus" onClick={onClosePopup}>
          OK
        </button>
      </div>
    </div>
  );
};
export default MyPopup;
