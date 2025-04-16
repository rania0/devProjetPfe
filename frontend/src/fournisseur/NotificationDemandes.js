import { useEffect, useState } from "react";
import axios from "../api";
import Swal from "sweetalert2";

const NotificationDemandes = () => {
  const [traitedIds, setTraitedIds] = useState([]); // Pour éviter les doublons

  const fetchDemandes = async () => {
    try {
        const token = localStorage.getItem("accessToken");

      const response = await fetch("http://localhost:9090/api/fournisseur/demandes", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Erreur lors de la récupération des demandes");
      }

      const data = await response.json();

      const enAttente = data.filter(
        (d) => d.statut === "EN_ATTENTE" && !traitedIds.includes(d.id)
      );

      for (const demande of enAttente) {
        Swal.fire({
          title: `🛎 Nouvelle demande de préparation`,
          html: `<strong>${demande.produit.nom}</strong><br/>Quantité: ${demande.quantiteDemandee}`,
          icon: "info",
          showCancelButton: true,
          confirmButtonText: "Accepter",
          cancelButtonText: "Refuser",
          allowOutsideClick: false,
        }).then(async (result) => {
          // ✅ transformer le booléen en string "true"/"false"
          const statut = result.isConfirmed === true ? "true" : "false";

          try {
            await axios.post(
              `http://localhost:9090/api/fournisseur/traiter?demandeId=${demande.id}&accepter=${statut}`,
              {},
              {
                headers: { Authorization: `Bearer ${token}` },
              }
            );
            setTraitedIds((prev) => [...prev, demande.id]);
            Swal.fire("Traitement", result.isConfirmed ? "Demande acceptée" : "Demande refusée", "success");
          } catch (err) {
            console.error("Erreur traitement :", err);
            Swal.fire("Erreur", "Impossible de traiter la demande.", "error");
          }
        });
      }
    } catch (err) {
      console.error("Erreur lors de la récupération des demandes :", err);
      Swal.fire("Erreur", "Impossible de récupérer les demandes.", "error");
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {
      fetchDemandes();
    }, 15000); // Appel toutes les 15s
    return () => clearInterval(interval);
  }, [traitedIds]);

  return null; // Pas de rendu visible dans le DOM
};

export default NotificationDemandes;
