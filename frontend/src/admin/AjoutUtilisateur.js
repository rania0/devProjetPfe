import { useState, useEffect } from "react";
import { addUser, getUsers, getPointsVente } from "../api"; // Assurez-vous que `getPointsVente` existe bien
import userIcon from "../assets/ajouter.png";
import "./ModifierUtilisateur.css";

const AjoutUtilisateur = ({ setUsers }) => {
    const [formData, setFormData] = useState({
        role: "",
        nom: "",
        prenom: "",
        mail: "",
        cin: "",
        numTel: "",
        numConduit: "",
        ville: "",
        codePostal: "",
        adresse: "",
        dateEmbauche: "",
        dateExpirationContract: "",
        dateSignatureContract: "",
        numIdentificationEntreprise: "",
        numSecuritySocial: "",
        nomSociete: "",
        idPt: "",
        type: "",
    });

    const [pointsVente, setPointsVente] = useState([]);

    // Charger la liste des points de vente au montage du composant
    useEffect(() => {
        const fetchPointsVente = async () => {
            try {
                const data = await getPointsVente();
                setPointsVente(data);
            } catch (error) {
                console.error("❌ Erreur lors de la récupération des points de vente :", error);
            }
        };

        fetchPointsVente();
    }, []);

    const handleChange = (e) => {
        let { name, value } = e.target;
    
        if (name === "idPt") {
            console.log("📌 ID Point de Vente avant conversion :", value);
            value = value ? Number(value) : ""; // Convertir en nombre ou laisser vide
        }
    
        setFormData({ ...formData, [name]: value });
    };
    
   
    

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Nettoyer les champs vides avant l’envoi
        const cleanedData = Object.fromEntries(
            Object.entries(formData).filter(([_, value]) => value !== "")
        );
        console.log("📤 Données envoyées :", cleanedData);
        try {
            await addUser(cleanedData);
            alert("✅ Utilisateur ajouté avec succès !");

            // Vérifier si setUsers est bien défini avant de l'utiliser
            if (setUsers) {
                const updatedUsers = await getUsers();
                setUsers(updatedUsers);
            }

            // Réinitialiser le formulaire après succès
            setFormData({
                role: "", nom: "", prenom: "", mail: "", cin: "", numTel: "",
                numConduit: "", ville: "", codePostal: "", adresse: "", dateEmbauche: "",
                dateExpirationContract: "", dateSignatureContract: "", numIdentificationEntreprise: "",
                numSecuritySocial: "", nomSociete: "", idPt: ""
            });
            
        } catch (error) {
            console.error("❌ Erreur lors de l'ajout de l'utilisateur :", error);
            alert("❌ Erreur lors de l'ajout !");
        }
    };
    const isRole = (r) => formData.role === r;

    // return (
    //     <section className="ftco-section">
    //     <div className="container">
    //       <div className="row justify-content-center">
    //         <div className="col-md-6 text-center mb-5">
    //         </div>
    //     </div>
    //     <div className="row justify-content-center">
    //       <div className="col-md-7 col-lg-5">
    //         <div className="login-wrap p-4 p-md-5">
    //           <div className="icon d-flex align-items-center justify-content-center">
    //             <img src={userIcon} alt="User Icon" className="user-icon" />
    //           </div>
    //           <h3 className="text-center mb-4">Ajouter un utilisateur</h3>
    //         <form className="login-form" onSubmit={handleSubmit}>
            
    //             {/* Rôle */}
    //             <div className="form-group selectt">
    //             <label>Rôle:</label>
    //             <select name="role" className="form-control select  rounded-left" value={formData.role} onChange={handleChange} required style={{ color: "#B22222", fontSize: "16px" }} >
    //                 <option value="">- Sélectionner un rôle -</option>
    //                 <option value="magasinier">Magasinier</option>
    //                 <option value="livreur">Livreur</option>
    //                 <option value="centre_regional">Centre Régional</option>
    //                 <option value="fournisseur">Fournisseur</option> 
    //             </select> 
    //             </div>
    //             <div className="container-fields">

    //             {/* Champs communs */}
    //             {formData.role && (
    //                 <><div className="form-group">
    //                     <label>Nom :</label>
    //                     <input type="text" className="form-control rounded-left" name="nom" value={formData.nom} onChange={handleChange} required />
    //                     </div>

    //                     <div className="form-group">
    //                     <label>Prénom :</label>
    //                     <input type="text" className="form-control rounded-left" name="prenom" value={formData.prenom} onChange={handleChange} required />
    //                     </div>

    //                     <div className="form-group">
    //                     <label>Email :</label>
    //                     <input type="email" className="form-control rounded-left" name="mail" value={formData.mail} onChange={handleChange} required />
    //                     </div>

    //                     <div className="form-group">
    //                     <label>Numéro de téléphone :</label>
    //                     <input type="text" className="form-control rounded-left" name="numTel" value={formData.numTel} onChange={handleChange} required />
    //                     </div>
    //                 </>
    //             )}

    //             {/* Champs spécifiques selon le rôle */}
    //             {(formData.role === "magasinier" || formData.role === "livreur" || formData.role === "responsable_point_vente" || formData.role === "fournisseur") && (
    //                 <><div className="form-group">
    //                     <label>CIN :</label>
    //                     <input type="text" className="form-control rounded-left" name="cin" value={formData.cin} onChange={handleChange} required />
    //                     </div>
    //                 </>
    //             )}

    //             {formData.role === "livreur" && (
    //                 <><div className="form-group">
    //                     <label>Numéro de permis :</label>
    //                     <input type="text" className="form-control rounded-left" name="numConduit" value={formData.numConduit} onChange={handleChange} required />
    //                     <label>Type :</label>
    //     <select
    //         className="form-control rounded-left"
    //         name="type"
    //         value={formData.type}
    //         onChange={handleChange}
    //         required
    //     >
    //         <option value="">-- Sélectionner un type --</option>
    //         <option value="livreur">Livreur</option>
    //         <option value="distributeur">Distributeur</option>
    //     </select>
    //                     </div>
    //                 </>
    //             )}

    //             {(formData.role === "responsable_point_vente" || formData.role === "fournisseur") && (
    //                 <><div className="form-group">
    //                     <label>Ville :</label>
    //                     <input type="text" className="form-control rounded-left" name="ville" value={formData.ville} onChange={handleChange} required />
    //                     </div>
    //                 </>
    //             )}

    //             {/* Champs affichés pour tous sauf "admin" */}
    //             {formData.role && (
    //                 <><div className="form-group">
    //                     <label>Code Postal :</label>
    //                     <input type="text" className="form-control rounded-left" name="codePostal" value={formData.codePostal} onChange={handleChange} />
    //                     </div>

    //                     <div className="form-group">
    //                     <label>Adresse :</label>
    //                     <input type="text" className="form-control rounded-left" name="adresse" value={formData.adresse} onChange={handleChange} />
    //                     </div>

    //                     <div className="form-group">
    //                     <label>Date d'embauche :</label>
    //                     <input type="date" className="form-control rounded-left" name="dateEmbauche" value={formData.dateEmbauche} onChange={handleChange} />
    //                     </div>

    //                     <div className="form-group">
    //                     <label>Date d'expiration du contrat :</label>
    //                     <input type="date" className="form-control rounded-left" name="dateExpirationContract" value={formData.dateExpirationContract} onChange={handleChange} />
    //                     </div>

    //                     <div className="form-group">
    //                     <label>Date de signature du contrat :</label>
    //                     <input type="date" className="form-control rounded-left" name="dateSignatureContract" value={formData.dateSignatureContract} onChange={handleChange} />
    //                     </div>
    //                 </>
    //             )}

    //             {/* Champs spécifiques pour "fournisseur" */}
    //             {formData.role === "fournisseur" && (
    //                 <><div className="form-group">
    //                     <label>Numéro d'identification entreprise :</label>
    //                     <input type="text" className="form-control rounded-left" name="numIdentificationEntreprise" value={formData.numIdentificationEntreprise} onChange={handleChange} />
    //                     </div>

    //                     <div className="form-group">
    //                     <label>Nom de la société :</label>
    //                     <input type="text" className="form-control rounded-left" name="nomSociete" value={formData.nomSociete} onChange={handleChange} />
    //                     <label>Type :</label>
    // <select
    //     className="form-control rounded-left"
    //     name="type"
    //     value={formData.type}
    //     onChange={handleChange}
    //     required
    // >
    //     <option value="">-- Sélectionner un type --</option>
    //     <option value="fournisseur papier thermique">Fournisseur papier thermique</option>
    //     <option value="imprimeur">Imprimeur</option>
    // </select>
    //                     </div>
    //                 </>
    //             )}


    //             <div className="form-group">
    //             {/* Sélection du point de vente (Admin, Magasinier, Livreur) */}
    //             {(formData.role === "responsable_point_vente" ) && (
    //                 <>
    //                     <label>Point de Vente :</label>
                    
    //                 <select name="idPt" className="form-control select rounded-left" value={formData.idPt} onChange={handleChange}>
    //                     <option value="">-- Sélectionner un point de vente --</option>
    //                     {pointsVente.map((pt) => (
    //                         <option key={pt.idP} value={pt.idP}>
    //                             {pt.nomP} {/* Vérifie bien les clés utilisées ici */}
    //                         </option>
    //                     ))}
    //                 </select>
    //                 </>
    //             )}
    //             </div>
    //             </div>


    //             <div className="form-group">
    //             <button type="submit" className="form-control btn btn-primary rounded submit px-3">Ajouter</button>
    //             </div>
    //         </form>
    //         </div>
    //       </div>
    //     </div>
    //   </div>
    //         </section>
    // );
    return (
        <form onSubmit={handleSubmit}>
            <select name="role" value={formData.role} onChange={handleChange} required>
                <option value="">-- Choisir rôle --</option>
                <option value="magasinier">Magasinier</option>
                <option value="livreur">Livreur</option>
                <option value="responsable_point_vente">Responsable Point de Vente</option>
                <option value="fournisseur">Fournisseur</option>
                <option value="centre_regional">Centre Régional</option>
            </select>

            {formData.role && (
                <>
                    {(isRole("fournisseur") || isRole("centre_regional")) && (
                        <>
                            <input name="nomSociete" placeholder="Nom Société" value={formData.nomSociete} onChange={handleChange} />
                            <input name="numIdentificationEntreprise" placeholder="Num ID Entreprise" value={formData.numIdentificationEntreprise} onChange={handleChange} />
                        </>
                    )}
                    {(isRole("fournisseur") || isRole("livreur")) && (
                        <>
                            <input type="date" name="dateSignatureContract" value={formData.dateSignatureContract} onChange={handleChange} />
                            <input type="date" name="dateExpirationContract" value={formData.dateExpirationContract} onChange={handleChange} />
                        </>
                    )}
                    <input name="nom" placeholder="Nom" value={formData.nom} onChange={handleChange} />
                    <input name="prenom" placeholder="Prénom" value={formData.prenom} onChange={handleChange} />
                    {(isRole("livreur") || isRole("magasinier") || isRole("fournisseur") || isRole("responsable_point_vente")) && (
                        <input name="cin" placeholder="CIN" value={formData.cin} onChange={handleChange} />
                    )}
                    <input name="numTel" placeholder="Téléphone" value={formData.numTel} onChange={handleChange} />
                    {isRole("livreur") && (
                        <input name="numConduit" placeholder="Numéro de permis" value={formData.numConduit} onChange={handleChange} />
                    )}
                    {(isRole("fournisseur") || isRole("centre_regional") || isRole("livreur") || isRole("magasinier") || isRole("responsable_point_vente")) && (
                        <>
                            <input name="mail" placeholder="Email" value={formData.mail} onChange={handleChange} />
                            <input name="adresse" placeholder="Adresse" value={formData.adresse} onChange={handleChange} />
                            <input name="ville" placeholder="Ville" value={formData.ville} onChange={handleChange} />
                            <input name="codePostal" placeholder="Code Postal" value={formData.codePostal} onChange={handleChange} />
                            <input type="date" name="dateEmbauche" value={formData.dateEmbauche} onChange={handleChange} />
                        </>
                    )}
                    {isRole("magasinier") && (
                        <input name="numSecuritySocial" placeholder="Num Sécurité Sociale" value={formData.numSecuritySocial} onChange={handleChange} />
                    )}
                    {(isRole("livreur") || isRole("fournisseur")) && (
                        <select name="type" value={formData.type} onChange={handleChange}>
                            <option value="">-- Choisir type --</option>
                            {isRole("livreur") && <>
                                <option value="livreur">Livreur</option>
                                <option value="distributeur">Distributeur</option>
                            </>}
                            {isRole("fournisseur") && <>
                                <option value="fournisseur papier thermique">Fournisseur papier thermique</option>
                                <option value="imprimeur">Imprimeur</option>
                            </>}
                        </select>
                    )}
                </>
            )}

            <button type="submit">Ajouter</button>
        </form>
    );
};

export default AjoutUtilisateur;
