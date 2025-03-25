import React from "react";
import "./progressBar.css"; // Tu peux le customiser plus tard

const ProgressBar = ({ currentStep }) => {
  const steps = [1, 2, 3, 4];

  return (
    <div className="step-indicator">
      {steps.map((step, index) => (
        <div key={index} className="step-wrapper">
          <div className={`step-circle ${currentStep === step ? "active" : currentStep > step ? "completed" : ""}`}>
            {step}
          </div>
          {index !== steps.length - 1 && (
            <div className={`step-line ${currentStep > step ? "filled" : ""}`}></div>
          )}
        </div>
      ))}
    </div>
  );
};

export default ProgressBar;
