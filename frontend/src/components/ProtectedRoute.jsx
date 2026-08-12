import { useEffect, useState } from "react";
import axios from "axios";
import { Navigate } from "react-router-dom";

function ProtectedRoute({ children }) {

  const [loading, setLoading] = useState(true);
  const [authenticated, setAuthenticated] =
    useState(false);

  useEffect(() => {

    axios.get(
      "http://localhost:8080/api/auth/me",
      {
        withCredentials: true
      }
    )
    .then(() => {
      setAuthenticated(true);
    })
    .catch(() => {
      setAuthenticated(false);
    })
    .finally(() => {
      setLoading(false);
    });

  }, []);

  if (loading) {
    return <h2>Loading...</h2>;
  }

  if (!authenticated) {
    return <Navigate to="/login" />;
  }

  return children;
}

export default ProtectedRoute;