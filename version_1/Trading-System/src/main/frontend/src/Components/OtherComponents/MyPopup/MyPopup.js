import React from "react";
import "./MyPopup.css";
import Modal from "../Modal/Modal";

const MyPopup = (props) => {
  function onClosePopup() {
    props.onClosePopup();
  }

  return (
    <Modal>
      <div className="alert">
        <div>
          <p>{props.errMsg}</p>
          <button className="buttunus" onClick={onClosePopup}>
            OK
          </button>
        </div>
      </div>
    </Modal>
  );
};
export default MyPopup;
