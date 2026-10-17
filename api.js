// All backend calls go through this file. It adds the JWT token to every request.
const BASE = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

async function call(path, { method = "GET", body } = {}) {
  const headers = { "Content-Type": "application/json" };
  const token = localStorage.getItem("token");
  if (token) headers.Authorization = "Bearer " + token;
  let res;
  try {
    res = await fetch(BASE + path, { method, headers, body: body ? JSON.stringify(body) : undefined });
  } catch {
    throw new Error("Cannot reach the server. Check that the backend is running.");
  }
  let data = null;
  try { data = await res.json(); } catch { /* empty response */ }
  if (!res.ok) throw new Error((data && (data.message || data.error)) || "Request failed (" + res.status + ")");
  return data;
}

export const api = {
  register: (b) => call("/auth/register", { method: "POST", body: b }),
  login: (b) => call("/auth/login", { method: "POST", body: b }),
  pets: (q = "", type = "", status = "") =>
    call(`/pets?q=${encodeURIComponent(q)}&type=${encodeURIComponent(type)}&status=${encodeURIComponent(status)}`),
  addPet: (b) => call("/pets", { method: "POST", body: b }),
  deletePet: (id) => call("/pets/" + id, { method: "DELETE" }),
  sendRequest: (b) => call("/requests", { method: "POST", body: b }),
  myRequests: () => call("/requests/mine"),
  allRequests: () => call("/requests"),
  approve: (id) => call(`/requests/${id}/approve`, { method: "PUT" }),
  reject: (id) => call(`/requests/${id}/reject`, { method: "PUT" }),
  stats: () => call("/admin/stats"),
};
