import React, { useState } from "react";
import axios from "axios";
import Stepper from "../../components/Stepper";
import "./InscriptionSteps.css";

const Step3Signature = ({ formData, signerName, setSignatureFile, nextStep, prevStep }) => {
  const [signatureFile, setLocalSignatureFile] = useState(null);
  const [message, setMessage] = useState("");

  const handleFileChange = (e) => {
    setLocalSignatureFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!signatureFile) {
      setMessage("⚠️ Veuillez sélectionner un fichier !");
      return;
    }

    const formDataUpload = new FormData();
    formDataUpload.append("file", signatureFile);
    // formDataUpload.append("signerName", `${formData?.nom}_${formData?.prenom}`);
    formDataUpload.append("signerName", signerName);


    try {
      const res = await axios.post("http://localhost:9090/api/contract/uploadSignature", formDataUpload, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      setMessage("✅ Signature uploadée avec succès !");
      setSignatureFile(signatureFile); // Met à jour la signature dans le parent
      console.log(res.data);
      // ➕ Appeler la génération du contrat signé
    // await axios.post("http://localhost:9090/api/contract/sign", {
    //     contractName: `contrat_${formData.nom}_${formData.prenom}.pdf`.replace(/\s+/g, "_"),
    //     signerName: `${formData.nom}_${formData.prenom}`.replace(/\s+/g, "_")
    //   });
    //   console.log("✅ Contrat signé généré !");
    } catch (err) {
      console.error("Erreur upload:", err);
      setMessage("❌ Erreur lors de l'upload.");
    }
  };
  const handleNext = async () => {
    try {
      // SignerName et ContractName
      const contractName = `contrat_${formData.nom}_${formData.prenom}.pdf`;
      const signerName = `${formData.nom}_${formData.prenom}`;
  
      const data = new URLSearchParams();
      data.append("contractName", contractName);
      data.append("signerName", signerName);
  
      await axios.post("http://localhost:9090/api/contract/sign", data, {
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      });
  
      console.log("✅ Contrat signé !");
      nextStep(); // ➡️ Passer à l’étape suivante
    } catch (error) {
      console.error("❌ Erreur génération contrat signé :", error);
      setMessage("❌ Erreur lors de la génération du contrat signé.");
    }
  };
  
  

  return (
    <div className="step-form-container">
      <div className="p-4">
      <Stepper step={3} />
      <h2 className="text-xl font-bold mb-4">🖋️ Ajouter votre Signature</h2>

      <input type="file" accept="image/*" onChange={handleFileChange} />
      <button onClick={handleUpload} className="button-primary">
        Upload
      </button>

      {message && <p className="mt-4 text-sm">{message}</p>}

      <div className="mt-6 flex justify-end">
      <button className="button-secondary" onClick={prevStep}>⬅️ Retour</button>
      <button
  onClick={handleNext}
 className="button-primary"
>
  Suivant
</button>

      </div>
    </div>
      
      
      
      
      
      
      
     </div>
    
  );
};

export default Step3Signature;
