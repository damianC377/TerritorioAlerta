import { useState } from "react";
import { Filter, X, MapPin, Calendar, AlertTriangle, Info } from "lucide-react";
import { MockMap, defaultMarkers, type MapMarker } from "../shared/MockMap";
import { RiskBadge } from "../shared/RiskBadge";

const filterOptions = [
  { id: "deslizamiento",   label: "Deslizamientos",   color: "#f97316", checked: true },
  { id: "inundacion",      label: "Inundaciones",      color: "#3b82f6", checked: true },
  { id: "residuos",        label: "Basuras",           color: "#a855f7", checked: true },
  { id: "infraestructura", label: "Infraestructura",   color: "#f59e0b", checked: true },
  { id: "servicios",       label: "Servicios públicos",color: "#06b6d4", checked: false },
  { id: "alumbrado",       label: "Alumbrado",         color: "#eab308", checked: false },
];

export function MapView() {
  const [filters, setFilters] = useState(
    filterOptions.reduce((acc, f) => ({ ...acc, [f.id]: f.checked }), {} as Record<string, boolean>)
  );
  const [selected, setSelected] = useState<MapMarker | null>(null);
  const [panelOpen, setPanelOpen] = useState(false);

  const activeFilters = Object.entries(filters).filter(([, v]) => v).map(([k]) => k);

  const toggle = (id: string) => setFilters(prev => ({ ...prev, [id]: !prev[id] }));

  const handleMarkerClick = (marker: MapMarker) => {
    setSelected(marker);
    setPanelOpen(true);
  };

  const riskLabel: Record<string, string> = {
    low: "Bajo", medium: "Medio", high: "Alto", critical: "Crítico",
  };

  return (
    <div className="flex h-full min-h-[calc(100vh-57px)] relative">
      {/* Filter sidebar */}
      <aside className="hidden md:flex w-56 bg-[#0a1628] border-r border-[#1e3a5f] flex-col shrink-0">
        <div className="px-4 py-3 border-b border-[#1e3a5f] flex items-center gap-2">
          <Filter className="w-4 h-4 text-blue-400" />
          <span className="text-[#94a3b8] text-sm font-medium">Filtros de capa</span>
        </div>
        <div className="p-4 flex flex-col gap-2">
          {filterOptions.map(f => (
            <label key={f.id} className="flex items-center gap-3 cursor-pointer group py-1">
              <div
                className={`w-4 h-4 rounded border-2 flex items-center justify-center shrink-0 transition-colors`}
                style={{ borderColor: filters[f.id] ? f.color : "#334155", background: filters[f.id] ? f.color + "30" : "transparent" }}
                onClick={() => toggle(f.id)}
              >
                {filters[f.id] && <span className="block w-2 h-2 rounded-sm" style={{ background: f.color }} />}
              </div>
              <div className="flex items-center gap-2">
                <span className="w-2 h-2 rounded-full shrink-0" style={{ background: f.color }} />
                <span className="text-[#94a3b8] text-xs group-hover:text-[#c4d4e4] transition-colors">{f.label}</span>
              </div>
            </label>
          ))}
        </div>

        <div className="px-4 py-3 border-t border-[#1e3a5f] mt-auto">
          <div className="text-[#3a5a78] text-xs uppercase tracking-wide mb-2">Incidentes visibles</div>
          <div className="text-[#94a3b8] text-xl font-semibold">{defaultMarkers.filter(m => activeFilters.includes(m.type)).length}</div>
          <div className="text-[#3a5a78] text-xs">de {defaultMarkers.length} totales</div>
        </div>

        {/* Risk counts */}
        <div className="px-4 pb-4 flex flex-col gap-1.5">
          {[["critical","#ef4444"],["high","#f97316"],["medium","#eab308"],["low","#22c55e"]].map(([r, c]) => {
            const count = defaultMarkers.filter(m => m.risk === r && activeFilters.includes(m.type)).length;
            return (
              <div key={r} className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <span className="w-2 h-2 rounded-full" style={{ background: c }} />
                  <span className="text-[#64748b] text-xs capitalize">{riskLabel[r]}</span>
                </div>
                <span className="text-[#94a3b8] text-xs font-medium tabular-nums">{count}</span>
              </div>
            );
          })}
        </div>
      </aside>

      {/* Map */}
      <div className="flex-1 relative p-3 md:p-4">
        {/* Mobile filter bar */}
        <div className="flex md:hidden gap-2 mb-3 overflow-x-auto pb-1">
          {filterOptions.map(f => (
            <button
              key={f.id}
              onClick={() => toggle(f.id)}
              className={`shrink-0 flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs border transition-colors ${
                filters[f.id]
                  ? "border-[#334155] text-[#94a3b8] bg-[#1e293b]"
                  : "border-[#1e3a5f] text-[#3a5a78] bg-transparent"
              }`}
            >
              <span className="w-1.5 h-1.5 rounded-full" style={{ background: f.color }} />
              {f.label}
            </button>
          ))}
        </div>

        <div className="h-[calc(100%-0px)] md:h-full" style={{ minHeight: "400px" }}>
          <MockMap
            markers={defaultMarkers}
            activeFilters={activeFilters}
            onMarkerClick={handleMarkerClick}
          />
        </div>
      </div>

      {/* Info panel */}
      {panelOpen && selected && (
        <aside className="w-72 bg-[#0a1628] border-l border-[#1e3a5f] flex flex-col shrink-0 absolute md:relative right-0 top-0 bottom-0 z-20 md:z-auto">
          <div className="px-4 py-3 border-b border-[#1e3a5f] flex items-center justify-between">
            <div className="flex items-center gap-2">
              <Info className="w-4 h-4 text-blue-400" />
              <span className="text-[#94a3b8] text-sm font-medium">Detalle del incidente</span>
            </div>
            <button onClick={() => setPanelOpen(false)} className="text-[#3a5a78] hover:text-[#94a3b8] transition-colors">
              <X className="w-4 h-4" />
            </button>
          </div>

          <div className="p-4 flex flex-col gap-4 overflow-y-auto">
            <div className="bg-[#1e293b] rounded-xl p-4 border border-[#334155]">
              <div className="flex items-start justify-between mb-3">
                <div>
                  <div className="text-[#f1f5f9] text-sm font-medium">{selected.label}</div>
                  <div className="flex items-center gap-1.5 mt-1">
                    <MapPin className="w-3 h-3 text-[#4a7aa8]" />
                    <span className="text-[#4a7aa8] text-xs">{selected.neighborhood}</span>
                  </div>
                </div>
                <RiskBadge level={selected.risk} />
              </div>

              {[
                ["Categoría",       selected.type],
                ["Nivel de riesgo", riskLabel[selected.risk]],
                ["ID Incidente",    `INC-${selected.id.padStart(4,"0")}`],
                ["Fuente",          "SIATA / DAGRD"],
                ["Última actualización","07/07/2026 09:30"],
              ].map(([label, value]) => (
                <div key={label} className="flex justify-between py-2 border-b border-[#1e3a5f] last:border-b-0">
                  <span className="text-[#4a7aa8] text-xs">{label}</span>
                  <span className="text-[#94a3b8] text-xs font-medium capitalize">{value}</span>
                </div>
              ))}
            </div>

            <div className="bg-[#1e293b] rounded-xl p-4 border border-[#334155]">
              <div className="flex items-center gap-2 mb-2">
                <Calendar className="w-3.5 h-3.5 text-blue-400" />
                <span className="text-[#94a3b8] text-xs font-medium">Descripción</span>
              </div>
              <p className="text-[#64748b] text-xs leading-relaxed">
                Incidente de tipo {selected.type.toLowerCase()} detectado en el barrio {selected.neighborhood}.
                Monitoreo activo por sensores SIATA. Se recomienda revisión de campo en las próximas 2 horas.
              </p>
            </div>

            <div className="bg-[#1e293b] rounded-xl p-4 border border-[#334155]">
              <div className="flex items-center gap-2 mb-3">
                <AlertTriangle className="w-3.5 h-3.5 text-yellow-400" />
                <span className="text-[#94a3b8] text-xs font-medium">Acciones recomendadas</span>
              </div>
              <ul className="flex flex-col gap-2">
                {["Notificar a comunidad afectada","Activar protocolo de monitoreo","Coordinar con DAGRD y Bomberos","Evaluar vías de evacuación"].map(a => (
                  <li key={a} className="flex items-start gap-2">
                    <span className="w-1.5 h-1.5 rounded-full bg-yellow-400 mt-1.5 shrink-0" />
                    <span className="text-[#64748b] text-xs">{a}</span>
                  </li>
                ))}
              </ul>
            </div>

            <button className="w-full py-2.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors">
              Crear alerta formal
            </button>
            <button className="w-full py-2.5 rounded-lg border border-[#334155] text-[#64748b] hover:text-[#94a3b8] hover:border-[#475569] text-xs transition-colors">
              Ver historial del punto
            </button>
          </div>
        </aside>
      )}
    </div>
  );
}
