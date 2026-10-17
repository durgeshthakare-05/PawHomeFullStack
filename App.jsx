import { useState } from "react";
import Pets from "./pages/Pets.jsx";
import Login from "./pages/Login.jsx";
import MyRequests from "./pages/MyRequests.jsx";
import Admin from "./pages/Admin.jsx";

export default function App() {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem("user")); } catch { return null; }
  });
  const [page, setPage] = useState("pets");
  const [toast, setToast] = useState("");

  const notify = (msg) => { setToast(msg); setTimeout(() => setToast(""), 2500); };
  const signIn = (data) => {
    localStorage.setItem("token", data.token);
    localStorage.setItem("user", JSON.stringify(data));
    setUser(data);
    setPage(data.role === "ADMIN" ? "admin" : "pets");
    notify("Welcome, " + data.name);
  };
  const signOut = () => {
    localStorage.removeItem("token"); localStorage.removeItem("user");
    setUser(null); setPage("pets"); notify("Logged out");
  };

  const isAdmin = user && user.role === "ADMIN";
  const isAdopter = user && user.role === "ADOPTER";
  // route guards: a page is only shown to the role allowed to see it
  let view;
  if (page === "login") view = <Login onSignIn={signIn} notify={notify} />;
  else if (page === "mine" && isAdopter) view = <MyRequests />;
  else if (page === "admin" && isAdmin) view = <Admin notify={notify} />;
  else view = <Pets user={user} notify={notify} goLogin={() => setPage("login")} />;

  const tab = (id, label) => (
    <button className={page === id ? "on" : ""} onClick={() => setPage(id)}>{label}</button>
  );

  return (
    <>
      <header>
        <div className="logo"><span>Paw</span>Home</div>
        <nav>
          {tab("pets", "Pets")}
          {isAdopter && tab("mine", "My requests")}
          {isAdmin && tab("admin", "Admin")}
          {user ? <button onClick={signOut}>Log out ({user.name.split(" ")[0]})</button> : tab("login", "Log in")}
        </nav>
      </header>
      <main>{view}</main>
      {toast && <div className="toast">{toast}</div>}
    </>
  );
}
