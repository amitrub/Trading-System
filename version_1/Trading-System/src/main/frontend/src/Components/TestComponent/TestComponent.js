import React, {useState, useEffect} from "react";
import createApiClient from "../../../src/ApiClient"

const api = createApiClient();

function TestComponent() {

    const [testState, setTestState] = useState('');

    const fetchTest = async () => {
      const hello_msg = await api.getTest();
      setTestState(hello_msg);
    }

    useEffect(() => {
      fetchTest();
    }, []);

    return <div>
      <h1> Test Component </h1>
      <p> information from api/test/: {testState} </p>
    </div>
}

export default TestComponent;