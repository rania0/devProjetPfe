import axios from "axios";

const API_URL = "http://localhost:9090/api";

const api = axios.create({
    baseURL: API_URL,
    headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        if (error.response.status === 403) {
            try {
                const refreshToken = localStorage.getItem("refreshToken");
                const response = await axios.post(`${API_URL}/auth/refresh`, { refreshToken });

                localStorage.setItem("accessToken", response.data.accessToken);
                error.config.headers.Authorization = `Bearer ${response.data.accessToken}`;


                return axios(error.config);
            } catch (err) {
                console.error("Refresh token invalide, déconnexion...");
                localStorage.clear();
                window.location.href = "/login";
            }
        }
        return Promise.reject(error);
    }
);
api.interceptors.response.use(
    (response) => response,
    async (error) => {
        if (error.response.status === 403) {
            try {
                const refreshToken = localStorage.getItem("refreshToken");
                if (!refreshToken) {
                    throw new Error("Pas de refresh token !");
                }

                const response = await axios.post("http://localhost:9090/api/auth/refresh", { refreshToken });
                
                localStorage.setItem("accessToken", response.data.accessToken);
                error.config.headers.Authorization = `Bearer ${response.data.accessToken}`;

                return axios(error.config); // Réessayer la requête avec le nouveau token
            } catch (err) {
                console.error("⚠️ Refresh token invalide, déconnexion...");
                localStorage.clear();
                window.location.href = "/login";
            }
        }
        return Promise.reject(error);
    }
);
export const updatePassword = async (passwordData) => {
    console.log("📌 Données envoyées pour update password :", passwordData);
    try {
        const response = await api.post("/auth/update-password", passwordData);
        return response.data;
    } catch (error) {
        console.error("❌ Erreur lors de la mise à jour du mot de passe", error);
        throw error;
    }
};



export const getUsers = async () => {
    try {
        const response = await api.get("/admin/liste");
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la récupération des utilisateurs", error);
        return [];
    }
};
export const addUser = async (userData) => {
    console.log("🛠️ Envoi de l'utilisateur :", userData); // Vérifie bien ce qui est envoyé
    try {
        const response = await api.post("/admin/ajouter", userData);
        return response.data;
    } catch (error) {
        console.error("❌ Erreur lors de l'ajout de l'utilisateur", error);
        throw error;
    }
};

export const getPointsVente = async () => {
    try {
        const response = await api.get("/point_vente/all");
        console.log("✅ Points de vente récupérés :", response.data);
        return response.data;
    } catch (error) {
        console.error("❌ Erreur lors de la récupération des points de vente", error);
        return [];
    }
};

export const deleteUser = async (id) => {
    try {
        const response = await api.delete(`/admin/supprimer/${id}`);
        return response.data;
    } catch (error) {
        console.error("❌ Erreur lors de la suppression de l'utilisateur", error);
        throw error;
    }
};
export const getUserById = async (id) => {
    try {
        const response = await api.get(`/admin/utilisateur/${id}`);
        console.log("✅ Données récupérées :", response.data); // Debug
        return response.data;
    } catch (error) {
        console.error("❌ Erreur lors de la récupération de l'utilisateur", error);
        throw error;
    }
};

export const filterUsers = async (filters) => {
    try {
        const response = await api.get("/admin/utilisateurs/filter", { params: filters });
        return response.data;
    } catch (error) {
        console.error("❌ Erreur lors du filtrage des utilisateurs", error);
        return [];
    }
};


    export const updateUser = async (id, userData) => {
        const token = localStorage.getItem("accessToken"); // ✅ Récupère le token
        console.log("🛠️ Token envoyé dans l'update :", token);
    
        try {
            const response = await api.put(`/admin/update/${id}`, userData);
            return response.data;
        } catch (error) {
            console.error("❌ Erreur lors de la modification de l'utilisateur", error);
            throw error;
        }
    };
    

    export const logout = async () => {
        try {
            const token = localStorage.getItem("accessToken");
            await api.post("/auth/logout", {}, {
                headers: { Authorization: `Bearer ${token}` }
            });
        } catch (error) {
            console.error("❌ Erreur lors de la déconnexion :", error);
        } finally {
            // ✅ Nettoyage des données en localStorage
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            localStorage.removeItem("userRole");
            localStorage.removeItem("userMail");
    
            window.location.href = "/login"; // ✅ Redirection vers la page de connexion
        }
    };
    



export default api;
