import { useEffect, useState } from "react";
import { api } from "../api.js";

export default function MyRequests() {
  const [rows, setRows] = useState(null);
  const [error, setError] = useState("");
  useEffect(() => { api.myRequests().then(setRows).catch((e) => setError(e.message)); }, []);

  if (error) return <div className="empty">{error}</div>;
  if (!rows) return <div className="empty">Loading...</div>;
  if (rows.length === 0) return <div className="empty">No requests yet. Pick a pet on the Pets page.</div>;
  return (
    <>
      <h2>My requests</h2>
      <div className="wrap">
        <table>
          <thead><tr><th>Pet</th><th>Type</th><th>Note</th><th>Status</th></tr></thead>
          <tbody>
            {rows.map((r) => (
              <tr key={r.id}><td>{r.petName}</td><td>{r.kind}</td><td>{r.note}</td>
                <td className={"st-" + r.status}>{r.status}</td></tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}
