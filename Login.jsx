import { useState } from "react";
import { api } from "../api.js";

export default function Login({ onSignIn, notify }) {
  const [register, setRegister] = useState(false);
  const [f, setF] = useState({ name: "", email: "", password: "", phone: "", city: "" });
  const set = (k) => (e) => setF({ ...f, [k]: e.target.value });

  const submit = async () => {
    try {
      const data = register ? await api.register(f) : await api.login({ email: f.email, password: f.password });
      onSignIn(data);
    } catch (e) { notify(e.message); }
  };

  return (
    <div className="box center">
      <h2>{register ? "Create an account" : "Log in"}</h2>
      {register && <label>Name<input value={f.name} onChange={set("name")} /></label>}
      <label>Email<input type="email" value={f.email} onChange={set("email")} autoComplete="username" /></label>
      <label>Password<input type="password" value={f.password} onChange={set("password")} autoComplete="current-password" /></label>
      {register && <label>Phone (optional)<input value={f.phone} onChange={set("phone")} /></label>}
      {register && <label>City (optional)<input value={f.city} onChange={set("city")} /></label>}
      <button className="btn p" onClick={submit}>{register ? "Register" : "Log in"}</button>
      <button className="btn" onClick={() => setRegister(!register)}>
        {register ? "I already have an account" : "New here? Register"}
      </button>
      {!register && <p className="note">Demo: admin@pawhome.com / admin123 (admin), riya@pawhome.com / riya123 (adopter)</p>}
    </div>
  );
}
