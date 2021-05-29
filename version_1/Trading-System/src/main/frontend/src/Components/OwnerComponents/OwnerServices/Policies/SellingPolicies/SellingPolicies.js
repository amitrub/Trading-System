import React, { useState, useEffect } from "react";
import PolicyRoot from "../PolicyRoot";
import AndRootPolicy from "./AndRootPolicy";
import OrRootPolicy from "./OrRootPolicy";
import ConditionsPolicy from "./CondtionsRootPolicy";
import XorRootComposite from "./XorRootComposite";

function Policies(props) {
  const store = props.currStore;

  const [showAnd, setShowAnd] = useState(false);
  const [showConditions, setShowConditions] = useState(false);
  const [showOr, setShowOr] = useState(false);
  const [showSimple, setShowSimple] = useState(false);
  const [showXor, setShowXor] = useState(false);

  function showAndHandler() {
    setShowAnd(true);
    setShowConditions(false);
    setShowOr(false);
    setShowSimple(false);

    props.onRefresh();
  }
  function hideAndHandler() {
    setShowAnd(false);
    props.onRefresh();
  }
  function showConditionHandler() {
    setShowAnd(false);
    setShowConditions(true);
    setShowOr(false);
    setShowSimple(false);

    props.onRefresh();
  }
  function hideConditionHandler() {
    setShowConditions(false);
    props.onRefresh();
  }

  function showOrHandler() {
    setShowAnd(false);
    setShowConditions(false);
    setShowOr(true);
    setShowSimple(false);

    props.onRefresh();
  }
  function hideOrHandler() {
    setShowOr(false);
    props.onRefresh();
  }

  function showXorHandler() {
    setShowAnd(false);
    setShowConditions(false);
    setShowOr(false);
    setShowSimple(false);
    setShowXor(true);

    props.onRefresh();
  }
  function hideXorHandler() {
    setShowXor(false);
    props.onRefresh();
  }

  function showSimpleHandler() {
    setShowAnd(false);
    setShowConditions(false);
    setShowOr(false);
    setShowSimple(true);

    props.onRefresh();
  }
  function hideSimpleHandler() {
    setShowSimple(false);
    props.onRefresh();
  }

  //   --------------------------------------------------------

  return (
    <section className="section-plans js--section-plans" id="store">
      <div className="row">
        {/* Show and Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={showAnd ? hideAndHandler : showAndHandler}
        >
          {showAnd ? "Hide" : "And Policy"}
        </button>
        {/* show condition Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={showConditions ? hideConditionHandler : showConditionHandler}
        >
          {showConditions ? "Hide" : "Conditions Policy"}
        </button>
        {/* show or Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={showOr ? hideOrHandler : showOrHandler}
        >
          {showOr ? "Hide" : "Or Policy"}
        </button>
        {/* show simple Btn */}
        {/* <button
          className="buttonus"
          value="load our stores..."
          onClick={showSimple ? hideSimpleHandler : showSimpleHandler}
        >
          {showSimple ? "Hide" : "Simple Handler"}
        </button> */}
        {/* show xor Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={showSimple ? hideXorHandler : showXorHandler}
        >
          {showXor ? "Hide" : "Xor Handler"}
        </button>
      </div>

      {/* And Policy */}
      <div className="row">
        {showAnd ? (
          <AndRootPolicy
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
          />
        ) : (
          ""
        )}
      </div>

      {/* Condtions Policy */}
      <div className="row">
        {showConditions ? (
          <ConditionsPolicy
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
          />
        ) : (
          ""
        )}
      </div>

      {/* Or Policy */}
      <div className="row">
        {showOr ? (
          <OrRootPolicy
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
          />
        ) : (
          ""
        )}
      </div>

      {/* Or Policy */}
      <div className="row">
        {showXor ? (
          <XorRootComposite
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
          />
        ) : (
          ""
        )}
      </div>
    </section>
  );
}

export default Policies;
