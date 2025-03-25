// import React, { useState, useEffect } from "react";
// import axios from "axios";

// const Step1Form = ({ formData, setFormData, nextStep }) => {
//   const [pointsDeVente, setPointsDeVente] = useState([]);

//   // 🔄 Charger la liste des points de vente depuis le backend
//   useEffect(() => {
//     axios
//       .get("http://localhost:9090/api/point_vente/all")
//       .then((res) => setPointsDeVente(res.data))
//       .catch((err) => console.error("Erreur chargement points de vente", err));
//   }, []);

//   // ✅ Fonction de mise à jour des champs du formulaire
//   const handleChange = (e) => {
//     const { name, value } = e.target;
//     const parsedValue = name === "pointDeVenteId"
//       ? (value !== "" ? parseInt(value) : "") // 🧼 parse int if not empty
//       : value;
//     setFormData({ ...formData, [name]: parsedValue });
//   };
  

//   // ✅ Validation simple avant de passer à l'étape suivante
//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//       const response = await axios.post("http://localhost:9090/api/contract/generate", formData);
//       console.log("✅ Contrat généré :", response.data);
//       nextStep(); // ➕ yji houni ba3d ma yetsenna el backend
//       if (!formData.pointDeVenteId) {
//         alert("Veuillez sélectionner un point de vente.");
//         return;
//       }
//     } catch (error) {
//       console.error("❌ Erreur lors de la génération du contrat", error);
//     }
//   };

//   return (
//     <form onSubmit={handleSubmit} className="p-4 space-y-4">
//       <h2 className="text-xl font-bold">📝 Formulaire de Demande</h2>

//       <input
//         type="text"
//         name="nom"
//         placeholder="Nom"
//         value={formData.nom}
//         onChange={handleChange}
//         className="w-full p-2 border rounded"
//         required
//       />
//       <input
//         type="text"
//         name="prenom"
//         placeholder="Prénom"
//         value={formData.prenom}
//         onChange={handleChange}
//         className="w-full p-2 border rounded"
//         required
//       />
//       <input
//         type="email"
//         name="email"
//         placeholder="Email"
//         value={formData.email}
//         onChange={handleChange}
//         className="w-full p-2 border rounded"
//         required
//       />
//       <input
//         type="password"
//         name="motDePasse"
//         placeholder="Mot de passe"
//         value={formData.motDePasse}
//         onChange={handleChange}
//         className="w-full p-2 border rounded"
//         required
//       />
//       <input
//         type="text"
//         name="telephone"
//         placeholder="Téléphone"
//         value={formData.telephone}
//         onChange={handleChange}
//         className="w-full p-2 border rounded"
//         required
//       />
//       <input
//         type="text"
//         name="cin"
//         placeholder="CIN"
//         value={formData.cin}
//         onChange={handleChange}
//         className="w-full p-2 border rounded"
//         required
//       />
//       <input
//         type="text"
//         name="adresse"
//         placeholder="Adresse"
//         value={formData.adresse}
//         onChange={handleChange}
//         className="w-full p-2 border rounded"
//         required
//       />
//       <input
//         type="text"
//         name="typeActivite"
//         placeholder="Type d'activité"
//         value={formData.typeActivite}
//         onChange={handleChange}
//         className="w-full p-2 border rounded"
//         required
//       />

//       {/* 🔽 Liste déroulante pour le point de vente */}
//       {/* <select name="pointDeVenteId" value={formData.pointDeVenteId} onChange={handleChange}>
//   <option value="">-- Sélectionner un point de vente --</option>
//   {pointsDeVente.map((point) => (
//     <option key={point.id} value={point.id}>
//       {point.nomP}
//     </option>
//   ))}
// </select> */}
// <select
//   name="pointDeVenteId"
//   value={formData.pointDeVenteId}
//   onChange={handleChange}
// >
//   <option value="">-- Sélectionner un point de vente --</option>
//   {pointsDeVente.map((point) => (
//     <option key={point.idP} value={point.idP}>
//       {point.nomP}
//     </option>
//   ))}
// </select>

//       <div className="flex justify-end">
//         <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded">
//           Suivant
//         </button>
//       </div>
//     </form>
//   );
// };

// export default Step1Form;


import React, { useState, useEffect } from "react";
import axios from "axios";
import Stepper from "../../components/Stepper";
import "./InscriptionSteps.css";
import Swal from "sweetalert2";



const Step1Form = ({ formData, setFormData, nextStep, prevSte }) => {
  const [pointsDeVente, setPointsDeVente] = useState([]);
  const [errors, setErrors] = useState({});
  const [backendError, setBackendError] = useState("");
  useEffect(() => {
    Swal.fire({
      title: "Bienvenue 👋",
      html: `
        Merci de vouloir rejoindre notre application !<br><br>
        📝 Veuillez remplir le formulaire attentivement et lire le contrat avant de le signer.<br><br>
        <span style="color: red; font-weight: bold;">
          NB : L’adresse email et le mot de passe que vous entrez ici<br>
          seront nécessaires pour vous connecter après acceptation.
        </span>
      `,
      icon: "info",
      confirmButtonText: "Compris",
      showClass: {
        popup: "animate__animated animate__fadeInUp animate__faster",
      },
      hideClass: {
        popup: "animate__animated animate__fadeOutDown animate__faster",
      },
    });
  }, []);

  useEffect(() => {
    axios
      .get("http://localhost:9090/api/point_vente/all")
      .then((res) => setPointsDeVente(res.data))
      .catch((err) => console.error("Erreur chargement points de vente", err));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    const parsedValue = name === "pointDeVenteId" ? (value !== "" ? parseInt(value) : "") : value;
    setFormData({ ...formData, [name]: parsedValue });
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.nom || !/^[A-Za-zÀ-ÿ\s-]+$/.test(formData.nom)) newErrors.nom = "Nom invalide";
    if (!formData.prenom || !/^[A-Za-zÀ-ÿ\s-]+$/.test(formData.prenom)) newErrors.prenom = "Prénom invalide";
    if (!formData.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) newErrors.email = "Email invalide";
    if (!formData.motDePasse || !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d]).{8,}$/.test(formData.motDePasse)) newErrors.motDePasse = "Mot de passe trop faible (1 majuscule, 1 minuscule, 1 chiffre, 1 symbole, 8 caractères min)";
    if (!formData.telephone || !/^\d{8}$/.test(formData.telephone)) newErrors.telephone = "Téléphone invalide (8 chiffres)";
    if (!formData.cin || !/^\d{8}$/.test(formData.cin)) newErrors.cin = "CIN invalide (8 chiffres)";
    if (!formData.adresse) newErrors.adresse = "Adresse requise";
    if (!formData.typeActivite) newErrors.typeActivite = "Activité requise";
    if (!formData.pointDeVenteId) newErrors.pointDeVenteId = "Point de vente requis";

    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) {
      Object.values(newErrors).forEach(msg => alert("❌ " + msg));
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setBackendError("");
    if (!validate()) return;

    try {
      const response = await axios.post("http://localhost:9090/api/contract/generate", formData);
      console.log("✅ Contrat généré :", response.data);
      console.log("📩 Email courant :", formData.email);
      nextStep();
    } catch (error) {
      console.error("❌ Erreur lors de la génération du contrat", error);
      const msg = error.response?.data?.message || "Erreur inconnue du serveur";
      setBackendError(msg);
      alert("❌ " + msg);
    }
  };

  return (
    <div className="step-form-container"> 
     <form onSubmit={handleSubmit} className="p-4 space-y-4">
      {/* Progression multi-step visuelle */}
      {/* <div className="flex justify-between items-center mb-6">
        {[1, 2, 3, 4].map((num) => (
          <div key={num} className={`w-8 h-8 rounded-full flex items-center justify-center text-white font-bold ${num === 1 ? 'bg-blue-600' : 'bg-gray-300'}`}>
            {num}
          </div>
        ))}
      </div> */}
      <Stepper step={1} /> 
      <h2 className="text-xl font-bold">📝 Formulaire de Demande</h2>

      <input type="text" name="nom" placeholder="Nom" value={formData.nom} onChange={handleChange} className="w-full p-2 border rounded" required />
      <input type="text" name="prenom" placeholder="Prénom" value={formData.prenom} onChange={handleChange} className="w-full p-2 border rounded" required />
      <input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} className="w-full p-2 border rounded" required />
      <input type="password" name="motDePasse" placeholder="Mot de passe" value={formData.motDePasse} onChange={handleChange} className="w-full p-2 border rounded" required />
      <input type="text" name="telephone" placeholder="Téléphone" value={formData.telephone} onChange={handleChange} className="w-full p-2 border rounded" required />
      <input type="text" name="cin" placeholder="CIN" value={formData.cin} onChange={handleChange} className="w-full p-2 border rounded" required />
      <input type="text" name="adresse" placeholder="Adresse" value={formData.adresse} onChange={handleChange} className="w-full p-2 border rounded" required />
      <input type="text" name="typeActivite" placeholder="Type d'activité" value={formData.typeActivite} onChange={handleChange} className="w-full p-2 border rounded" required />

      <select name="pointDeVenteId" value={formData.pointDeVenteId} onChange={handleChange} className="w-full p-2 border rounded">
        <option value="">-- Sélectionner un point de vente --</option>
        {pointsDeVente.map((point) => (
          <option key={point.idP} value={point.idP}>{point.nomP}</option>
        ))}
      </select>

      <div className="flex justify-end">
        <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded button-primary" >Suivant</button>
        
      </div>
    </form>
    
    
    
    
    
    
    </div>
   
  );
};

export default Step1Form;
