import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./auth/login";
import Dashboard from "./admin/Dashboard";

function AppRoutes() {
    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/admin/dashboard" element={<Dashboard />} />
            </Routes>
        </Router>
    );
}

export default AppRoutes;
