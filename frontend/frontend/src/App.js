import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./auth/login";
import DashboardAdmin from "./admin/Dashboard";
import TestMagasinier from "./magasinier/Testmag";
import TestLivreur from "./livreur/Testliv";
import TestPointVente from "./responsable_point_vente/Testresp";
import TestFournisseur from "./fournisseur/Testfourn";
import Unauthorized from "./Unauthorized";
import ProtectedRoute from "./components/ProtectedRoute";
import AjoutUtilisateur from "./admin/AjoutUtilisateur";
import ModifierUtilisateur from "./admin/ModifierUtilisateur";
import Sidebar from "./components/Sidebar";
import ModifierProfil from "./profile/ModifierProfil";
import ResponsableInscription from "./responsable_point_vente/inscription_responsable/Inscription";
import PageDemandes from "./admin/PageDemandes";

// ✅ Vérifier si l'utilisateur est connecté
const isAuthenticated = () => !!localStorage.getItem("accessToken");

// ✅ Récupérer le rôle de l'utilisateur
const getUserRole = () => localStorage.getItem("userRole");

// ✅ Layout dynamique selon le rôle
const Layout = ({ children }) => {
    return (
        <div style={{ display: "flex" }}>
            <Sidebar /> {/* Sidebar qui s'affiche selon le rôle */}
            <div style={{ marginLeft: "260px", padding: "20px", width: "100%" }}>
                {children}
            </div>
        </div>
    );
};

function App() {
    return (
        <Routes>
            {/* ✅ Empêcher l'accès à /login si connecté */}
            <Route path="/login" element={isAuthenticated() ? <Navigate to={`/${getUserRole()}/dashboard`} /> : <Login />} />
            
            <Route path="/unauthorized" element={<Unauthorized />} />
            <Route path="/inscription-responsable" element={<ResponsableInscription />} />


            {/* ✅ Routes ADMIN protégées avec sidebar */}
            <Route element={<ProtectedRoute allowedRoles={["admin"]} />}>
                <Route
                    path="/admin/*"
                    element={
                        <Layout>
                            <Routes>
                                <Route path="dashboard" element={<DashboardAdmin />} />
                                <Route path="ajouter-utilisateur" element={<AjoutUtilisateur />} />
                                <Route path="modifier-utilisateur/:id" element={<ModifierUtilisateur />} />
                                <Route path="demandes" element={<PageDemandes />} />
                            </Routes>
                        </Layout>
                    }
                />
            </Route>

            {/* ✅ Routes MAGASINIER */}
            <Route element={<ProtectedRoute allowedRoles={["magasinier"]} />}>
                <Route
                    path="/magasinier/*"
                    element={
                        <Layout>
                            <Routes>
                                <Route path="dashboard" element={<TestMagasinier />} />
                            </Routes>
                        </Layout>
                    }
                />
            </Route>

            {/* ✅ Routes LIVREUR */}
            <Route element={<ProtectedRoute allowedRoles={["livreur"]} />}>
                <Route
                    path="/livreur/*"
                    element={
                        <Layout>
                            <Routes>
                                <Route path="dashboard" element={<TestLivreur />} />
                            </Routes>
                        </Layout>
                    }
                />
            </Route>

            {/* ✅ Routes RESPONSABLE POINT DE VENTE */}
            <Route element={<ProtectedRoute allowedRoles={["responsable_point_vente"]} />}>
                <Route
                    path="/point-vente/*"
                    element={
                        <Layout>
                            <Routes>
                                <Route path="dashboard" element={<TestPointVente />} />
                            </Routes>
                        </Layout>
                    }
                />
            </Route>

            {/* ✅ Routes FOURNISSEUR */}
            <Route element={<ProtectedRoute allowedRoles={["fournisseur"]} />}>
                <Route
                    path="/fournisseur/*"
                    element={
                        <Layout>
                            <Routes>
                                <Route path="dashboard" element={<TestFournisseur />} />
                            </Routes>
                        </Layout>
                    }
                />
            </Route>

            {/* ✅ Route pour modifier le profil (accessible à tous) */}
            <Route element={<ProtectedRoute allowedRoles={["admin", "magasinier", "livreur", "responsable_point_vente", "fournisseur"]} />}>
    <Route 
        path="/profil/modifier" 
        element={
            <Layout>
                <ModifierProfil />
            </Layout>
        } 
    />
</Route>

            {/* ✅ Page Not Found */}
            <Route path="*" element={<h1>404 Not Found</h1>} />
        </Routes>
    );
}

export default App;
