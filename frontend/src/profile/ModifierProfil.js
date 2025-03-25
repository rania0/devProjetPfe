import { useState, useEffect } from "react";
import { updatePassword } from "../api";
import { useNavigate } from "react-router-dom";
import userIcon from "../assets/cloud-computing.png";
import "../auth/Login.css";

const ModifierProfil = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
    });

    const [userMail, setUserMail] = useState(null);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    // ✅ Récupérer l'email utilisateur après le rendu
    useEffect(() => {
        const storedMail = localStorage.getItem("userMail");
        console.log("📌 Email récupéré depuis localStorage :", storedMail);
        setUserMail(storedMail);
    }, []);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setSuccess("");

        // ✅ Vérifier si l'email est bien présent
        if (!userMail) {
            setError("❌ Erreur : Email utilisateur non trouvé !");
            return;
        }

        if (formData.newPassword !== formData.confirmPassword) {
            setError("❌ Les nouveaux mots de passe ne correspondent pas !");
            return;
        }

        try {
            await updatePassword({
                mail: userMail,
                oldPassword: formData.oldPassword,
                newPassword: formData.newPassword,
            });
            setSuccess("✅ Mot de passe mis à jour avec succès !");
            
            setTimeout(() => {
                localStorage.clear();
                navigate("/login");
            }, 2000);
        } catch (err) {
            setError("❌ Erreur lors de la mise à jour du mot de passe !");
        }
    };

    return (
  
<section className="ftco-section">
      <div className="container">
            
      <div className="row justify-content-center">
          <div className="col-md-7 col-lg-5">
            <div className="loginn-wrap p-4 p-md-5">
              <div className="icon d-flex align-items-center justify-content-center">
                <img src={userIcon} alt="User Icon" className="user-icon" />
              </div>
              <h3 className="text-center mb-4">modifier mon mot de passe</h3>

            <form className="login-form" onSubmit={handleSubmit}>
            <div className="form-group">
                <input type="password" className="form-controll rounded-left"  placeholder="Mot de passe actuel" name="oldPassword" value={formData.oldPassword} onChange={handleChange} required />
                </div>
                <div className="form-group">
                <input type="password" className="form-controll rounded-left" placeholder="Nouveau mot de passe" name="newPassword" value={formData.newPassword} onChange={handleChange} required />
                </div>
                <div className="form-group">
                <input type="password" className="form-controll rounded-left" placeholder="Confirmer le nouveau mot de passe" name="confirmPassword" value={formData.confirmPassword} onChange={handleChange} required />
                </div>
                <div className="form-group">
                <button className="form-control btn btn-primary rounded submit px-3" type="submit">Mettre à jour</button>
                </div>
            </form>

            {error && <p style={{ color: "red" }}>❌ {error}</p>}
            {success && <p style={{ color: "green" }}>✅ {success}</p>}
        </div>
        </div>
      </div>
      </div>
    </section>
    );
};

export default ModifierProfil;
