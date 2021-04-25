import React, {useState, useEffect} from "react";
import './App.css';
import axios from "axios";
import createApiClient from "../src/ApiClient"

const api = createApiClient();

const TestComponent = () => {

    const [testState, setTestState] = useState([]);

    const fetchTest = () => {
      // const hello_msg = api.getTest();
      // setTestState(hello_msg);
      axios.get("http://localhost:8080/api/test").then(res => {
          console.log(res);
          const data = res.data;
          setTestState(data);
        });
    }

    useEffect(() => {
      fetchTest();
    }, []);

    return <div>
      <h1> Test Component </h1>
      <p> information from api/test/: {testState} </p>
    </div>
}



function App() {
  return (
    <div className="App">
        ~ Trading System ~
        <TestComponent/>
    </div>
  );
}

export default App;
