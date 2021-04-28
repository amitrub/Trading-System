import React, {useState} from "react";
import './App.css';
import TestComponent from './Components/TestComponent/TestComponent'
import Register from './Components/Register/Register'
import User from './Components/User/User'


function App() {

  const [currUserConnID, setCurrUserConnID] = useState('');
  // const [error, setErrorState] = useState('');

  function submitRegisterHandler(regData) {
    console.log(regData);
  }

  function updateConnIDHandler(connID) {
    setCurrUserConnID(connID)
  }

  return (
    <div className="App">
        <h1>~ Trading System ~</h1>
        <TestComponent/>
        <User onUpdateConnID = {updateConnIDHandler}/>
        <Register onSubmitRegister = {submitRegisterHandler} connID = {currUserConnID}/>
    </div>
  );
}

export default App;
