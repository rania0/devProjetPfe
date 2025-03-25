// Ce composant gère tout le processus d’inscription multi-étapes
// pour un responsable point de vente.
// Il affiche dynamiquement chaque étape :
// 1. Formulaire d'informations
// 2. Aperçu du contrat
// 3. Upload de la signature
// 4. Aperçu final du contrat signé

import React, { useState } from "react";
import Step1Form from "./Step1Form";
import Step2Contract from "./Step2Contract";
import Step3Signature from "./Step3Signature";
import Step4Preview from "./Step4Preview";

export default function Inscription() {
  // ⬅️ Stocker l’étape actuelle du formulaire
  const [step, setStep] = useState(1);

  // ⬅️ Données saisies dans le formulaire
  const [formData, setFormData] = useState({
    nom: "",
    prenom: "",
    email: "",
    motDePasse: "",
    telephone: "",
    cin: "",
    adresse: "",
    typeActivite: "",
    pointDeVenteId: "" 
  });
  

  // ⬅️ Signature (image)
  const [signatureFile, setSignatureFile] = useState(null);

  // ⬅️ Nom du contrat généré (retourné par le backend)
  const [contractName, setContractName] = useState("");

  // 👉 Passer à l’étape suivante
  const nextStep = () => setStep((prev) => prev + 1);

  // 👉 Revenir à l’étape précédente
  const prevStep = () => setStep((prev) => prev - 1);

  // 🔁 Afficher le bon composant selon l’étape
  switch (step) {
    case 1:
      return (
        <Step1Form
          formData={formData}
          setFormData={setFormData}
          nextStep={nextStep}
        />
      );
    case 2:
      return (
        <Step2Contract
          formData={formData}
          nextStep={nextStep}
          prevStep={prevStep}
          setContractName={setContractName}
        />
      );
      case 3:
        return (
          <Step3Signature
      formData={formData} // ✅ ADD THIS LINE!
      signerName={`${formData.nom}_${formData.prenom}`}
      setSignatureFile={setSignatureFile}
      nextStep={nextStep}
      prevStep={prevStep}
    />
        );
      
    case 4:
      return (
        <Step4Preview
          contractName={contractName}
          signerName={`${formData.nom}_${formData.prenom}`}
          formData={formData}
          signatureFile={signatureFile}
          prevStep={prevStep}
        />
      );
    default:
      return <div>Étape invalide</div>;
  }
}
