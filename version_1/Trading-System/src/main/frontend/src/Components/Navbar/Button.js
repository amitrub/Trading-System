import React, { Component } from "react";
import "./Button.css";

const STYLES = ["btnus--primary1", "btnus--outline1"];
const SIZES = ["btnus--primary1", "btnus--outline1"];

export const Button = ({
  children,
  type,
  onClick,
  buttonStyle,
  buttonSize,
}) => {
  const checkButtonStyle = STYLES.includes(buttonStyle)
    ? buttonStyle
    : STYLES[0];
  const checkButtonSize = SIZES.includes(buttonSize) ? buttonSize : SIZES[0];
  return (
    <button
      className={`btnus ${checkButtonStyle} ${checkButtonSize}`}
      onClick={onClick}
    >
      {children}
    </button>
  );
};
