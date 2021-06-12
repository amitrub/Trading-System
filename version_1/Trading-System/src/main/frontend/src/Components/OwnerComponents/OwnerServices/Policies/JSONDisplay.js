import React from "react";

function JSONDisplay(props) {
  return (
    <pre
      style={{
        backgroundColor: "#eee",
        padding: "1em",
        textAlign: "left",
      }}
    >
      <code>{JSON.stringify(props.value, null, 2)}</code>
    </pre>
  );
}

export default JSONDisplay;
