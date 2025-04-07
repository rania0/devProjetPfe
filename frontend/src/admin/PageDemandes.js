// import { useEffect, useState } from "react";
// import axios from "axios";
// import "./dashboard.css"; // réutilise ton CSS existant ou crée un nouveau si tu veux
// import Swal from 'sweetalert2';


// const PageDemandes = () => {
//   const [enAttente, setEnAttente] = useState([]);
//   const [traitees, setTraitees] = useState([]);
//   const [refusText, setRefusText] = useState({});
//   const [showInputRefus, setShowInputRefus] = useState({});

//   useEffect(() => {
//     fetchDemandes();
//   }, []);

//   const fetchDemandes = async () => {
//     const token = localStorage.getItem("accessToken");
//     const config = { headers: { Authorization: `Bearer ${token}` } };
//     try {
//       const [att, tra] = await Promise.all([
//         axios.get("http://localhost:9090/api/admin/demandes/attente", config),
//         axios.get("http://localhost:9090/api/admin/demandes/traitees", config),
//       ]);
//       setEnAttente(att.data);
//       setTraitees(tra.data);
//     } catch (err) {
//       alert("❌ Erreur lors du chargement des demandes");
//     }
//   };

//   const handleAccepter = async (id) => {
//     try {
//       await axios.put(`http://localhost:9090/api/admin/demande/${id}/accepter`, null, {
//         headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
//       });
//       alert("✅ Demande acceptée !");
//       fetchDemandes();
//     } catch (err) {
//       alert("❌ Erreur lors de l'acceptation !");
//     }
//   };

//   const handleRefuser = async (id) => {
//     const { value: raison } = await Swal.fire({
//       title: "❌ Refuser la demande",
//       input: "text",
//       inputLabel: "Raison du refus",
//       inputPlaceholder: "Écrivez la raison ici...",
//       showCancelButton: true,
//       confirmButtonText: "Refuser",
//       cancelButtonText: "Annuler",
//       inputValidator: (value) => {
//         if (!value) {
//           return "⚠️ Vous devez écrire une raison !";
//         }
//       },
//     });
  
//     if (raison) {
//       try {
//         await axios.put("http://localhost:9090/api/admin/demande/refuser", {
//           responsableId: id,
//           raison: raison,
//         }, {
//           headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
//         });
  
//         Swal.fire("Refusé !", "La demande a bien été refusée.", "success");
//         fetchDemandes();
//       } catch (err) {
//         Swal.fire("Erreur", "Impossible de refuser la demande.", "error");
//       }
//     }
//   };
  

//   return (
//     <div>
//       <h2>📥 Demandes en attente</h2>
//       <table className="user-table">
//         <thead>
//           <tr>
//             <th>Nom</th>
//             <th>Email</th>
//             <th>Actions</th>
//           </tr>
//         </thead>
//         <tbody>
//           {enAttente.map((respo) => (
//             <tr key={respo.id}>
//               <td>{respo.nom} {respo.prenom}</td>
//               <td>{respo.email}</td>
//               <td>
//                 <button className="edit-btn" onClick={() => handleAccepter(respo.id)}>✅ Accepter</button>
//                 <button className="delete-btn" onClick={() => setShowInputRefus(prev => ({ ...prev, [respo.id]: true }))}>❌ Refuser</button>
//                 {showInputRefus[respo.id] && (
//                   <div style={{ marginTop: "8px" }}>
//                     <input
//                       type="text"
//                       placeholder="Raison du refus"
//                       value={refusText[respo.id] || ""}
//                       onChange={(e) =>
//                         setRefusText((prev) => ({ ...prev, [respo.id]: e.target.value }))
//                       }
//                     />
//                     <button onClick={() => handleRefuser(respo.id)}>Envoyer</button>
//                   </div>
//                 )}
//               </td>
//             </tr>
//           ))}
//         </tbody>
//       </table>

//       <h2 style={{ marginTop: "40px" }}>📄 Demandes traitées</h2>
//       <table className="user-table">
//         <thead>
//           <tr>
//             <th>Nom</th>
//             <th>Email</th>
//             <th>Statut</th>
//           </tr>
//         </thead>
//         <tbody>
//           {traitees.map((respo) => (
//             <tr key={respo.id}>
//               <td>{respo.nom} {respo.prenom}</td>
//               <td>{respo.email}</td>
//               <td className={respo.status === "ACCEPTE" ? "accept" : "refuse"}>
//                 {respo.status}
//               </td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
//     </div>
//   );
// };

// export default PageDemandes;





import { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import "./dashboard.css";
import { FaInfoCircle } from "react-icons/fa";

const PageDemandes = () => {
  const [enAttente, setEnAttente] = useState([]);
  const [traitees, setTraitees] = useState([]);
  const [selectedContratUrl, setSelectedContratUrl] = useState(null);
  useEffect(() => {
    fetchDemandes();
  }, []);
  const handleVoirContrat = (url) => {
    setSelectedContratUrl(url);
  };

  const fetchDemandes = async () => {
    const token = localStorage.getItem("accessToken");
    const config = { headers: { Authorization: `Bearer ${token}` } };
    try {
      const [att, tra] = await Promise.all([
        axios.get("http://localhost:9090/api/admin/demandes/attente", config),
        axios.get("http://localhost:9090/api/admin/demandes/traitees", config),
      ]);
      setEnAttente(att.data);
      setTraitees(tra.data);
    } catch (err) {
      Swal.fire("Erreur", "Erreur lors du chargement des demandes", "error");
    }
  };

  const handleAccepter = async (id) => {
    let timerInterval;
    Swal.fire({
      title: "📤 Envoi de l'email...",
      html: "Veuillez patienter... <b></b>",
      timer: 3000,
      timerProgressBar: true,
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
        const timer = Swal.getPopup().querySelector("b");
        timerInterval = setInterval(() => {
          timer.textContent = `${Swal.getTimerLeft()}`;
        }, 100);
      },
      willClose: () => clearInterval(timerInterval),
    });

    try {
      await axios.put(`http://localhost:9090/api/admin/demande/${id}/accepter`, null, {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });

      Swal.fire({
        title: "✅ Demande acceptée",
        text: "L'e-mail a été envoyé avec succès.",
        icon: "success",
        showClass: {
          popup: "animate__animated animate__fadeInUp animate__faster",
        },
        hideClass: {
          popup: "animate__animated animate__fadeOutDown animate__faster",
        },
      });

      fetchDemandes();
    } catch (err) {
      Swal.fire("Erreur", "Impossible d’accepter la demande", "error");
    }
  };

  const handleRefuser = async (id) => {
    const { value: raison } = await Swal.fire({
      title: "❌ Refuser la demande",
      input: "text",
      inputLabel: "Raison du refus",
      inputPlaceholder: "Écrivez la raison ici...",
      showCancelButton: true,
      confirmButtonText: "Refuser",
      cancelButtonText: "Annuler",
      inputValidator: (value) => {
        if (!value) {
          return "⚠️ Vous devez écrire une raison !";
        }
      },
    });

    if (raison) {
      let timerInterval;
      Swal.fire({
        title: "📤 Envoi de l'email...",
        html: "Veuillez patienter... <b></b>",
        timer: 3000,
        timerProgressBar: true,
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
          const timer = Swal.getPopup().querySelector("b");
          timerInterval = setInterval(() => {
            timer.textContent = `${Swal.getTimerLeft()}`;
          }, 100);
        },
        willClose: () => clearInterval(timerInterval),
      });

      try {
        await axios.put(
          "http://localhost:9090/api/admin/demande/refuser",
          { responsableId: id, raison },
          { headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` } }
        );

        Swal.fire({
          title: "Refusé ❌",
          text: "L’e-mail a été envoyé avec succès.",
          icon: "success",
          showClass: {
            popup: "animate__animated animate__fadeInUp animate__faster",
          },
          hideClass: {
            popup: "animate__animated animate__fadeOutDown animate__faster",
          },
        });

        fetchDemandes();
      } catch (err) {
        Swal.fire("Erreur", "Erreur lors du refus de la demande", "error");
      }
    }
  };

  return (
    <div>
      <h2>📥 <strong>Demandes en attente</strong></h2>
      <table className="user-table">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {enAttente.map((respo) => (
            <tr key={respo.id}>
              <td>{respo.nom} {respo.prenom}</td>
              <td>{respo.email}</td>
              <td>
                <button className="edit-btn" onClick={() => handleAccepter(respo.id)}>✅ Accepter</button>
                <button className="delete-btn" onClick={() => handleRefuser(respo.id)}>❌ Refuser</button>
                <button
  onClick={() => handleVoirContrat(respo.contratUrl)}
  style={{
    background: "transparent",
    border: "none",
    cursor: "pointer",
    color: "#3498db", // couleur bleue info
    fontSize: "20px",
    marginLeft: "10px"
  }}
  title="Voir contrat"
>
  <FaInfoCircle />
</button>

              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2 style={{ marginTop: "40px" }}>📄 <strong>Demandes traitées</strong></h2>
      <table className="user-table">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Email</th>
            <th>Statut</th>
          </tr>
        </thead>
        <tbody>
          {traitees.map((respo) => (
            <tr key={respo.id}>
              <td>{respo.nom} {respo.prenom}</td>
              <td>{respo.email}</td>
              <td className={respo.status === "ACCEPTE" ? "accept" : "refuse"}>
                <span className={`status-tag ${respo.status.toLowerCase()}`}>
                  {respo.status}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
       

      </table>
      {selectedContratUrl && (
  <div className="modal-overlay">
    <div className="modal-content">
      <button className="close-button" onClick={() => setSelectedContratUrl(null)}>
        &times;
      </button>
      <h3 style={{ marginBottom: "10px" }}>📄 Contrat signé</h3>
      <iframe
        src={selectedContratUrl}
        width="100%"
        height="500px"
        title="Contrat PDF"
        style={{ border: "1px solid #ccc", borderRadius: "5px" }}
      />
    </div>
  </div>
)}

    </div>
  );
};

export default PageDemandes;
