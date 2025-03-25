import React from "react";
import "./Stepper.css";

const Stepper = ({ step }) => {
  return (
    <div className="stepper-container">
      {[1, 2, 3, 4].map((num) => (
        <div key={num} className={`step-item ${step === num ? "active" : step > num ? "completed" : ""}`}>
          <div className="step-circle">
            {num}
          </div>
        </div>
      ))}
    </div>
  );
};

export default Stepper;