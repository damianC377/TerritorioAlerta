import { useState } from "react";
import { Search, Filter, Eye, Clock, ChevronDown, X, CheckCircle, AlertTriangle, MapPin } from "lucide-react";
import { RiskBadge, type RiskLevel } from "../shared/RiskBadge";

interface Alert {
  id: string;
  type: string;
  category: string;
  location: string;
  neighborhood: string;
  risk: RiskLevel;
  status: "active" | "monitoring" | "resolved";
  date: string;
  source: string;
}

const allAlerts: Alert[] = [
  { id:"A-2847", type:"Deslizamiento",   category:"Geológico",    location:"Calle 13 #80-20",     neighborhood:"Robledo",       risk:"critical", status:"active",     date:"07/07/2026 09:12", source:"SIATA"    },
  { id:"A-2846", type:"Inundación",       category:"Hidrológico",  location:"Río Medellín Km 4",   neighborhood:"Castilla",      risk:"critical", status:"active",     date:"07/07/2026 08:30", source:"IDEAM"    },
  { id:"A-2845", type:"Deslizamiento",    category:"Geológico",    location:"Sector 4 Manzana C",  neighborhood:"Popular",       risk:"high",     status:"active",     date:"07/07/2026 07:55", source:"DAGRD"    },
  { id:"A-2844", type:"Infraestructura",  category:"Civil",        location:"Cra 45 #52-10",       neighborhood:"El Centro",     risk:"high",     status:"monitoring", date:"07/07/2026 06:20", source:"DAM"      },
  { id:"A-2843", type:"Residuos",         category:"Ambiental",    location:"Parque Sur",          neighborhood:"Belén",         risk:"medium",   status:"monitoring", date:"06/07/2026 18:00", source:"MDE Open" },
  { id:"A-2842", type:"Alumbrado",        category:"Servicios",    location:"Av El Poblado #3-5",  neighborhood:"El Poblado",    risk:"low",      status:"monitoring", date:"06/07/2026 16:45", source:"EPM"      },
  { id:"A-2841", type:"Deslizamiento",    category:"Geológico",    location:"Talud Cra 86",        neighborhood:"San Javier",    risk:"high",     status:"active",     date:"06/07/2026 14:10", source:"SIATA"    },
  { id:"A-2840", type:"Inundación",       category:"Hidrológico",  location:"Quebrada Aguacatala", neighborhood:"Envigado",      risk:"medium",   status:"monitoring", date:"06/07/2026 11:30", source:"IDEAM"    },
  { id:"A-2839", type:"Servicios",        category:"Servicios",    location:"Sector 1 Barrio Juan", neighborhood:"Aranjuez",     risk:"medium",   status:"resolved",   date:"05/07/2026 20:00", source:"EPM"      },
  { id:"A-2838", type:"Infraestructura",  category:"Civil",        location:"Puente Cra 52",       neighborhood:"Guayabal",      risk:"low",      status:"resolved",   date:"05/07/2026 15:00", source:"DAM"      },
  { id:"A-2837", type:"Residuos",         category:"Ambiental",    location:"Zona Industrial",     neighborhood:"Buenos Aires",  risk:"high",     status:"resolved",   date:"04/07/2026 10:00", source:"MDE Open" },
  { id:"A-2836", type:"Deslizamiento",    category:"Geológico",    location:"Loma Hermosa",        neighborhood:"Manrique",      risk:"medium",   status:"resolved",   date:"03/07/2026 09:00", source:"SIATA"    },
];

const statusConfig = {
  active:     { label: "Activa",       bg: "bg-red-500/15",    text: "text-red-400",    border: "border-red-500/30"    },
  monitoring: { label: "Monitoreo",    bg: "bg-yellow-500/15", text: "text-yellow-400", border: "border-yellow-500/30" },
  resolved:   { label: "Finalizada",   bg: "bg-green-500/15",  text: "text-green-400",  border: "border-green-500/30"  },
};

export function Alerts() {
  const [tab, setTab]       = useState<"all" | "active" | "resolved">("all");
  const [search, setSearch] = useState("");
  const [detail, setDetail] = useState<Alert | null>(null);

  const filtered = allAlerts.filter(a => {
    const matchTab    = tab === "all" || (tab === "active" ? a.status !== "resolved" : a.status === "resolved");
    const matchSearch = !search || [a.id, a.type, a.location, a.neighborhood].some(f => f.toLowerCase().includes(search.toLowerCase()));
    return matchTab && matchSearch;
  });

  return (
    <div className="p-4 md:p-6 flex flex-col gap-5">
      {/* Controls */}
      <div className="flex flex-col sm:flex-row gap-3 items-start sm:items-center justify-between">
        {/* Tabs */}
        <div className="flex gap-1 p-1 bg-[#0a1628] rounded-xl border border-[#1e3a5f]">
          {(["all","active","resolved"] as const).map(t => {
            const labels = { all: "Todas", active: "Activas", resolved: "Finalizadas" };
            return (
              <button
                key={t}
                onClick={() => setTab(t)}
                className={`px-3 py-1.5 rounded-lg text-xs transition-colors ${
                  tab === t ? "bg-blue-600 text-white" : "text-[#4a7aa8] hover:text-[#94a3b8]"
                }`}
              >
                {labels[t]}
                <span className="ml-1.5 text-xs opacity-70">
                  {t === "all" ? allAlerts.length : t === "active" ? allAlerts.filter(a => a.status !== "resolved").length : allAlerts.filter(a => a.status === "resolved").length}
                </span>
              </button>
            );
          })}
        </div>

        <div className="flex gap-2 w-full sm:w-auto">
          <div className="relative flex-1 sm:w-64">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-[#3a5a78]" />
            <input
              value={search}
              onChange={e => setSearch(e.target.value)}
              placeholder="Buscar alertas..."
              className="w-full bg-[#0a1628] border border-[#1e3a5f] rounded-lg pl-9 pr-4 py-2 text-[#94a3b8] placeholder-[#2a4a6a] text-xs outline-none focus:border-blue-500/50 transition-colors"
            />
          </div>
          <button className="px-3 py-2 rounded-lg border border-[#1e3a5f] bg-[#0a1628] text-[#4a7aa8] hover:text-[#94a3b8] hover:border-[#334155] transition-colors flex items-center gap-1.5 text-xs">
            <Filter className="w-3.5 h-3.5" /> Filtrar
          </button>
        </div>
      </div>

      {/* Table */}
      <div className="bg-[#1e293b] border border-[#334155] rounded-xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="bg-[#0f1f35] border-b border-[#334155]">
                {["ID","Tipo","Ubicación","Riesgo","Estado","Fecha","Fuente",""].map(h => (
                  <th key={h} className="px-4 py-3 text-left text-[#3a5a78] text-xs uppercase tracking-wide font-medium whitespace-nowrap">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {filtered.length === 0 && (
                <tr>
                  <td colSpan={8} className="px-4 py-8 text-center text-[#3a5a78] text-sm">
                    No hay alertas que coincidan con los filtros.
                  </td>
                </tr>
              )}
              {filtered.map((alert, i) => {
                const sc = statusConfig[alert.status];
                return (
                  <tr key={alert.id} className={`border-b border-[#0d1a2a] hover:bg-[#0a1628]/50 transition-colors ${i === filtered.length - 1 ? "border-b-0" : ""}`}>
                    <td className="px-4 py-3 text-[#3b82f6] text-xs font-mono whitespace-nowrap">{alert.id}</td>
                    <td className="px-4 py-3">
                      <div className="text-[#94a3b8] text-xs font-medium">{alert.type}</div>
                      <div className="text-[#3a5a78] text-xs">{alert.category}</div>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-1.5">
                        <MapPin className="w-3 h-3 text-[#3a5a78] shrink-0" />
                        <div>
                          <div className="text-[#64748b] text-xs">{alert.neighborhood}</div>
                          <div className="text-[#3a5a78] text-xs">{alert.location}</div>
                        </div>
                      </div>
                    </td>
                    <td className="px-4 py-3"><RiskBadge level={alert.risk} /></td>
                    <td className="px-4 py-3">
                      <span className={`inline-flex items-center gap-1.5 px-2 py-0.5 rounded-full text-xs border ${sc.bg} ${sc.text} ${sc.border}`}>
                        {alert.status === "active" ? <AlertTriangle className="w-3 h-3" /> : alert.status === "resolved" ? <CheckCircle className="w-3 h-3" /> : <Clock className="w-3 h-3" />}
                        {sc.label}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-[#3a5a78] text-xs whitespace-nowrap">{alert.date}</td>
                    <td className="px-4 py-3 text-[#3a5a78] text-xs">{alert.source}</td>
                    <td className="px-4 py-3">
                      <button
                        onClick={() => setDetail(alert)}
                        className="flex items-center gap-1.5 text-blue-400 hover:text-blue-300 transition-colors text-xs whitespace-nowrap"
                      >
                        <Eye className="w-3.5 h-3.5" /> Ver detalle
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>

      {/* Detail Modal */}
      {detail && (
        <div className="fixed inset-0 bg-black/70 z-50 flex items-center justify-center p-4" onClick={() => setDetail(null)}>
          <div className="bg-[#0f1f35] border border-[#1e3a5f] rounded-2xl w-full max-w-md shadow-2xl" onClick={e => e.stopPropagation()}>
            <div className="px-5 py-4 border-b border-[#1e3a5f] flex items-center justify-between">
              <div>
                <div className="text-[#f1f5f9] text-sm font-medium">{detail.id} — {detail.type}</div>
                <div className="text-[#3a5a78] text-xs mt-0.5">{detail.neighborhood} · {detail.source}</div>
              </div>
              <button onClick={() => setDetail(null)} className="text-[#3a5a78] hover:text-[#94a3b8] transition-colors">
                <X className="w-5 h-5" />
              </button>
            </div>
            <div className="p-5 flex flex-col gap-3">
              <div className="flex items-center gap-3">
                <RiskBadge level={detail.risk} />
                <span className={`inline-flex items-center gap-1.5 px-2 py-0.5 rounded-full text-xs border ${statusConfig[detail.status].bg} ${statusConfig[detail.status].text} ${statusConfig[detail.status].border}`}>
                  {statusConfig[detail.status].label}
                </span>
              </div>
              {[
                ["Tipo",       detail.type],
                ["Categoría",  detail.category],
                ["Ubicación",  detail.location],
                ["Barrio",     detail.neighborhood],
                ["Fuente",     detail.source],
                ["Fecha",      detail.date],
              ].map(([k, v]) => (
                <div key={k} className="flex justify-between py-2 border-b border-[#1e3a5f] last:border-b-0">
                  <span className="text-[#4a7aa8] text-xs">{k}</span>
                  <span className="text-[#94a3b8] text-xs">{v}</span>
                </div>
              ))}
              <p className="text-[#64748b] text-xs leading-relaxed mt-1">
                Incidente registrado automáticamente por la red de sensores. Se encuentra en fase de {statusConfig[detail.status].label.toLowerCase()}.
                Personal de campo fue notificado. Se realizará seguimiento en las próximas 4 horas.
              </p>
              <div className="flex gap-2 mt-2">
                <button className="flex-1 py-2.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors">Actualizar estado</button>
                <button onClick={() => setDetail(null)} className="flex-1 py-2.5 rounded-lg border border-[#334155] text-[#64748b] hover:text-[#94a3b8] text-xs transition-colors">Cerrar</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
