import React, { useState } from "react";
import axios from "../api"; // Ton instance axios configurée
import Swal from "sweetalert2";

const CreerSessionCommande = () => {
  const [dateDebut, setDateDebut] = useState("");
  const [dateFin, setDateFin] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("accessToken");

      const payload = {
        dateDebut: dateDebut,  // type: "2025-04-04T08:00"
        dateFin: dateFin       // type: "2025-04-07T23:59"
      };

      console.log("👉 Payload envoyé :", payload);

      const response = await axios.post("http://localhost:9090/api/session-commande/create", payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("✅ Réponse backend :", response.data);

      Swal.fire({
        icon: "success",
        title: "✅ Session créée avec succès",
        showConfirmButton: false,
        timer: 2000,
      });

      setDateDebut("");
      setDateFin("");
    } catch (error) {
      console.error("❌ Erreur création session :", error);
      Swal.fire({
        icon: "error",
        title: "Erreur",
        text: "❌ Impossible de créer la session",
      });
    }
  };

  return (
    <div className="container mx-auto p-4 max-w-md bg-white shadow-xl rounded-xl mt-10">
      <h2 className="text-xl font-bold mb-4 text-center">Créer une session de commande</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block font-semibold">Date de début :</label>
          <input
            type="datetime-local"
            className="w-full p-2 border rounded"
            value={dateDebut}
            onChange={(e) => setDateDebut(e.target.value)}
            required
          />
        </div>
        <div>
          <label className="block font-semibold">Date de fin :</label>
          <input
            type="datetime-local"
            className="w-full p-2 border rounded"
            value={dateFin}
            onChange={(e) => setDateFin(e.target.value)}
            required
          />
        </div>
        <button
          type="submit"
          className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700"
        >
          Créer la session
        </button>
      </form>
    </div>
  );
};

export default CreerSessionCommande;
