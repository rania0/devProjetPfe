
// import React, { useEffect, useState } from "react";
// import axios from "../api";
// import Swal from "sweetalert2";
// import { FaFilePdf, FaBoxOpen, FaFolderOpen, FaMagic, FaSyncAlt } from "react-icons/fa";
// import "./PageAffectation.css";

// const PageAffectation = () => {
//   const [sessionId, setSessionId] = useState(null);
//   const [groupes, setGroupes] = useState([]);
//   const [pdfs, setPdfs] = useState({});
//   const [dateLimite, setDateLimite] = useState("");
//   const [isLoading, setIsLoading] = useState(false);
//   const [expired, setExpired] = useState(false);
//   const [lancementEffectue, setLancementEffectue] = useState(false);
//   const [demandesAffectees, setDemandesAffectees] = useState([]);

//   const DELAI_SIMULATION = 20000;

//   useEffect(() => {
//     const storedLancement = localStorage.getItem("lancementEffectue");
//     const storedDate = localStorage.getItem("dateLimite");

//     if (storedLancement === "true") setLancementEffectue(true);
//     if (storedDate) setDateLimite(storedDate);
//   }, []);

//   const fetchLastClosedSession = async () => {
//     const token = localStorage.getItem("accessToken");
//     try {
//       const res = await axios.get("http://localhost:9090/api/session-commande/last-closed", {
//         headers: { Authorization: `Bearer ${token}` },
//       });
//       if (res.data !== sessionId) {
//         setSessionId(res.data);
//       }
//     } catch (error) {
//       Swal.fire("Erreur", "Erreur lors de la récupération de la session.", "error");
//     }
//   };

//   useEffect(() => {
//     fetchLastClosedSession();
//   }, []);

//   useEffect(() => {
//     const token = localStorage.getItem("accessToken");
//     if (sessionId) {
//       axios
//         .get(`http://localhost:9090/api/preparation/non-affectees?sessionId=${sessionId}`, {
//           headers: { Authorization: `Bearer ${token}` },
//         })
//         .then((res) => setGroupes(res.data)) // toujours setGroupes, car le tableau utilise ce nom
//         .catch(() => Swal.fire("Erreur", "Erreur lors du chargement des produits non affectés.", "error"));
//     }
//   }, [sessionId]);

//   const handleFileChange = (e, produitId) => {
//     setPdfs({ ...pdfs, [produitId]: e.target.files[0] });
//   };

//   const handleUploadPDF = async (produitId) => {
//     const token = localStorage.getItem("accessToken");
//     const formData = new FormData();
//     formData.append("file", pdfs[produitId]);
//     formData.append("produitId", produitId);
//     formData.append("sessionId", sessionId);
//     try {
//       await axios.post(`http://localhost:9090/api/preparation/upload-magazine`, formData, {
//         headers: {
//           Authorization: `Bearer ${token}`,
//           "Content-Type": "multipart/form-data",
//         },
//       });
//       Swal.fire({
//         title: "✅ PDF bien envoyé !",
//         text: "Le fichier a été enregistré pour ce produit.",
//         icon: "success",
//         timer: 2000,
//         showConfirmButton: false,
//       });
//     } catch (error) {
//       Swal.fire("Erreur", "Erreur lors de l'upload du PDF.", "error");
//     }
//   };

//   const handleAffectation = async () => {
//     if (!dateLimite) return Swal.fire("Erreur", "Date limite obligatoire", "warning");
//     const token = localStorage.getItem("accessToken");
//     try {
//       setIsLoading(true);
//       await axios.post(
//         `http://localhost:9090/api/preparation/affectation-auto?sessionId=${sessionId}&dateLimite=${dateLimite}`,
//         {},
//         { headers: { Authorization: `Bearer ${token}` } }
//       );

//       Swal.fire({
//         title: "🔍 Recherche en cours...",
//         text: "Cela peut prendre jusqu'à 1h. Veuillez patienter...",
//         timer: DELAI_SIMULATION,
//         allowOutsideClick: false,
//         didOpen: () => {
//           Swal.showLoading();
//         },
//       });

//       setTimeout(() => {
//         setExpired(true);
//         setIsLoading(false);
//         setLancementEffectue(true);
//         localStorage.setItem("lancementEffectue", "true");
//         localStorage.setItem("dateLimite", dateLimite);
//       }, DELAI_SIMULATION);
//     } catch (error) {
//       Swal.fire("Erreur", "Erreur durant l'affectation.", "error");
//     }
//   };

//   const handleRelancer = async (produitId) => {
//     const token = localStorage.getItem("accessToken");
//     try {
//       await axios.post(
//         `http://localhost:9090/api/preparation/relancer?produitId=${produitId}&sessionId=${sessionId}&dateLimite=${dateLimite}`,
//         {},
//         { headers: { Authorization: `Bearer ${token}` } }
//       );
//       Swal.fire("Relancé", "La demande a été relancée avec succès.", "success");
//     } catch (err) {
//       Swal.fire("Erreur", "Erreur lors de la relance.", "error");
//     }
//   };
  
// const fetchDemandesAffectees = async () => {
//   const token = localStorage.getItem("accessToken");
//   try {
//     const res = await axios.get("http://localhost:9090/api/preparation/demande-affectees", {
//       headers: { Authorization: `Bearer ${token}` },
//     });
//     setDemandesAffectees(res.data);
//   } catch (error) {
//     console.error("Erreur chargement des demandes affectées", error);
//   }
// };

// useEffect(() => {
//   fetchDemandesAffectees();
// }, []);
// const confirmerLivraison = async (demandeId) => {
//   const token = localStorage.getItem("accessToken");
//   try {
//     await axios.put(`http://localhost:9090/api/preparation/confirmer-livraison/${demandeId}`, {}, {
//       headers: { Authorization: `Bearer ${token}` }
//     });
//     Swal.fire("Succès", "Livraison confirmée avec succès", "success");
//     fetchDemandesAffectees();
//   } catch (err) {
//     Swal.fire("Erreur", "Impossible de confirmer la livraison", "error");
//   }
// };

//   return (
//     <div className="page-affectation">
//       <div className="explication-zone">
//         📌 <strong>Important :</strong> Vous devez d'abord <strong>lancer l'affectation automatique</strong> avant de
//         pouvoir <em>uploader un magazine</em> ou <em>relancer la recherche de fournisseur</em>.
//       </div>

//       <h2 className="text-2xl font-bold flex items-center gap-2 mb-4">
//         <FaMagic className="icon-title" /> Gestion des Affectations
//       </h2>

//       <div className="date-input-zone">
//         Cette commande doit être préparée avant :
//         <input
//           className="date-input"
//           type="date"
//           value={dateLimite}
//           onChange={(e) => setDateLimite(e.target.value)}
//         />
//       </div>

//       <table className="affectation-table">
//         <thead>
//           <tr>
//             <th>Produit</th>
//             <th>Quantité</th>
//             <th>Type</th>
//             <th>PDF</th>
//             <th>Relancer</th>
//           </tr>
//         </thead>
       
//         <tbody>
//   {groupes.map((groupe) => (
//     <tr key={groupe.id}>
//       <td>{groupe.produit.nom}</td>
//       <td>{groupe.quantiteDemandee}</td>
//       <td>{groupe.produit.typeProduit}</td>
//       <td>
//         {groupe.produit.typeProduit === "MAGAZINE" && (
//           <>
//             <input
//               type="file"
//               accept="application/pdf"
//               onChange={(e) => handleFileChange(e, groupe.produit.id)}
//               disabled={!lancementEffectue}
//             />
//             <button
//               className="upload-btn"
//               onClick={() => handleUploadPDF(groupe.produit.id)}
//               disabled={!lancementEffectue || !pdfs[groupe.produit.id]}
//             >
//               Upload
//             </button>
//           </>
//         )}
//       </td>
//       <td>
//         <button
//           className="relancer-btn"
//           onClick={() => handleRelancer(groupe.produit.id)}
//           disabled={!lancementEffectue}
//         >
//           <FaSyncAlt className="inline-block mr-1" /> Relancer
//         </button>
//       </td>
//     </tr>
//   ))}
// </tbody>
//       </table>

//       {expired && (
//         <div style={{ color: "#dc2626", fontWeight: "bold", marginBottom: "1rem" }}>
//           ❌ Certains produits n'ont pas de fournisseur disponible. Vous pouvez relancer dans 15 minutes.
//         </div>
//       )}

//       <button className="affecter-btn" onClick={handleAffectation} disabled={isLoading || lancementEffectue}>
//         <FaMagic className="mr-2" /> Lancer l'affectation automatique
//       </button>
//       {demandesAffectees.length > 0 && (
//   <>
//     <h3 className="text-xl font-bold mt-6 mb-2">📦 Suivi des Commandes Affectées</h3>
//     <table className="affectation-table">
//       <thead>
//         <tr>
//           <th>Produit</th>
//           <th>Quantité</th>
//           <th>Fournisseur</th>
//           <th>Téléphone</th>
//           <th>Société</th>
//           <th>État</th>
//           <th>Confirmation</th>
//         </tr>
//       </thead>
//       <tbody>
//         {demandesAffectees.map((demande) => (
//           <tr key={demande.id}>
//             <td>{demande.produit.nom}</td>
//             <td>{demande.quantiteDemandee}</td>
//             <td>{demande.fournisseur.nom} {demande.fournisseur.prenom}</td>
//             <td>{demande.fournisseur.numTel}</td>
//             <td>{demande.fournisseur.nomSociete}</td>
//             <td>{demande.etatCommande}</td>
//             <td>
//               {demande.etatCommande === "EN_ROUTE" && (
//                 <button
//                   className="btn-accept"
//                   onClick={() => confirmerLivraison(demande.id)}
//                 >
//                   Confirmer
//                 </button>
//               )}
//             </td>
//           </tr>
//         ))}
//       </tbody>
//     </table>
//   </>
// )}
//     </div>
//   );
// };

// export default PageAffectation;



// import React, { useEffect, useState } from "react";
// import axios from "../api";
// import Swal from "sweetalert2";
// import { FaFilePdf, FaBoxOpen, FaFolderOpen, FaMagic, FaSyncAlt } from "react-icons/fa";
// import "./PageAffectation.css";
// import LoadingSpinner from "./LoadingSpinner";

// const PageAffectation = () => {
//   const [sessionId, setSessionId] = useState(null);
//   const [groupes, setGroupes] = useState([]);
//   const [pdfs, setPdfs] = useState({});
//   const [dateLimite, setDateLimite] = useState("");
//   const [isLoading, setIsLoading] = useState(false);
//   const [expired, setExpired] = useState(false);
//   const [lancementEffectue, setLancementEffectue] = useState(false);
//   const [demandesAffectees, setDemandesAffectees] = useState([]);

//   const DELAI_SIMULATION = 20000;

//   useEffect(() => {
//     const storedLancement = localStorage.getItem("lancementEffectue");
//     const storedDate = localStorage.getItem("dateLimite");

//     if (storedLancement === "true") setLancementEffectue(true);
//     if (storedDate) setDateLimite(storedDate);
//   }, []);

//   const fetchLastClosedSession = async () => {
//     const token = localStorage.getItem("accessToken");
//     try {
//       const res = await axios.get("http://localhost:9090/api/session-commande/last-closed", {
//         headers: { Authorization: `Bearer ${token}` },
//       });
//       if (res.data !== sessionId) {
//         setSessionId(res.data);
//       }
//     } catch (error) {
//       Swal.fire("Erreur", "Erreur lors de la récupération de la session.", "error");
//     }
//   };

//   useEffect(() => {
//     fetchLastClosedSession();
//   }, []);

//   useEffect(() => {
//     const token = localStorage.getItem("accessToken");
//     if (sessionId) {
//       axios
//         .get(`http://localhost:9090/api/preparation/non-affectees?sessionId=${sessionId}`, {
//           headers: { Authorization: `Bearer ${token}` },
//         })
//         .then((res) => setGroupes(res.data)) // toujours setGroupes, car le tableau utilise ce nom
//         .catch(() => Swal.fire("Erreur", "Erreur lors du chargement des produits non affectés.", "error"));
//     }
//   }, [sessionId]);

//   const handleFileChange = (e, produitId) => {
//     setPdfs({ ...pdfs, [produitId]: e.target.files[0] });
//   };

//   const handleUploadPDF = async (produitId) => {
//     const token = localStorage.getItem("accessToken");
//     const formData = new FormData();
//     formData.append("file", pdfs[produitId]);
//     formData.append("produitId", produitId);
//     formData.append("sessionId", sessionId);
//     try {
//       await axios.post(`http://localhost:9090/api/preparation/upload-magazine`, formData, {
//         headers: {
//           Authorization: `Bearer ${token}`,
//           "Content-Type": "multipart/form-data",
//         },
//       });
//       Swal.fire({
//         title: "✅ PDF bien envoyé !",
//         text: "Le fichier a été enregistré pour ce produit.",
//         icon: "success",
//         timer: 2000,
//         showConfirmButton: false,
//       });
//     } catch (error) {
//       Swal.fire("Erreur", "Erreur lors de l'upload du PDF.", "error");
//     }
//   };

//   const handleAffectation = async () => {
//     if (!dateLimite) return Swal.fire("Erreur", "Date limite obligatoire", "warning");
//     const token = localStorage.getItem("accessToken");
//     try {
//       setIsLoading(true);
//       await axios.post(
//         `http://localhost:9090/api/preparation/affectation-auto?sessionId=${sessionId}&dateLimite=${dateLimite}`,
//         {},
//         { headers: { Authorization: `Bearer ${token}` } }
//       );

//       setTimeout(() => {
//         setExpired(true);
//         setIsLoading(false);
//         localStorage.setItem("dateLimite", dateLimite);
//       }, DELAI_SIMULATION);
//     } catch (error) {
//       Swal.fire("Erreur", "Erreur durant l'affectation.", "error");
//     }
//   };

//   // const handleRelancer = async (produitId) => {
//   //   const token = localStorage.getItem("accessToken");
//   //   try {
//   //     await axios.post(
//   //       `http://localhost:9090/api/preparation/relancer?produitId=${produitId}&sessionId=${sessionId}&dateLimite=${dateLimite}`,
//   //       {},
//   //       { headers: { Authorization: `Bearer ${token}` } }
//   //     );
//   //     Swal.fire("Relancé", "La demande a été relancée avec succès.", "success");
//   //   } catch (err) {
//   //     Swal.fire("Erreur", "Erreur lors de la relance.", "error");
//   //   }
//   // };
  
// const fetchDemandesAffectees = async () => {
//   const token = localStorage.getItem("accessToken");
//   try {
//     const res = await axios.get("http://localhost:9090/api/preparation/demande-affectees", {
//       headers: { Authorization: `Bearer ${token}` },
//     });
//     setDemandesAffectees(res.data);
//   } catch (error) {
//     console.error("Erreur chargement des demandes affectées", error);
//   }
// };

// useEffect(() => {
//   fetchDemandesAffectees();
// }, []);
// const confirmerLivraison = async (demandeId) => {
//   const token = localStorage.getItem("accessToken");
//   try {
//     await axios.put(`http://localhost:9090/api/preparation/confirmer-livraison/${demandeId}`, {}, {
//       headers: { Authorization: `Bearer ${token}` }
//     });
//     Swal.fire("Succès", "Livraison confirmée avec succès", "success");
//     fetchDemandesAffectees();
//   } catch (err) {
//     Swal.fire("Erreur", "Impossible de confirmer la livraison", "error");
//   }
// };

//   return (
//     <div className="page-affectation">
//       <div className="explication-zone">
//         📌 <strong>Important :</strong> Vous devez d'abord <strong>lancer l'affectation automatique</strong> avant de
//         pouvoir <em>uploader un magazine</em> ou <em>relancer la recherche de fournisseur</em>.
//       </div>

//       <h2 className="text-2xl font-bold flex items-center gap-2 mb-4">
//         <FaMagic className="icon-title" /> Gestion des Affectations
//       </h2>

//       <div className="date-input-zone">
//         Cette commande doit être préparée avant :
//         <input
//           className="date-input"
//           type="date"
//           value={dateLimite}
//           onChange={(e) => setDateLimite(e.target.value)}
//         />
//       </div>

//       <table className="affectation-table">
//         <thead>
//           <tr>
//             <th>Produit</th>
//             <th>Quantité</th>
//             <th>Type</th>
//             <th>PDF</th>
            
//           </tr>
//         </thead>
       
//         <tbody>
//   {groupes.map((groupe) => (
//     <tr key={groupe.id}>
//       <td>{groupe.produit.nom}</td>
//       <td>{groupe.quantiteDemandee}</td>
//       <td>{groupe.produit.typeProduit}</td>
//       <td>
//         {groupe.produit.typeProduit === "MAGAZINE" && (
//           <>
//             <input
//               type="file"
//               accept="application/pdf"
//               onChange={(e) => handleFileChange(e, groupe.produit.id)}
//               disabled={!lancementEffectue}
//             />
//             <button
//               className="upload-btn"
//               onClick={() => handleUploadPDF(groupe.produit.id)}
//               disabled={!lancementEffectue || !pdfs[groupe.produit.id]}
//             >
//               Upload
//             </button>
//           </>
//         )}
//       </td>
//     </tr>
//   ))}
// </tbody>
//       </table>

//       {expired && (
//         <div style={{ color: "#dc2626", fontWeight: "bold", marginBottom: "1rem" }}>
//           ❌ Certains produits n'ont pas de fournisseur disponible. Vous pouvez relancer dans 15 minutes.
//         </div>
//       )}
//       <button className="affecter-btn" onClick={handleAffectation} disabled={isLoading}>
//   <FaMagic className="mr-2" /> Lancer l'affectation automatique
// </button>

// {isLoading && <LoadingSpinner />}
//       {demandesAffectees.length > 0 && (
//   <>
//     <h3 className="text-xl font-bold mt-6 mb-2">📦 Suivi des Commandes Affectées</h3>
//     <table className="affectation-table">
//       <thead>
//         <tr>
//           <th>Produit</th>
//           <th>Quantité</th>
//           <th>Fournisseur</th>
//           <th>Téléphone</th>
//           <th>Société</th>
//           <th>État</th>
//           <th>Confirmation</th>
//         </tr>
//       </thead>
//       <tbody>
//         {demandesAffectees.map((demande) => (
//           <tr key={demande.id}>
//             <td>{demande.produit.nom}</td>
//             <td>{demande.quantiteDemandee}</td>
//             <td>{demande.fournisseur.nom} {demande.fournisseur.prenom}</td>
//             <td>{demande.fournisseur.numTel}</td>
//             <td>{demande.fournisseur.nomSociete}</td>
//             <td>{demande.etatCommande}</td>
//             <td>
//               {demande.etatCommande === "EN_ROUTE" && (
//                 <button
//                   className="btn-accept"
//                   onClick={() => confirmerLivraison(demande.id)}
//                 >
//                   Confirmer
//                 </button>
//               )}
//             </td>
//           </tr>
//         ))}
//       </tbody>
//     </table>
//   </>
// )}
//     </div>
//   );
// };

// export default PageAffectation;







import React, { useEffect, useState } from "react";
import axios from "../api";
import Swal from "sweetalert2";
import { FaFilePdf, FaBoxOpen, FaFolderOpen, FaMagic, FaSyncAlt } from "react-icons/fa";
import "./PageAffectation.css";
import LoadingSpinner from "./LoadingSpinner";

const PageAffectation = () => {
  const [sessionId, setSessionId] = useState(null);
  const [groupes, setGroupes] = useState([]);
  const [pdfs, setPdfs] = useState({});
  const [dateLimite, setDateLimite] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [expired, setExpired] = useState(false);
  const [lancementEffectue, setLancementEffectue] = useState(false);
  const [demandesAffectees, setDemandesAffectees] = useState([]);

  const DELAI_SIMULATION = 20000;

  useEffect(() => {
    const storedLancement = localStorage.getItem("lancementEffectue");
    const storedDate = localStorage.getItem("dateLimite");

    if (storedLancement === "true") setLancementEffectue(true);
    if (storedDate) setDateLimite(storedDate);
  }, []);

  const fetchLastClosedSession = async () => {
    const token = localStorage.getItem("accessToken");
    try {
      const res = await axios.get("http://localhost:9090/api/session-commande/last-closed", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (res.data !== sessionId) {
        setSessionId(res.data);
      }
    } catch (error) {
      Swal.fire("Erreur", "Erreur lors de la récupération de la session.", "error");
    }
  };

  useEffect(() => {
    fetchLastClosedSession();
  }, []);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (sessionId) {
      axios
        .get(`http://localhost:9090/api/preparation/non-affectees?sessionId=${sessionId}`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((res) => setGroupes(res.data)) // toujours setGroupes, car le tableau utilise ce nom
        .catch(() => Swal.fire("Erreur", "Erreur lors du chargement des produits non affectés.", "error"));
    }
  }, [sessionId]);

  const handleFileChange = (e, produitId) => {
    setPdfs({ ...pdfs, [produitId]: e.target.files[0] });
  };

  const handleUploadPDF = async (produitId) => {
    const token = localStorage.getItem("accessToken");
    const formData = new FormData();
    formData.append("file", pdfs[produitId]);
    formData.append("produitId", produitId);
    formData.append("sessionId", sessionId);
    try {
      await axios.post(`http://localhost:9090/api/preparation/upload-magazine`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      Swal.fire({
        title: "✅ PDF bien envoyé !",
        text: "Le fichier a été enregistré pour ce produit.",
        icon: "success",
        timer: 2000,
        showConfirmButton: false,
      });
    } catch (error) {
      Swal.fire("Erreur", "Erreur lors de l'upload du PDF.", "error");
    }
  };
  useEffect(() => {
    const lancement = localStorage.getItem("lancementTime");
    const sessionStockee = localStorage.getItem("sessionIdAffectation");
  
    if (lancement && sessionStockee === String(sessionId)) {
      const now = new Date().getTime();
      const diff = now - parseInt(lancement);
  
      const delay = 15 * 60 * 1000; // 15 minutes en ms
      if (diff < delay) {
        setIsLoading(true);
        const remaining = delay - diff;
  
        const timer = setTimeout(() => {
          setIsLoading(false);
          localStorage.removeItem("lancementTime");
          localStorage.removeItem("sessionIdAffectation");
        }, remaining);
  
        return () => clearTimeout(timer);
      }
    }
  }, [sessionId]);
  const [countdown, setCountdown] = useState("");

useEffect(() => {
  let interval;
  if (isLoading) {
    interval = setInterval(() => {
      const lancement = localStorage.getItem("lancementTime");
      if (lancement) {
        const now = new Date().getTime();
        const diff = parseInt(lancement) + 15 * 60 * 1000 - now;
        if (diff > 0) {
          const minutes = Math.floor(diff / 60000);
          const seconds = Math.floor((diff % 60000) / 1000);
          setCountdown(`${minutes}m ${seconds}s`);
        } else {
          setCountdown("");
          setIsLoading(false);
          clearInterval(interval);
          localStorage.removeItem("lancementTime");
          localStorage.removeItem("sessionIdAffectation");
        }
      }
    }, 1000);
  }

  return () => clearInterval(interval);
}, [isLoading]);

  
//   const handleAffectation = async () => {
//     if (!dateLimite) return Swal.fire("Erreur", "Date limite obligatoire", "warning");
//     const token = localStorage.getItem("accessToken");
//     try {
//       setIsLoading(true);
//       await axios.post(
//         `http://localhost:9090/api/preparation/affectation-auto?sessionId=${sessionId}&dateLimite=${dateLimite}`,
//         {},
//         { headers: { Authorization: `Bearer ${token}` } }
//       );

//       setTimeout(() => {
//         setExpired(true);
//         // setIsLoading(false);
//         localStorage.setItem("dateLimite", dateLimite);
//         const now = new Date().getTime();
// localStorage.setItem("lancementTime", now);
// localStorage.setItem("sessionIdAffectation", sessionId);
// setLancementEffectue(true);

//       }, DELAI_SIMULATION);
//     } catch (error) {
//       Swal.fire("Erreur", "Erreur durant l'affectation.", "error");
//     }
//   };
const handleAffectation = async () => {
  if (!dateLimite) return Swal.fire("Erreur", "Date limite obligatoire", "warning");

  // ✅ Check timer deja mawjouda
  const lancement = localStorage.getItem("lancementTime");
  const sessionStockee = localStorage.getItem("sessionIdAffectation");
  const now = new Date().getTime();

  // if (lancement && sessionStockee === String(sessionId)) {
  //   const diff = now - parseInt(lancement);
  //   const delay = 15 * 60 * 1000;
  //   if (diff < delay) {
  //     // ⛔ timer encore actif, ma na3mel chay
  //     console.log("⏳ Timer déjà en cours, on skip");
  //     return;
  //   }
  // }

  // ✅ Pas de timer actif → on lance !
  const token = localStorage.getItem("accessToken");
  try {
    setIsLoading(true); // ybedi spinner + message + disable button
    await axios.post(
      `http://localhost:9090/api/preparation/affectation-auto?sessionId=${sessionId}&dateLimite=${dateLimite}`,
      {},
      { headers: { Authorization: `Bearer ${token}` } }
    );

    // simulate delay (ex: 20 sec = DELAI_SIMULATION)
    setTimeout(() => {
      setExpired(true); // y5alli message el produit ma 3andouch fournisseur
      localStorage.setItem("dateLimite", dateLimite);

      // 🧠 timer storage
      const now = new Date().getTime();
      localStorage.setItem("lancementTime", now);
      localStorage.setItem("sessionIdAffectation", sessionId);
      setLancementEffectue(true);
    }, DELAI_SIMULATION);
  } catch (error) {
    Swal.fire("Erreur", "Erreur durant l'affectation.", "error");
  }
};



const fetchDemandesAffectees = async () => {
  const token = localStorage.getItem("accessToken");
  try {
    const res = await axios.get("http://localhost:9090/api/preparation/demande-affectees", {
      headers: { Authorization: `Bearer ${token}` },
    });
    setDemandesAffectees(res.data);
  } catch (error) {
    console.error("Erreur chargement des demandes affectées", error);
  }
};

useEffect(() => {
  fetchDemandesAffectees();
}, []);
const confirmerLivraison = async (demandeId) => {
  const token = localStorage.getItem("accessToken");
  try {
    await axios.put(`http://localhost:9090/api/preparation/confirmer-livraison/${demandeId}`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
    Swal.fire("Succès", "Livraison confirmée avec succès", "success");
    fetchDemandesAffectees();
  } catch (err) {
    Swal.fire("Erreur", "Impossible de confirmer la livraison", "error");
  }
};
useEffect(() => {
  const interval = setInterval(() => {
    fetchDemandesAffectees();
    console.log("✅ Refresh toutes les 10 secondes");
  }, 10000); // 10 secondes

  return () => clearInterval(interval);
}, []);


  return (
    <div className="page-affectation">
      <div className="explication-zone">
        📌 <strong>Important :</strong> Vous devez d'abord <strong>lancer l'affectation automatique</strong> avant de
        pouvoir <em>uploader un magazine</em> ou <em>relancer la recherche de fournisseur</em>.
      </div>

      <h2 className="text-2xl font-bold flex items-center gap-2 mb-4">
        <FaMagic className="icon-title" /> Gestion des Affectations
      </h2>

      <div className="date-input-zone">
        Cette commande doit être préparée avant :
        <input
          className="date-input"
          type="date"
          value={dateLimite}
          onChange={(e) => setDateLimite(e.target.value)}
        />
      </div>

      <table className="affectation-table">
        <thead>
          <tr>
            <th>Produit</th>
            <th>Quantité</th>
            <th>Type</th>
            <th>PDF</th>
            
          </tr>
        </thead>
       
        <tbody>
  {groupes.map((groupe) => (
    <tr key={groupe.id}>
      <td>{groupe.produit.nom}</td>
      <td>{groupe.quantiteDemandee}</td>
      <td>{groupe.produit.typeProduit}</td>
      <td>
        {groupe.produit.typeProduit === "MAGAZINE" && (
          <>
            <input
              type="file"
              accept="application/pdf"
              onChange={(e) => handleFileChange(e, groupe.produit.id)}
              disabled={!lancementEffectue}
            />
            <button
              className="upload-btn"
              onClick={() => handleUploadPDF(groupe.produit.id)}
              disabled={!lancementEffectue || !pdfs[groupe.produit.id]}
            >
              Upload
            </button>
          </>
        )}
      </td>
    </tr>
  ))}
</tbody>
      </table>

      {expired && (
        <div style={{ color: "#dc2626", fontWeight: "bold", marginBottom: "1rem" }}>
          ❌ Certains produits n'ont pas de fournisseur disponible,svp stanaaa ....
        </div>
      )}
      {/* <button className="affecter-btn" onClick={handleAffectation} disabled={isLoading}>
  <FaMagic className="mr-2" /> Lancer l'affectation automatique
</button> */}
<button
  className="affecter-btn"
  onClick={handleAffectation}
  disabled={isLoading || groupes.length === 0} 
>
  <FaMagic className="mr-2" />
  {isLoading ? `Patientez... ${countdown}` : "Lancer l'affectation automatique"}
</button>
{isLoading && (
  <div className="loading-section" style={{ textAlign: "center", margin: "30px 0" }}>
    <LoadingSpinner />
    <p className="loading-message" style={{ fontWeight: "bold", color: "#2f8ee5", fontSize: "18px" }}>
      {/* Recherche des fournisseurs en cours... */}
    </p>
  </div>
)}

      {demandesAffectees.length > 0 && (
  <>
    <h3 className="text-xl font-bold mt-6 mb-2">📦 Suivi des Commandes Affectées</h3>
    <table className="affectation-table">
      <thead>
        <tr>
          <th>Produit</th>
          <th>Quantité</th>
          <th>Fournisseur</th>
          <th>Téléphone</th>
          <th>Société</th>
          <th>État</th>
          <th>Confirmation</th>
        </tr>
      </thead>
      <tbody>
        {demandesAffectees.map((demande) => (
          <tr key={demande.id}>
            <td>{demande.produit.nom}</td>
            <td>{demande.quantiteDemandee}</td>
            <td>{demande.fournisseur.nom} {demande.fournisseur.prenom}</td>
            <td>{demande.fournisseur.numTel}</td>
            <td>{demande.fournisseur.nomSociete}</td>
            <td>{demande.etatCommande}</td>
            <td>
              {demande.etatCommande === "EN_ROUTE" && (
                <button
                  className="btn-accept"
                  onClick={() => confirmerLivraison(demande.id)}
                >
                  Confirmer
                </button>
              )}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  </>
)}
    </div>
  );
};

export default PageAffectation;
