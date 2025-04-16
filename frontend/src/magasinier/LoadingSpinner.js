import React from "react";
import "./LoadingSpinner.css"; // le CSS séparé pour l’animation

const LoadingSpinner = () => {
  return (
    <div className="loader-wrapper">
      <div className="loader"></div>
      <p className="loader-text">Recherche des fournisseurs en cours...</p>
    </div>
  );
};

export default LoadingSpinner;
