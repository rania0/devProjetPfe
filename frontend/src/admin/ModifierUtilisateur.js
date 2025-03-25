// import { useEffect, useState } from "react";
// import { useParams, useNavigate } from "react-router-dom";
// import { getUserById, updateUser, getPointsVente } from "../api";
// // import "../auth/Login.css";
// import "./ModifierUtilisateur.css";
// import userIcon from "../assets/edit.png";

// const ModifierUtilisateur = () => {
//     const { id } = useParams();
//     const navigate = useNavigate();
//     const [formData, setFormData] = useState({});
//     const [pointsVente, setPointsVente] = useState([]);

//     useEffect(() => {
//         const fetchUserData = async () => {
//             try {
//                 const user = await getUserById(id);
//                 console.log("📌 Utilisateur récupéré :", user);
                
//                 // Filtrer uniquement à l'initialisation (on ne touche plus après)
//                 const initialData = Object.fromEntries(
//                     Object.entries(user).filter(([_, value]) => value !== null)
//                 );

//                 setFormData(initialData);
//             } catch (error) {
//                 console.error("❌ Erreur lors de la récupération de l'utilisateur :", error);
//             }
//         };

//         const fetchPointsVente = async () => {
//             try {
//                 const data = await getPointsVente();
//                 setPointsVente(data);
//             } catch (error) {
//                 console.error("❌ Erreur lors de la récupération des points de vente :", error);
//             }
//         };

//         fetchUserData();
//         fetchPointsVente();
//     }, [id]);

//     const handleChange = (e) => {
//         const { name, value } = e.target;

//         setFormData((prevData) => ({
//             ...prevData,
//             [name]: value, // On garde la clé même si le champ est vide
//         }));
//     };

//     const handleSubmit = async (e) => {
//         e.preventDefault();
//         try {
//             await updateUser(id, formData);
//             alert("✅ Utilisateur modifié avec succès !");
//             navigate("/admin/dashboard");
//         } catch (error) {
//             alert("❌ Erreur lors de la modification !");
//         }
//     };

//     return (
//         <section className="ftco-section">
//         <div className="container">
//           <div className="row justify-content-center">
//             <div className="col-md-6 text-center mb-5">
//             </div>
//         </div>
//         <div className="row justify-content-center">
//           <div className="col-md-7 col-lg-5">
//             <div className="login-wrap p-4 p-md-5">
//               <div className="icon d-flex align-items-center justify-content-center">
//                 <img src={userIcon} alt="User Icon" className="user-icon" />
//               </div>
//               <h3 className="text-center mb-4">Modifier les informations d'un utilisateur</h3>
//             <form className="login-form" onSubmit={handleSubmit}>
//             <div className="container-fields">
//                 {/* Champ rôle (lecture seule) */}
//                 <div className="form-group"> 
//                 <label>Rôle :</label>
//                 <input type="text" className="form-control  rounded-left" name="role" value={formData.role || ""} readOnly style={{ color: "#B22222", fontWeight: "bold" }} />
//                 </div>
        

//                 <div className="form-group">
//                 {/* Afficher seulement les champs non null */}
//                 {"nom" in formData && (
//                     <>
//                         <label>Nom :</label>
//                         <input type="text" className="form-control rounded-left" name="nom" value={formData.nom} onChange={handleChange} required />
//                     </>
//                 )}
//                 </div>


//                 <div className="form-group">
//                 {"prenom" in formData && (
//                     <>
//                         <label>Prénom :</label>
//                         <input type="text" className="form-control rounded-left" name="prenom" value={formData.prenom} onChange={handleChange} required />
//                     </>
//                 )}</div>



//                <div className="form-group">
//                 {"mail" in formData && (
//                     <>
//                         <label>Email :</label>
//                         <input type="email" className="form-control rounded-left" name="mail" value={formData.mail} onChange={handleChange} required />
//                     </>
//                 )}</div>



//                 <div className="form-group">
//                 {"numTel" in formData && (
//                     <>
//                         <label>Numéro de téléphone :</label>
//                         <input type="text" className="form-control rounded-left" name="numTel" value={formData.numTel} onChange={handleChange} required />
//                     </>
//                 )}
//                 </div>


                
//                 <div className="form-group">
//                 {"cin" in formData && (
//                     <>
//                         <label>CIN :</label>
//                         <input type="text" className="form-control rounded-left" name="cin" value={formData.cin} onChange={handleChange} required />
//                     </>
//                 )}</div>




//                 <div className="form-group">
//                 {"numConduit" in formData && (
//                     <>
//                         <label>Numéro de permis :</label>
//                         <input type="text" className="form-control rounded-left" name="numConduit" value={formData.numConduit} onChange={handleChange} />
//                     </>
//                 )}</div> 
                
                
                
//                 <div className="form-group">
//                 {"dateEmbauche" in formData && (
//                     <>
//                         <label>Date d'embauche :</label>
//                         <input type="date" className="form-control rounded-left" name="dateEmbauche" value={formData.dateEmbauche?.substring(0, 10)} onChange={handleChange} />
//                     </>
//                 )}</div>



//                 <div className="form-group">
//                 {"ville" in formData && (
//                     <>
//                         <label>Ville :</label>
//                         <input type="text" className="form-control rounded-left" name="ville" value={formData.ville} onChange={handleChange} />
//                     </>
//                 )}</div>



//                 <div className="form-group">
//                 {"codePostal" in formData && (
//                     <>
//                         <label>Code Postal :</label>
//                         <input type="text" className="form-control rounded-left" name="codePostal" value={formData.codePostal} onChange={handleChange} />
//                     </>
//                 )}</div>




//                <div className="form-group">
//                 {"adresse" in formData && (
//                     <>
//                         <label>Adresse :</label>
//                         <input type="text" className="form-control rounded-left" name="adresse" value={formData.adresse} onChange={handleChange} />
//                     </>
//                 )}</div>



             





//                <div className="form-group">
//                 {"dateExpirationContract" in formData && (
//                     <>
//                         <label>Date d'expiration du contrat :</label>
//                         <input type="date" className="form-control rounded-left" name="dateExpirationContract" value={formData.dateExpirationContract?.substring(0, 10)} onChange={handleChange} />
//                     </>
//                 )}</div>





//                <div className="form-group">
//                 {"dateSignatureContract" in formData && (
//                     <>
//                         <label>Date de signature du contrat :</label>
//                         <input type="date" className="form-control rounded-left" name="dateSignatureContract" value={formData.dateSignatureContract?.substring(0, 10)} onChange={handleChange} />
//                     </>
//                 )}</div>




//                 <div className="form-group">
//                 {"numIdentificationEntreprise" in formData && (
//                     <>
//                         <label>Numéro d'identification entreprise :</label>
//                         <input type="text" className="form-control rounded-left" name="numIdentificationEntreprise" value={formData.numIdentificationEntreprise} onChange={handleChange} />
//                     </>
//                 )}</div>



//                <div className="form-group">
//                 {"nomSociete" in formData && (
//                     <>
//                         <label>Nom de la société :</label>
//                         <input type="text" className="form-control rounded-left" name="nomSociete" value={formData.nomSociete} onChange={handleChange} />
//                     </>
//                 )}</div>




//                <div className="form-group">
//                 {/* Sélection du point de vente pour les rôles concernés */}
//                 {formData.role === "responsable_point_vente" && "idPt" in formData && (
//                     <>
//                         <label>Point de Vente :</label>
//                         <select className="form-control select rounded-left" name="idPt" value={formData.idPt} onChange={handleChange}>
//                             <option value="">-- Sélectionner un point de vente --</option>
//                             {pointsVente.map((pt) => (
//                                 <option key={pt.idP} value={pt.idP}>
//                                     {pt.nomP}
//                                 </option>
//                             ))}
//                         </select>
//                     </>
//                 )}</div>
//               </div>

//                 <div className="form-group">
//                 <button type="submit" className="form-control btn btn-primary rounded submit px-3">Modifier</button>
//                 </div>
                
//             </form>
//             </div>
//           </div>
//         </div>
//       </div>
//             </section>
//     );
// };

// export default ModifierUtilisateur;


// 👇🏻 Assure-toi que ce fichier existe : src/profile/ModifierUtilisateur.js
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getUserById, updateUser, getPointsVente } from "../api";
import "./ModifierUtilisateur.css";
import userIcon from "../assets/edit.png";

const ModifierUtilisateur = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({});
    const [pointsVente, setPointsVente] = useState([]);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const user = await getUserById(id);
                const initialData = Object.fromEntries(
                    Object.entries(user).filter(([_, value]) => value !== null)
                );
                setFormData(initialData);
            } catch (error) {
                console.error("❌ Erreur lors de la récupération de l'utilisateur :", error);
            }
        };

        const fetchPointsVente = async () => {
            try {
                const data = await getPointsVente();
                setPointsVente(data);
            } catch (error) {
                console.error("❌ Erreur lors de la récupération des points de vente :", error);
            }
        };

        fetchUserData();
        fetchPointsVente();
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await updateUser(id, formData);
            alert("✅ Utilisateur modifié avec succès !");
            navigate("/admin/dashboard");
        } catch (error) {
            alert("❌ Erreur lors de la modification !");
        }
    };

    const { role } = formData;
    console.log("TYPE (loaded from DB):", formData.type);

    return (
        <section className="ftco-section">
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-7 col-lg-5">
                        <div className="login-wrap p-4 p-md-5">
                            <div className="icon d-flex align-items-center justify-content-center">
                                <img src={userIcon} alt="User Icon" className="user-icon" />
                            </div>
                            <h3 className="text-center mb-4">Modifier les informations</h3>

                            <form className="login-form" onSubmit={handleSubmit}>
                                <div className="form-group">
                                    <label>Rôle :</label>
                                    <input type="text" name="role" value={formData.role || ""} readOnly className="form-control" />
                                </div>

                                {["nom", "prenom", "mail", "cin", "numTel", "ville", "codePostal", "adresse", "numConduit", "numSecuritySocial", "numIdentificationEntreprise", "nomSociete", "type"].map((field) =>
                                    field in formData ? (
                                        <div className="form-group" key={field}>
                                            <label>{field.charAt(0).toUpperCase() + field.slice(1)} :</label>
                                            <input type="text" name={field} value={formData[field]} onChange={handleChange} className="form-control" />
                                        </div>
                                    ) : null
                                )}

                                {["dateSignatureContract", "dateExpirationContract", "dateEmbauche"].map((field) =>
                                    field in formData ? (
                                        <div className="form-group" key={field}>
                                            <label>{field.replace(/([A-Z])/g, " $1").trim()} :</label>
                                            <input type="date" name={field} value={formData[field]?.substring(0, 10)} onChange={handleChange} className="form-control" />
                                        </div>
                                    ) : null
                                )}

                                {role === "responsable_point_vente" && "idPt" in formData && (
                                    <div className="form-group">
                                        <label>Point de Vente :</label>
                                        <select className="form-control" name="idPt" value={formData.idPt} onChange={handleChange}>
                                            <option value="">-- Sélectionner un point de vente --</option>
                                            {pointsVente.map((pt) => (
                                                <option key={pt.idP} value={pt.idP}>
                                                    {pt.nomP}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                )}
                                {/* Champ TYPE conditionnel */}
{(formData.role === "livreur" || formData.role === "fournisseur") && (
  <div className="form-group">
    <label>Type :</label>
    <select
  className="form-control select rounded-left"
  name="type"
  value={formData.type || ""}
  onChange={handleChange}
  required
>
  <option value="">-- Sélectionner un type --</option>

  {formData.role === "livreur" && (
    <>
      <option value="livreur">Livreur</option>
      <option value="distributeur">Distributeur</option>
    </>
  )}

  {formData.role === "fournisseur" && (
    <>
      <option value="fournisseur papier thermique">Fournisseur papier thermique</option>
      <option value="imprimeur">Imprimeur</option>
    </>
  )}
</select>

  </div>
)}


                                <div className="form-group">
                                    <button type="submit" className="form-control btn btn-primary rounded submit px-3">
                                        Modifier
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default ModifierUtilisateur;

