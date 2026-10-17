import { useEffect, useState } from "react";
import { api } from "../api.js";

export default function Admin({ notify }) {
  const [stats, setStats] = useState(null);
  const [pets, setPets] = useState([]);
  const [reqs, setReqs] = useState([]);
  const [np, setNp] = useState({ name: "", type: "Dog", breed: "", age: "", gender: "", description: "" });

  const load = () =>
    Promise.all([api.stats(), api.pets(), api.allRequests()])
      .then(([s, p, r]) => { setStats(s); setPets(p); setReqs(r); })
      .catch((e) => notify(e.message));
  useEffect(() => { load(); }, []);

  const run = async (fn, okMsg) => {
    try { await fn(); notify(okMsg); load(); } catch (e) { notify(e.message); }
  };

  const addPet = () => {
    if (!np.name.trim() || !np.breed.trim() || np.age === "") { notify("Fill in name, breed and age"); return; }
    run(() => api.addPet({ ...np, age: Number(np.age) }), np.name + " added")
      .then(() => setNp({ ...np, name: "", breed: "", age: "", description: "" }));
  };

  return (
    <>
      <h2>Admin dashboard</h2>
      {stats && (
        <div className="stats">
          <div className="stat"><b>{stats.totalPets}</b>Total pets</div>
          <div className="stat"><b>{stats.available}</b>Available</div>
          <div className="stat"><b>{stats.placed}</b>Placed</div>
          <div className="stat"><b>{stats.pendingRequests}</b>Pending requests</div>
        </div>
      )}
      <h2>Add a pet</h2>
      <div className="bar">
        <input placeholder="Name" value={np.name} onChange={(e) => setNp({ ...np, name: e.target.value })} />
        <select value={np.type} onChange={(e) => setNp({ ...np, type: e.target.value })}>
          <option>Dog</option><option>Cat</option><option>Rabbit</option>
        </select>
        <input placeholder="Breed" value={np.breed} onChange={(e) => setNp({ ...np, breed: e.target.value })} />
        <input type="number" min="0" placeholder="Age" style={{ width: 80 }} value={np.age}
               onChange={(e) => setNp({ ...np, age: e.target.value })} />
        <button className="btn p" onClick={addPet}>Add pet</button>
      </div>
      <h2>Requests</h2>
      <div className="wrap">
        <table>
          <thead><tr><th>Pet</th><th>User</th><th>Type</th><th>Note</th><th>Status</th><th></th></tr></thead>
          <tbody>
            {reqs.map((r) => (
              <tr key={r.id}>
                <td>{r.petName}</td><td>{r.userName}</td><td>{r.kind}</td><td>{r.note}</td>
                <td className={"st-" + r.status}>{r.status}</td>
                <td>{r.status === "PENDING" && (<>
                  <button className="btn p" onClick={() => run(() => api.approve(r.id), "Request approved")}>Approve</button>{" "}
                  <button className="btn d" onClick={() => run(() => api.reject(r.id), "Request rejected")}>Reject</button>
                </>)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <h2>All pets</h2>
      <div className="wrap">
        <table>
          <thead><tr><th>Name</th><th>Type</th><th>Breed</th><th>Status</th><th></th></tr></thead>
          <tbody>
            {pets.map((p) => (
              <tr key={p.id}>
                <td>{p.name}</td><td>{p.type}</td><td>{p.breed}</td>
                <td className={"st-" + p.status}>{p.status}</td>
                <td><button className="btn d" onClick={() => run(() => api.deletePet(p.id), "Pet deleted")}>Delete</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}
