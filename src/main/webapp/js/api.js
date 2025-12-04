const API_BASE = "/aks-lab-1/api/v1";

async function apiGet(url) {
    const response = await fetch(API_BASE + url);
    if (!response.ok) throw new Error("Ошибка API");
    return await response.json();
}
