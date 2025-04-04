import React, { useEffect, useState } from "react";
import axios from "../api";
import { useParams } from "react-router-dom";
import Swal from "sweetalert2";

const ResultatsSession = () => {
  const { id } = useParams(); // l'id de la session passée via l'URL
  const [produits, setProduits] = useState([]);

  useEffect(() => {
    const fetchResultats = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const response = await axios.get(`http://localhost:9090/api/session-commande/${id}/groupes`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        setProduits(response.data);
      } catch (error) {
        console.error("Erreur lors de la récupération des résultats", error);
        Swal.fire({
          icon: "error",
          title: "Erreur",
          text: "Impossible de récupérer les résultats de la session.",
        });
      }
    };

    fetchResultats();
  }, [id]);

  return (
    <div className="p-8">
      <h2 className="text-2xl font-bold mb-4">📊 Résultats de la session #{id}</h2>

      {produits.length === 0 ? (
        <p>🔄 Chargement des données...</p>
      ) : (
        <table className="w-full border border-gray-300">
          <thead>
            <tr className="bg-gray-200">
              <th className="border p-2">Nom produit</th>
              <th className="border p-2">Quantité totale</th>
              <th className="border p-2">Type</th>
            </tr>
          </thead>
          <tbody>
            {produits.map((p, index) => (
              <tr key={index} className="text-center">
                <td className="border p-2">{p.nomProduit}</td>
                <td className="border p-2">{p.totalQuantite}</td>
                <td className="border p-2">{p.typeProduit}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default ResultatsSession;
