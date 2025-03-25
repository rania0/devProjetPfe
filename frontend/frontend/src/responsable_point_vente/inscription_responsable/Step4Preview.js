// import React from "react";
// import axios from "axios";

// const Step4Preview = ({ contractName, signerName, formData, signatureFile, prevStep }) => {
//   const signedPdfName = `signed_contrat_${signerName}.pdf`;
//   const pdfUrl = `http://localhost:9090/signed_contracts/${signedPdfName}`;
//   const signedPdfUrl = `http://localhost:9090/api/contract/signed/preview?contractName=${signedPdfName}`;


//   // 🔄 Envoi final vers le backend pour sauvegarde complète
//   const handleConfirm = async () => {
//     const confirmData = {
//       ...formData,
//       motDePasse: formData.motDePasse,
//       pointDeVenteId: formData.pointDeVenteId,
//     };
  
//     try {
//       const res = await axios.post("http://localhost:9090/api/responsable/confirm", confirmData);
//       alert("✅ Votre demande a été enregistrée avec succès !\nVous recevrez une réponse par mail entre 2 jours et 1 semaine contenant l'adresse et le mot de passe pour accéder à l'application.");
//       console.log("✅ Backend response:", res.data);
//     } catch (error) {
//       console.error("❌ Erreur lors de la confirmation :", error);
//       alert("❌ Une erreur s’est produite lors de l'enregistrement de votre demande.");
//     }
//   };
  

//   return (
//     <div className="p-4">
//       <h2 className="text-xl font-bold mb-4">📄 Contrat Signé</h2>

//       <iframe
//   src={signedPdfUrl}
//   width="100%"
//   height="600px"
//   title="Contrat signé"
//   className="border"
// />

//       <div className="mt-4">
//         <a href={pdfUrl} target="_blank" rel="noopener noreferrer" className="text-blue-600 underline">
//           📄 Ouvrir dans un nouvel onglet
//         </a>
//       </div>

//       <div className="mt-6 flex justify-between">
//         <button
//           onClick={prevStep}
//           className="px-4 py-2 bg-gray-400 text-white rounded"
//         >
//           ⬅️ Retour
//         </button>

//         <button
//           onClick={handleConfirm}
//           className="px-4 py-2 bg-green-600 text-white rounded"
//         >
//           ✅ Confirmer la demande
//         </button>
//       </div>
//     </div>
//   );
// };

// export default Step4Preview;





// Step4Preview.js
import React from "react";
import axios from "axios";
import Stepper from "../../components/Stepper";
import "./InscriptionSteps.css";

const Step4Preview = ({ contractName, signerName, formData, signatureFile, prevStep }) => {
  // 📝 Nom du contrat signé
  const signedPdfName = `signed_contrat_${signerName}.pdf`;

  // 📄 Lien pour affichage dans iframe
  const signedPdfUrl = `http://localhost:9090/api/contract/signed/preview?contractName=${signedPdfName}`;

  // 📄 Lien pour ouvrir dans un nouvel onglet
  const downloadUrl = `http://localhost:9090/signed_contracts/${signedPdfName}`;

  // ✅ Envoi final de la demande
  const handleConfirm = async () => {
    const confirmData = {
      ...formData,
      motDePasse: formData.motDePasse,
      rawPassword: formData.motDePasse,
      pointDeVenteId: formData.pointDeVenteId,
    };

    try {
      const res = await axios.post("http://localhost:9090/api/responsable/confirm", confirmData);
      alert(
        "✅ Votre demande a été enregistrée avec succès !\n\n🕐 Vous recevrez une réponse par mail entre 2 jours et 1 semaine contenant :\n- 🏢 L'adresse de connexion\n- 🔑 Votre mot de passe"
      );
      console.log("✅ Confirmation réussie :", res.data);
    } catch (error) {
      console.error("❌ Erreur lors de la confirmation :", error);
      alert("❌ Une erreur s’est produite lors de l'envoi de votre demande.");
    }
  };

  return (
    <div className="step-form-container">
      <div className="p-4">
      <Stepper step={4} />
      <h2 className="text-xl font-bold mb-4">📄 Contrat Signé</h2>

      <iframe
        src={signedPdfUrl}
        width="100%"
        height="600px"
        title="Contrat signé"
        className="border"
      />

      <div className="mt-4">
        <a
          href={downloadUrl}
          target="_blank"
          rel="noopener noreferrer"
          className="text-blue-600 underline"
        >
          📄 Ouvrir dans un nouvel onglet
        </a>
      </div>

      <div className="mt-6 flex justify-between">
        <button
          onClick={prevStep}
          className="px-4 py-2 bg-gray-500 text-white rounded"
        >
          ⬅️ Retour
        </button>

        <button
          onClick={handleConfirm}
          className="px-4 py-2 bg-green-600 text-white rounded"
        >
          ✅ Confirmer la demande
        </button>
      </div>
    </div>
    </div>
    
  );
};

export default Step4Preview;

