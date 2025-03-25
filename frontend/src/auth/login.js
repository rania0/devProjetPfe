import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import userIcon from "../assets/user.png";
import "./Login.css"; // ✅ Import du fichier CSS global

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post("/auth/login", { mail: email, password });

            localStorage.setItem("accessToken", response.data.token);
            localStorage.setItem("refreshToken", response.data.refreshToken);
            localStorage.setItem("userRole", response.data.role);
            localStorage.setItem("userMail", response.data.mail);

            switch (response.data.role) {
                case "admin":
                    navigate("/admin/dashboard");
                    break;
                case "magasinier":
                    navigate("/magasinier/testmag");
                    break;
                case "livreur":
                    navigate("/livreur/testliv");
                    break;
                case "responsable_point_vente":
                    navigate("/point-vente/testresp");
                    break;
                case "fournisseur":
                    navigate("/fournisseur/testfor");
                    break;
                default:
                    navigate("/unauthorized");
            }
        } catch (error) {
            alert("❌ Erreur d'authentification !");
        }
    };

    return (
        <section className="ftco-section">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-md-6 text-center mb-5">
            </div>
        </div>
        <div className="row justify-content-center">
          <div className="col-md-7 col-lg-5">
            <div className="loginn-wrap p-4 p-md-5">
              <div className="icon d-flex align-items-center justify-content-center">
                <img src={userIcon} alt="User Icon" className="user-icon" />
              </div>
              <h3 className="text-center mb-4">Connexion</h3>

            <form className="login-form" onSubmit={handleLogin}>
            <div className="form-group">
                <input
                    type="email"
                    className="form-controll rounded-left"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                </div>
                <div className="form-group">
                <input
                    type="password"
                    placeholder="Mot de passe"
                    className="form-controll rounded-left"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                </div>
                <div className="form-group">
                <button type="submit" className="form-control btn btn-primary rounded submit px-3">Se connecter</button>
                </div>
            </form>
            </div>
          </div>
        </div>
      </div>
            </section>
    );
};

export default Login;
