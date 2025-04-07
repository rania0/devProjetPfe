import { Link, useNavigate } from "react-router-dom";
import "../style/Sidebar.css"; // ✅ Importation du CSS

const Sidebar = () => {
    const userRole = localStorage.getItem("userRole");
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("userRole");
        localStorage.removeItem("userMail");

        navigate("/login"); // 🔥 Redirection vers la page de connexion après déconnexion
    };


    return (
        <div className="sidebar">
            <h2>⚙️ Menu</h2>

            {/* Option pour tout le monde */}
            <Link to="/profil/modifier">Modifier Profil</Link>

            {/* Options spécifiques selon le rôle */}
            {userRole === "admin" && (
                <>
                    <Link to="/admin/dashboard">Dashboard Admin</Link>
                    <Link to="/admin/demandes">Gérer les demandes</Link>

                </>
            )}

{userRole === "magasinier" && (
  <>
    <Link to="/magasinier/dashboard">Dashboard Magasinier</Link>
    <Link to="/magasinier/session/creer">Créer Session de commande</Link>
  </>
)}
            {userRole === "livreur" && <Link to="/livreur/dashboard">Dashboard Livreur</Link>}
            {userRole === "responsable_point_vente" && <Link to="/point-vente/dashboard">Dashboard Point de Vente</Link>}
            {userRole === "fournisseur" && <Link to="/fournisseur/dashboard">Dashboard Fournisseur</Link>}

            {/* 🔥 Bouton Déconnexion */}
            <div className="logout">
                <button onClick={handleLogout}>🚪 Déconnexion</button>
            </div>
        </div>
    );
};

export default Sidebar;
