import { useEffect, useState } from "react";
import { api } from "../api.js";

const EMOJI = { Dog: "🐕", Cat: "🐈", Rabbit: "🐇" };
const COLORS = ["#f4d9a8", "#cfe3f4", "#e6d3f1", "#cdebd9", "#f6d0d0"];

export default function Pets({ user, notify, goLogin }) {
  const [pets, setPets] = useState([]);
  const [q, setQ] = useState("");
  const [type, setType] = useState("");
  const [status, setStatus] = useState("");
  const [error, setError] = useState("");
  const [asking, setAsking] = useState(null); // { pet, kind }
  const [note, setNote] = useState("");

  useEffect(() => {
    api.pets(q, type, status).then((d) => { setPets(d); setError(""); }).catch((e) => setError(e.message));
  }, [q, type, status]);

  const ask = (pet, kind) => {
    if (!user) { notify("Log in first to send a request"); goLogin(); return; }
    if (user.role !== "ADOPTER") { notify("Admins cannot send requests. Log in as an adopter."); return; }
    setNote(""); setAsking({ pet, kind });
  };

  const send = async () => {
    if (!note.trim()) { notify("Add a short note before sending"); return; }
    try {
      await api.sendRequest({ petId: asking.pet.id, kind: asking.kind, note });
      notify("Request sent"); setAsking(null);
    } catch (e) { notify(e.message); }
  };

  return (
    <>
      <div className="hero">
        <h1>Find a pet who needs a home</h1>
        <p>Adopt for good or foster for a while. Browse the pets below and send a request in one tap.</p>
      </div>
      <div className="bar">
        <input placeholder="Search name or breed" value={q} onChange={(e) => setQ(e.target.value)} />
        <select value={type} onChange={(e) => setType(e.target.value)}>
          <option value="">All types</option><option>Dog</option><option>Cat</option><option>Rabbit</option>
        </select>
        <select value={status} onChange={(e) => setStatus(e.target.value)}>
          <option value="">Any status</option><option value="AVAILABLE">Available</option>
          <option value="FOSTERED">Fostered</option><option value="ADOPTED">Adopted</option>
        </select>
      </div>
      {error && <div className="empty">{error}</div>}
      {!error && pets.length === 0 && <div className="empty">No pets match. Clear a filter to see more.</div>}
      <div className="grid">
        {pets.map((p, i) => (
          <div className="card" key={p.id}>
            <div className="pic" style={{ background: COLORS[i % COLORS.length] }}>{EMOJI[p.type] || "🐾"}</div>
            <div className="body">
              <h3>{p.name}</h3>
              <div className="meta">{p.breed}, {p.age} yr{p.gender ? ", " + p.gender : ""}</div>
              <span className="tag">{p.type}</span>
              <span className={"tag st-" + p.status}>{p.status}</span>
              <p className="desc">{p.description}</p>
              {p.status === "AVAILABLE" ? (
                <div>
                  <button className="btn p" onClick={() => ask(p, "ADOPT")}>Adopt</button>{" "}
                  <button className="btn s" onClick={() => ask(p, "FOSTER")}>Foster</button>
                </div>
              ) : <span className="note">Not available</span>}
            </div>
          </div>
        ))}
      </div>
      {asking && (
        <div className="modal" onClick={(e) => e.target === e.currentTarget && setAsking(null)}>
          <div className="box">
            <h2>{asking.kind === "ADOPT" ? "Adopt" : "Foster"} {asking.pet.name}</h2>
            <label>Why are you a good match?
              <input value={note} onChange={(e) => setNote(e.target.value)} placeholder="e.g. Home with a yard" />
            </label>
            <button className="btn p" onClick={send}>Send request</button>
            <button className="btn" onClick={() => setAsking(null)}>Cancel</button>
          </div>
        </div>
      )}
    </>
  );
}
