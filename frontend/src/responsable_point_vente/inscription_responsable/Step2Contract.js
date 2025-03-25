import React from "react";
import Stepper from "../../components/Stepper";
import "./InscriptionSteps.css";



const Step2Contract = ({ formData, nextStep, prevStep }) => {
  const contractName = `contrat_${formData.nom}_${formData.prenom}.pdf`.replace(/\s+/g, "_");
  console.log("📩 Email courant :", formData.email);

  const pdfUrl = `http://localhost:9090/api/contract/preview?contractName=${contractName}`;

  return (
    <div className="step-form-container">
        <div className="p-4">
      <Stepper step={2} />

      <h2 className="text-xl font-bold mb-4">📄 Contrat Généré</h2>

      <iframe
  src={`http://localhost:9090/api/contract/preview?contractName=${contractName}`}
  width="100%"
  height="600px"
  title="Contrat PDF"
/>

      <a
  href={pdfUrl}
  target="_blank"
  rel="noopener noreferrer"
  className="text-blue-600 underline"
>
  📄 Ouvrir le contrat dans un nouvel onglet
</a>


      <div className="mt-4 flex justify-end">
      <button className="button-secondary" onClick={prevStep}>⬅️ Retour</button>
        <button
          onClick={nextStep}
          className="button-primary"
        >
          Suivant
        </button>
        
      </div>
    </div>






    </div>
  
  );
};

export default Step2Contract;
