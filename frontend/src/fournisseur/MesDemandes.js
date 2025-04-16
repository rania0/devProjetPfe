


// import React, { useEffect, useState, useRef } from "react";
// import axios from "../api";
// import Swal from "sweetalert2";
// import "./MesDemandes.css";
// import notifSound from "../assets/notification.wav";
// import jsPDF from "jspdf";
// import autoTable from "jspdf-autotable";

// const MesDemandes = () => {
//   const [notifications, setNotifications] = useState([]);
//   const [demandesTraitees, setDemandesTraitees] = useState([]);
//   const [hasNew, setHasNew] = useState(false);
//   const [audioEnabled, setAudioEnabled] = useState(false);
//   const audioRef = useRef(null);
//   const [originalTitle] = useState(document.title);

//   const fetchNotifications = async () => {
//     const token = localStorage.getItem("accessToken");
//     try {
//       const res = await axios.get("http://localhost:9090/api/fournisseur/notifications", {
//         headers: { Authorization: `Bearer ${token}` },
//       });
//       setNotifications(res.data);
//       const nouvelle = res.data.length > 0;
//       if (nouvelle && !hasNew && audioEnabled) {
//         setHasNew(true);
//         audioRef.current?.play().catch((e) => console.log("🔇 Audio blocked:", e));
//       }
//       if (!nouvelle) setHasNew(false);
//     } catch (err) {
//       console.error("❌ Erreur notifications", err);
//     }
//   };

//   const fetchDemandesTraitees = async () => {
//     const token = localStorage.getItem("accessToken");
//     try {
//       const res = await axios.get("http://localhost:9090/api/fournisseur/mes-demandes-traitees", {
//         headers: { Authorization: `Bearer ${token}` },
//       });
//       setDemandesTraitees(res.data);
//     } catch (err) {
//       console.error("❌ Erreur demandes traitées", err);
//     }
//   };

//   const traiterDemande = async (demandeId, accepter) => {
//     const token = localStorage.getItem("accessToken");

//     const confirmation = await Swal.fire({
//       title: accepter ? "Confirmer l'acceptation ?" : "Confirmer le refus ?",
//       icon: "question",
//       showCancelButton: true,
//       confirmButtonText: accepter ? "Oui, accepter" : "Oui, refuser",
//       cancelButtonText: "Annuler",
//     });

//     if (!confirmation.isConfirmed) return;

//     try {
//       await axios.post(
//         `http://localhost:9090/api/fournisseur/traiter?demandeId=${demandeId}&accepter=${accepter}`,
//         {},
//         { headers: { Authorization: `Bearer ${token}` } }
//       );
//       Swal.fire("Succès", `Demande ${accepter ? "acceptée" : "refusée"}.`, "success");
//       fetchNotifications();
//       fetchDemandesTraitees(); // important !
//     } catch (err) {
//       console.error("❌ Erreur traitement:", err);
//     }
//   };

//   const updateEtatCommande = async (demandeId, nouvelEtat) => {
//     const token = localStorage.getItem("accessToken");
//     try {
//       await axios.post(
//         `http://localhost:9090/api/fournisseur/changer-etat?demandeId=${demandeId}&nouvelEtat=${nouvelEtat}`,
//         {},
//         { headers: { Authorization: `Bearer ${token}` } }
//       );
//       fetchDemandesTraitees(); // refresh
//     } catch (err) {
//       console.error("❌ Erreur changement état:", err);
//     }
//   };

//   const renderProgress = (demande) => {
//     // const etats = ["EN_PREPARATION", "PRETE_POUR_ENVOI", "EN_ROUTE", "LIVREE"];
//     // const etatActuel = demande.etatCommande || "EN_PREPARATION";
//     // const indexActuel = etats.indexOf(etatActuel);

//       const etats = ["EN_PREPARATION", "PRETE_POUR_ENVOI", "EN_ROUTE", "LIVREE"];
//       const etatActuel = demande.etatCommande || "EN_PREPARATION";
//       const indexActuel = etats.indexOf(etatActuel);

//     return (
//       <div className="progress-steps">
//         {etats.map((etat, index) => (
//           <React.Fragment key={etat}>
//             <div
//               className={`step ${
//                 index < indexActuel
//                   ? "active"
//                   : index === indexActuel
//                   ? "active"
//                   : index === indexActuel + 1
//                   ? "next"
//                   : "inactive"
//               }`}
//               onClick={() => index === indexActuel + 1 && updateEtatCommande(demande.id, etat)}
//             >
//               {etat.replaceAll("_", " ")}
//             </div>
//             {index < etats.length - 1 && <div className="line" />}
//           </React.Fragment>
//         ))}
//       </div>
//     );
//   };

//   useEffect(() => {
//     const handleFirstInteraction = () => {
//       setAudioEnabled(true);
//       window.removeEventListener("click", handleFirstInteraction);
//     };
//     window.addEventListener("click", handleFirstInteraction);
//     return () => window.removeEventListener("click", handleFirstInteraction);
//   }, []);

//   useEffect(() => {
//     fetchNotifications();
//     fetchDemandesTraitees();
//     const interval = setInterval(() => {
//       fetchNotifications();
//       fetchDemandesTraitees();
//     }, 15000);
//     return () => clearInterval(interval);
//   }, [audioEnabled]);

//   const genererFacturePDF = async (demande, prixUnitaire) => {
//     const doc = new jsPDF();
//     const today = new Date().toLocaleDateString("fr-FR");
  
//     const quantite = demande.quantiteDemandee;
//     const designation = demande.produit.nom;
//     const totalHT = prixUnitaire * quantite;
//     const tva = totalHT * 0.20;
//     const totalTTC = totalHT + tva;
  
//     const fournisseur = demande.fournisseur;
  
//     // 🧾 Titre
//     doc.setFontSize(24);
//     doc.setTextColor(40, 80, 160);
//     doc.text("Facture", 15, 20);
  
//     // 📍 Infos vendeur/client
//     doc.setFontSize(11);
//     doc.setTextColor(0);
//     doc.text("Vendeur", 15, 35);
//     doc.text(fournisseur.nomSociete || "Fournisseur", 15, 40);
//     doc.text(fournisseur.ville || "", 15, 45);
  
//     doc.text("Client", 105, 35);
//     doc.text("Promosport", 105, 40);
//     doc.text("Tunis", 105, 45);
  
//     doc.text(`Date : ${today}`, 15, 60);
  
//     // 📦 Tableau
//     autoTable(doc, {
//       startY: 70,
//       head: [["Description", "Quantité", "Unité", "Prix unitaire HT", "% TVA", "Total TTC"]],
//       body: [
//         [
//           designation,
//           quantite,
//           "pcs",
//           `${prixUnitaire.toFixed(2)} €`,
//           "20 %",
//           `${totalTTC.toFixed(2)} €`,
//         ],
//       ],
//       styles: {
//         fontSize: 10,
//         cellPadding: 3,
//       },
//       headStyles: {
//         fillColor: [41, 128, 185],
//         textColor: 255,
//       },
//     });
  
//     const finalY = doc.lastAutoTable.finalY;
  
//     // 📊 Totaux à droite
//     doc.setFontSize(11);
//     doc.text(`Total HT       : ${totalHT.toFixed(2)} €`, 140, finalY + 10, { align: "right" });
//     doc.text(`Total TVA (20%) : ${tva.toFixed(2)} €`, 140, finalY + 17, { align: "right" });
//     doc.setTextColor(0, 0, 180);
//     doc.setFontSize(13);
//     doc.text(`Total TTC       : ${totalTTC.toFixed(2)} €`, 140, finalY + 27, { align: "right" });
  
//     // 🖊️ Cachet en bas
//     doc.setTextColor(150, 0, 50);
//     doc.setFontSize(14);
//     doc.text("Cachet", 15, 285);
  
//     // 📥 Prévisualisation
//     const preview = doc.output("dataurlstring");
//     const result = await Swal.fire({
//       title: "Aperçu Facture",
//       html: `<iframe src="${preview}" width="100%" height="400px" style="border:none;"></iframe>`,
//       showCancelButton: true,
//       confirmButtonText: "✅ Imprimer",
//       cancelButtonText: "Annuler",
//       width: 700,
//     });
  
//     if (result.isConfirmed) {
//       doc.save(`Facture_${designation.replaceAll(" ", "_")}.pdf`);
//     }
//   };
//   useEffect(() => {
//     let blinkInterval;
//     const link = document.querySelector("link[rel~='icon']") || document.createElement("link");
//     link.rel = "icon";

//     if (hasNew && document.visibilityState !== "visible") {
//       let state = false;
//       blinkInterval = setInterval(() => {
//         document.title = state ? "🔴 Nouvelle Demande!" : originalTitle;
//         state = !state;
//       }, 1000);
//       link.href = "/favicon-alert.ico";
//     } else {
//       document.title = originalTitle;
//       link.href = "/favicon.ico";
//     }

//     document.head.appendChild(link);
//     return () => {
//       clearInterval(blinkInterval);
//       document.title = originalTitle;
//     };
//   }, [hasNew, originalTitle]);

//   return (
//     <div className="mes-demandes-container">
//       <h2>
//         📬 Nouvelles Demandes Disponibles {hasNew && <span className="notif-dot">🔴</span>}
//       </h2>
//       <audio ref={audioRef} src={notifSound} preload="auto" />
  
//       {/* 🔔 Notifications */}
//       <table className="table-demandes">
//         <thead>
//           <tr>
//             <th>Produit</th>
//             <th>Quantité</th>
//             <th>Type</th>
//             <th>Date Limite</th>
//             <th>Action</th>
//           </tr>
//         </thead>
//         <tbody>
//           {notifications.map((d) => (
//             <tr key={d.id}>
//               <td>{d.produit.nom}</td>
//               <td>{d.quantiteDemandee}</td>
//               <td>{d.produit.typeProduit}</td>
//               <td>{d.dateLimitePreparation?.substring(0, 10)}</td>
//               <td>
//                 <button className="btn-accept" onClick={() => traiterDemande(d.id, true)}>Accepter</button>
//                 <button className="btn-refuse" onClick={() => traiterDemande(d.id, false)}>Refuser</button>
//               </td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
  
//       {/* 📦 Suivi d’état */}
//       {demandesTraitees.length > 0 && (
//         <>
//           <h3 style={{ marginTop: "40px" }}>📦 Suivi des Demandes Acceptées</h3>
//           {demandesTraitees.map((demande) => {
//             const etats = ["EN_PREPARATION", "PRETE_POUR_ENVOI", "EN_ROUTE", "LIVREE"];
//             const etatActuel = demande.etatCommande || "EN_PREPARATION";
//             const indexActuel = etats.indexOf(etatActuel);
  
//             return (
//               <div key={demande.id} className="etat-suivi">
//                 <h4>{demande.produit.nom}</h4>
//                 <div className="progress-steps">
//                   {etats.map((etat, index) => (
//                     <React.Fragment key={etat}>
//                       <div
//                         className={`step ${
//                           index < indexActuel
//                             ? "active"
//                             : index === indexActuel
//                             ? "active"
//                             : index === indexActuel + 1
//                             ? "next"
//                             : "inactive"
//                         }`}
//                         onClick={async () => {
//                           if (index === indexActuel + 1) {
//                             if (etat === "PRETE_POUR_ENVOI") {
//                               const { value: prix } = await Swal.fire({
//                                 title: "Prix unitaire (€)",
//                                 input: "number",
//                                 inputLabel: "Veuillez entrer le prix unitaire de l'article",
//                                 inputPlaceholder: "Ex: 10.00",
//                                 showCancelButton: true,
//                                 confirmButtonText: "Valider",
//                                 cancelButtonText: "Annuler",
//                                 inputValidator: (value) => {
//                                   if (!value || isNaN(value) || parseFloat(value) <= 0) {
//                                     return "Veuillez entrer un prix valide.";
//                                   }
//                                 },
//                               });
  
//                               if (prix) {
//                                 await genererFacturePDF(demande, parseFloat(prix));
//                                 updateEtatCommande(demande.id, etat);
//                               }
//                             } else {
//                               updateEtatCommande(demande.id, etat);
//                             }
//                           }
//                         }}
//                       >
//                         {etat.replaceAll("_", " ")}
//                       </div>
//                       {index < etats.length - 1 && <div className="line" />}
//                     </React.Fragment>
//                   ))}
//                 </div>
//               </div>
//             );
//           })}
//         </>
//       )}
//     </div>
//   );
  
// };

// export default MesDemandes;





import React, { useEffect, useState, useRef } from "react";
import axios from "../api";
import Swal from "sweetalert2";
import "./MesDemandes.css";
import notifSound from "../assets/notification.wav";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import QRCode from "qrcode";

const MesDemandes = () => {
  const [notifications, setNotifications] = useState([]);
  const [demandesTraitees, setDemandesTraitees] = useState([]);
  const [hasNew, setHasNew] = useState(false);
  const [audioEnabled, setAudioEnabled] = useState(false);
  const audioRef = useRef(null);
  const [originalTitle] = useState(document.title);

  const fetchNotifications = async () => {
    const token = localStorage.getItem("accessToken");
    try {
      const res = await axios.get("http://localhost:9090/api/fournisseur/notifications", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setNotifications(res.data);
      const nouvelle = res.data.length > 0;
      if (nouvelle && !hasNew && audioEnabled) {
        setHasNew(true);
        audioRef.current?.play().catch((e) => console.log("🔇 Audio blocked:", e));
      }
      if (!nouvelle) setHasNew(false);
    } catch (err) {
      console.error("❌ Erreur notifications", err);
    }
  };

  const fetchDemandesTraitees = async () => {
    const token = localStorage.getItem("accessToken");
    try {
      const res = await axios.get("http://localhost:9090/api/fournisseur/mes-demandes-traitees", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setDemandesTraitees(res.data);
    } catch (err) {
      console.error("❌ Erreur demandes traitées", err);
    }
  };

  const traiterDemande = async (demandeId, accepter) => {
    const token = localStorage.getItem("accessToken");

    const confirmation = await Swal.fire({
      title: accepter ? "Confirmer l'acceptation ?" : "Confirmer le refus ?",
      icon: "question",
      showCancelButton: true,
      confirmButtonText: accepter ? "Oui, accepter" : "Oui, refuser",
      cancelButtonText: "Annuler",
    });

    if (!confirmation.isConfirmed) return;

    try {
      await axios.post(
        `http://localhost:9090/api/fournisseur/traiter?demandeId=${demandeId}&accepter=${accepter}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
      Swal.fire("Succès", `Demande ${accepter ? "acceptée" : "refusée"}.`, "success");
      fetchNotifications();
      fetchDemandesTraitees(); // important !
    } catch (err) {
      console.error("❌ Erreur traitement:", err);
    }
  };

  const updateEtatCommande = async (demandeId, nouvelEtat) => {
    const token = localStorage.getItem("accessToken");
    try {
      await axios.post(
        `http://localhost:9090/api/fournisseur/changer-etat?demandeId=${demandeId}&nouvelEtat=${nouvelEtat}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
      fetchDemandesTraitees(); // refresh
    } catch (err) {
      console.error("❌ Erreur changement état:", err);
    }
  };
  useEffect(() => {
    fetchNotifications();
    fetchDemandesTraitees();
    const interval = setInterval(() => {
      fetchNotifications();
      fetchDemandesTraitees();
    }, 10000); 
    return () => clearInterval(interval);
  }, [audioEnabled]);
  const renderProgress = (demande) => {
      const etats = ["EN_PREPARATION", "PRETE_POUR_ENVOI", "EN_ROUTE", "LIVREE"];
      const etatActuel = demande.etatCommande || "EN_PREPARATION";
      const indexActuel = etats.indexOf(etatActuel);

    return (
      <div className="progress-steps">
        {etats.map((etat, index) => (
          <React.Fragment key={etat}>
            <div
              className={`step ${
                index < indexActuel
                  ? "active"
                  : index === indexActuel
                  ? "active"
                  : index === indexActuel + 1
                  ? "next"
                  : "inactive"
              }`}
              onClick={() => index === indexActuel + 1 && updateEtatCommande(demande.id, etat)}
            >
              {etat.replaceAll("_", " ")}
            </div>
            {index < etats.length - 1 && <div className="line" />}
          </React.Fragment>
        ))}
      </div>
    );
  };

  useEffect(() => {
    const handleFirstInteraction = () => {
      setAudioEnabled(true);
      window.removeEventListener("click", handleFirstInteraction);
    };
    window.addEventListener("click", handleFirstInteraction);
    return () => window.removeEventListener("click", handleFirstInteraction);
  }, []);

  useEffect(() => {
    fetchNotifications();
    fetchDemandesTraitees();
    const interval = setInterval(() => {
      fetchNotifications();
      fetchDemandesTraitees();
    }, 15000);
    return () => clearInterval(interval);
  }, [audioEnabled]);

  const genererFacturePDF = async (demande, prixUnitaire) => {
    const doc = new jsPDF();
    const today = new Date().toLocaleDateString("fr-FR");
    const quantite = demande.quantiteDemandee;
    const designation = demande.produit.nom;
    const totalHT = prixUnitaire * quantite;
    const tva = totalHT * 0.20;
    const totalTTC = totalHT + tva;
  
    // 👇 Préparation des infos QR code selon type
    let qrData = {};
    const typeProduit = demande.produit.typeProduit;
  
    if (typeProduit === "PAPIER_THERMIQUE") {
      qrData = {
        idProduit: demande.produit.id,
        nomProduit: designation,
        largeur: demande.produit.largeur,
        diametre: demande.produit.diametre,
        longueur: demande.produit.longueur,
        grammage: demande.produit.grammage,
        dateExpiration: new Date().toISOString().split("T")[0],
        quantite: quantite
      };
    } else if (typeProduit === "MAGAZINE") {
      const { value: nbPages } = await Swal.fire({
        title: "Nombre de pages",
        input: "number",
        inputLabel: "Veuillez entrer le nombre de pages du magazine",
        inputPlaceholder: "Ex: 32",
        inputAttributes: {
          min: 1
        },
        showCancelButton: true,
        confirmButtonText: "Valider",
        cancelButtonText: "Annuler",
        inputValidator: (value) => {
          if (!value || isNaN(value) || parseInt(value) <= 0) {
            return "Veuillez entrer un nombre valide.";
          }
        }
      });
  
      if (!nbPages) return;
  
      qrData = {
        idProduit: demande.produit.id,
        nomProduit: designation,
        quantite: quantite,
        nombrePages: nbPages
      };
    }
  
    // 👇 Générer l'image QR à partir de json
    const qrCodeDataURL = await QRCode.toDataURL(JSON.stringify(qrData));
  
    // 🧾 Génération du PDF
    doc.setFontSize(24);
    doc.text("Facture 2025", 15, 20);
    
  
    doc.setFontSize(12);
    doc.text(`Date : ${today}`, 150, 20);
    doc.text(`Client : Promosport`, 150, 30);
    doc.text(`Fournisseur : ${demande.fournisseur.nomSociete}`, 15, 30);
    doc.text(`Ville : ${demande.fournisseur.ville}`, 15, 40);
  
    autoTable(doc, {
      startY: 50,
      head: [["Qté", "Désignation", "Prix Unit.", "Total HT"]],
      body: [[quantite, designation, `${prixUnitaire.toFixed(2)} DT`, `${totalHT.toFixed(2)} DT`]],
      styles: {
        fillColor: [41, 128, 185]
      }
    });
  
    const finY = doc.lastAutoTable.finalY || 60;
  
    doc.text(`Total HT : ${totalHT.toFixed(2)} DT`, 15, finY + 10);
    doc.text(`TVA (20%) : ${tva.toFixed(2)} DT`, 15, finY + 20);
    doc.setFont("helvetica", "bold");
    doc.text(`Total TTC : ${totalTTC.toFixed(2)} DT`, 15, finY + 30);
    doc.setFont("helvetica", "normal");
  
    // ✨ QR code en bas à droite
    // doc.addImage(qrCodeDataURL, "PNG", 150, finY + 10, 40, 40);
    const yQRCode = Math.min(finY + 50, 250); // pour pas dépasser la page
doc.addImage(qrCodeDataURL, "PNG", 150, yQRCode, 40, 40);
console.log("✅ QR Code URL:", qrCodeDataURL);
    // ✍️ Mention cachet
    doc.setFontSize(10);
    doc.text("Cachet :", 15, 280);
    const preview = doc.output("dataurlstring");

const confirm = await Swal.fire({
  title: "Aperçu de la Facture",
  html: `<iframe src="${preview}" width="100%" height="500px" style="border:none;"></iframe>`,
  // showCancelButton: true,
  confirmButtonText: "✅ Oui, télécharger",
  // cancelButtonText: "Annuler",
  width: 800,
  heightAuto: false
});

if (confirm.isConfirmed) {
  doc.save(`Facture_${designation.replaceAll(" ", "_")}.pdf`);
}
  
    if (confirm.isConfirmed) {
      doc.save(`Facture_${designation.replaceAll(" ", "_")}.pdf`);
    }
  };
  useEffect(() => {
    let blinkInterval;
    const link = document.querySelector("link[rel~='icon']") || document.createElement("link");
    link.rel = "icon";

    if (hasNew && document.visibilityState !== "visible") {
      let state = false;
      blinkInterval = setInterval(() => {
        document.title = state ? "🔴 Nouvelle Demande!" : originalTitle;
        state = !state;
      }, 1000);
      link.href = "/favicon-alert.ico";
    } else {
      document.title = originalTitle;
      link.href = "/favicon.ico";
    }

    document.head.appendChild(link);
    return () => {
      clearInterval(blinkInterval);
      document.title = originalTitle;
    };
  }, [hasNew, originalTitle]);

  return (
    <div className="mes-demandes-container">
      <h2>
        📬 Nouvelles Demandes Disponibles {hasNew && <span className="notif-dot">🔴</span>}
      </h2>
      <audio ref={audioRef} src={notifSound} preload="auto" />
  
      {/* 🔔 Notifications */}
      <table className="table-demandes">
        <thead>
          <tr>
            <th>Produit</th>
            <th>Quantité</th>
            <th>Type</th>
            <th>Date Limite</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {notifications.map((d) => (
            <tr key={d.id}>
              <td>{d.produit.nom}</td>
              <td>{d.quantiteDemandee}</td>
              <td>{d.produit.typeProduit}</td>
              <td>{d.dateLimitePreparation?.substring(0, 10)}</td>
              <td>
                <button className="btn-accept" onClick={() => traiterDemande(d.id, true)}>Accepter</button>
                <button className="btn-refuse" onClick={() => traiterDemande(d.id, false)}>Refuser</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
  
      {/* 📦 Suivi d’état */}
      {demandesTraitees.length > 0 && (
        <>
          <h3 style={{ marginTop: "40px" }}>📦 Suivi des Demandes Acceptées</h3>
          {demandesTraitees.map((demande) => {
            const etats = ["EN_PREPARATION", "PRETE_POUR_ENVOI", "EN_ROUTE", "LIVREE"];
            const etatActuel = demande.etatCommande || "EN_PREPARATION";
            const indexActuel = etats.indexOf(etatActuel);
  
            return (
              <div key={demande.id} className="etat-suivi">
                <h4>{demande.produit.nom}</h4>
                <div className="progress-steps">
                  {etats.map((etat, index) => (
                    <React.Fragment key={etat}>
                      <div
                        className={`step ${
                          index < indexActuel
                            ? "active"
                            : index === indexActuel
                            ? "active"
                            : index === indexActuel + 1
                            ? "next"
                            : "inactive"
                        }`}
                        onClick={async () => {
                          if (index === indexActuel + 1 && etat !== "LIVREE") {
                            if (etat === "PRETE_POUR_ENVOI") {
                              const { value: prix } = await Swal.fire({
                                title: "Prix unitaire (€)",
                                input: "number",
                                inputLabel: "Veuillez entrer le prix unitaire de l'article",
                                inputPlaceholder: "Ex: 10.00",
                                showCancelButton: true,
                                confirmButtonText: "Valider",
                                cancelButtonText: "Annuler",
                                inputValidator: (value) => {
                                  if (!value || isNaN(value) || parseFloat(value) <= 0) {
                                    return "Veuillez entrer un prix valide.";
                                  }
                                },
                              });
  
                              if (prix) {
                                await genererFacturePDF(demande, parseFloat(prix));
                                updateEtatCommande(demande.id, etat);
                              }
                            } else {
                              updateEtatCommande(demande.id, etat);
                            }
                          }
                        }}
                      >
                        {etat.replaceAll("_", " ")}
                      </div>
                      {index < etats.length - 1 && <div className="line" />}
                    </React.Fragment>
                  ))}
                </div>
              </div>
            );
          })}
        </>
      )}
    </div>
  );
  
};

export default MesDemandes;


