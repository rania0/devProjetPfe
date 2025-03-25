import { useEffect, useState } from "react";
import { getUsers, deleteUser, filterUsers } from "../api"; 
import { useNavigate } from "react-router-dom";
import "./dashboard.css";

function Dashboard() {
    const [users, setUsers] = useState([]);
    const [filterRole, setFilterRole] = useState("");
    

    const navigate = useNavigate();

    // 🛠️ Charger les utilisateurs au début
    useEffect(() => {
        const fetchUsers = async () => {
            const data = await getUsers();
            setUsers(data);
        };
        fetchUsers();
    }, []);

    // ✅ Fonction pour gérer la suppression
    const handleDelete = async (id) => {
        const confirmDelete = window.confirm("❗Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
        if (!confirmDelete) return;

        try {
            await deleteUser(id);
            alert("✅ Utilisateur supprimé avec succès !");
            setUsers(users.filter(user => user.idU !== id)); // Met à jour l'affichage
        } catch (error) {
            alert("❌ Erreur lors de la suppression !");
        }
    };

    // ✅ Fonction pour appliquer les filtres
    const filteredUsers = filterRole ? users.filter(user => user.role === filterRole) : users;

    
    const getInitials = (nom = "", prenom = "") => {
        return `${nom.charAt(0).toUpperCase()}${prenom.charAt(0).toUpperCase()}`;
    };
    const generateColor = (nom, prenom) => {
        const colors = ["#e9d35c", "#FF69B4", "#8A2BE2", "#00CED1", "#FF4500"];
        const index = (nom.charCodeAt(0) +  prenom.charCodeAt(0)) % colors.length;
        return colors[index];
    };
    
    

    return (
        <div className="dashboard-container">
            <div className="header">
                <h2>Tout Les utilisateurs ({users.length})</h2>
                <button className="add-user-btn" onClick={() => navigate("/admin/ajouter-utilisateur")}>➕ Ajouter un utilisateur</button>
            </div>

            <div className="filters">
    <button 
        className={`filter-btn ${filterRole === "" ? "active" : ""}`} 
        onClick={() => setFilterRole("")}
    >
        Voir Tout
    </button>
    {["magasinier", "livreur", "fournisseur", "responsable_point_vente"].map(role => (
        <button 
            key={role} 
            className={`filter-btn ${filterRole === role ? "active" : ""}`} 
            onClick={() => setFilterRole(role)}
        >
            {role.charAt(0).toUpperCase() + role.slice(1).replace("_", " ")}
        </button>
    ))}
</div>


            <table className="user-table">
                <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Email</th>
                        <th>Rôle</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredUsers.map((user) => (
                        <tr key={user.idU}>
                            <td>
                                <div className="user-name">
                                <span className="initials" style={{ backgroundColor: generateColor(user.nom , user.prenom) }}>
    {getInitials(user.nom, user.prenom)}
</span>


                                    {user.nom} {user.prenom}
                                </div>
                            </td>
                            <td>{user.mail}</td>
                            <td className={`role ${user.role}`}>{user.role.replace("_", " ")}</td>
                            <td>
                                <button className="edit-btn" onClick={() => navigate(`/admin/modifier-utilisateur/${user.idU}`)}>✏️ Modifier</button>
                                <button className="delete-btn" onClick={() => handleDelete(user.idU)}>🗑️ Supprimer</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default Dashboard;
