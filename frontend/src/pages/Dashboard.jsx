import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Dashboard() {

    const navigate = useNavigate();

    const [user, setUser] = useState(null);

    useEffect(() => {

        axios.get(
            "http://localhost:8080/api/auth/me",
            {
                withCredentials: true
            }
        )
            .then((response) => {

                setUser(response.data);

            })
            .catch(() => {

                navigate("/login");

            });

    }, [navigate]);

    const handleLogout = async () => {
        try {
            await axios.post(
                "http://localhost:8080/api/auth/logout",
                {},
                {
                    withCredentials: true
                }
            );

            navigate("/login");

        } catch (error) {
            console.error("Logout failed:", error);
        }
    };

    if (!user) {
        return <h2>Loading...</h2>;
    }

    return (

        <div className="dashboard">

            <nav className="navbar">

                <h2>My Dashboard</h2>
                <button onClick={handleLogout}>
                    Logout
                </button>

            </nav>

            <main className="dashboard-content">

                <h1>
                    Welcome, {user.name}! 👋
                </h1>

                <p>
                    You have successfully logged in.
                </p>

                <div className="dashboard-card">

                    <h2>User Information</h2>

                    <p>
                        <strong>User ID:</strong>{" "}
                        {user.id}
                    </p>

                    <p>
                        <strong>Name:</strong>{" "}
                        {user.name}
                    </p>

                </div>

            </main>

        </div>
    );
}

export default Dashboard;