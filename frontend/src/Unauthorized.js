const Unauthorized = () => {
    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>🚫 Accès Refusé !</h1>
            <p>Vous n'avez pas l'autorisation d'accéder à cette page.</p>
            <a href="/login">🔄 Retour à la page de connexion</a>
        </div>
    );
};

export default Unauthorized;
