import { MdLogout } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import { logout } from "../../Services/authService";

const Settings = () => {
    
    const navigate = useNavigate();

    const handleLogOut = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
          return;
        }
        await logout(token);
        navigate("/login");
    }

    return (
        <div>
            <div>
                <button onClick={handleLogOut}>
                    Log Out {<MdLogout />}
                </button>
            </div>
        </div>
    );
}

export default Settings;