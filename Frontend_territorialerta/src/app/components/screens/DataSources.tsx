import { useState } from "react";
import { Satellite, Globe, Building, Database, CheckCircle, AlertTriangle, Clock, RefreshCw, ExternalLink, ChevronDown, ChevronUp } from "lucide-react";

interface DataSource {
  id: string;
  name: string;
  fullName: string;
  icon: typeof Satellite;
  status: "online" | "degraded" | "offline";
  lastUpdate: string;
  dataTypes: string[];
  description: string;
  totalRecords: number;
  updateFreq: string;
  coverage: string;
}

const sources: DataSource[] = [
  {
    id: "siata",
    name: "SIATA",
    fullName: "Sistema de Alerta Temprana de Medellín",
    icon: Satellite,
    status: "online",
    lastUpdate: "07/07/2026 09:45",
    dataTypes: ["Lluvia", "Nivel ríos", "Deslizamientos", "Sismicidad", "Calidad aire"],
    description: "Red de monitoreo hidrometeorológico y geológico del Valle de Aburrá. Opera en tiempo real con más de 300 sensores.",
    totalRecords: 1_482_300,
    updateFreq: "Tiempo real (1 min)",
    coverage: "Valle de Aburrá",
  },
  {
    id: "ideam",
    name: "IDEAM",
    fullName: "Instituto de Hidrología, Meteorología y Estudios Ambientales",
    icon: Globe,
    status: "online",
    lastUpdate: "07/07/2026 09:00",
    dataTypes: ["Clima nacional", "Precipitación", "Temperatura", "Alertas hidro"],
    description: "Autoridad nacional en hidrología y meteorología. Provee datos a escala regional y nacional.",
    totalRecords: 892_100,
    updateFreq: "Cada hora",
    coverage: "Nacional",
  },
  {
    id: "dagrd",
    name: "DAGRD",
    fullName: "Departamento Administrativo de Gestión del Riesgo de Desastres",
    icon: Building,
    status: "online",
    lastUpdate: "07/07/2026 08:15",
    dataTypes: ["Alertas activas", "Incidentes", "Recursos", "Evacuaciones"],
    description: "Coordinación de emergencias y gestión del riesgo municipal. Registro oficial de incidentes y alertas.",
    totalRecords: 234_500,
    updateFreq: "Cada 15 min",
    coverage: "Medellín",
  },
  {
    id: "mde-open",
    name: "Datos Abiertos MDE",
    fullName: "Portal de Datos Abiertos — Alcaldía de Medellín",
    icon: Database,
    status: "degraded",
    lastUpdate: "06/07/2026 22:30",
    dataTypes: ["Infraestructura", "Residuos", "Equipamientos", "Movilidad"],
    description: "Repositorio de datos abiertos de la ciudad. Incluye capas geoespaciales, infraestructura y servicios urbanos.",
    totalRecords: 1_105_700,
    updateFreq: "Diario",
    coverage: "Medellín metropolitana",
  },
];

const statusConfig = {
  online:   { label: "En línea",   color: "text-green-400",  bg: "bg-green-500/15",  border: "border-green-500/30",  icon: CheckCircle   },
  degraded: { label: "Degradado",  color: "text-yellow-400", bg: "bg-yellow-500/15", border: "border-yellow-500/30", icon: AlertTriangle },
  offline:  { label: "Sin conexión",color: "text-red-400",   bg: "bg-red-500/15",    border: "border-red-500/30",    icon: AlertTriangle },
};

function fmt(n: number): string {
  return n >= 1_000_000 ? `${(n/1_000_000).toFixed(1)}M` : n >= 1000 ? `${(n/1000).toFixed(0)}K` : `${n}`;
}

export function DataSources() {
  const [expanded, setExpanded] = useState<string | null>(null);

  return (
    <div className="p-4 md:p-6 flex flex-col gap-6">
      {/* Summary bar */}
      <div className="bg-[#1e293b] border border-[#334155] rounded-xl px-5 py-4 flex flex-wrap gap-6 items-center">
        <div>
          <div className="text-[#64748b] text-xs uppercase tracking-wide">Fuentes activas</div>
          <div className="text-[#f1f5f9] text-2xl font-semibold mt-0.5">{sources.filter(s => s.status === "online").length}<span className="text-[#64748b] text-sm">/{sources.length}</span></div>
        </div>
        <div className="w-px h-10 bg-[#334155] hidden sm:block" />
        <div>
          <div className="text-[#64748b] text-xs uppercase tracking-wide">Registros totales</div>
          <div className="text-[#f1f5f9] text-2xl font-semibold mt-0.5">{fmt(sources.reduce((s,d) => s + d.totalRecords, 0))}</div>
        </div>
        <div className="w-px h-10 bg-[#334155] hidden sm:block" />
        <div>
          <div className="text-[#64748b] text-xs uppercase tracking-wide">Última sincronización</div>
          <div className="text-[#94a3b8] text-sm font-medium mt-0.5">07/07/2026 09:45</div>
        </div>
        <button className="ml-auto flex items-center gap-2 px-4 py-2 rounded-lg bg-blue-600/20 border border-blue-500/40 text-blue-400 hover:bg-blue-600/30 transition-colors text-xs">
          <RefreshCw className="w-3.5 h-3.5" /> Sincronizar todas
        </button>
      </div>

      {/* Source cards */}
      <div className="grid grid-cols-1 gap-4">
        {sources.map(source => {
          const sc = statusConfig[source.status];
          const StatusIcon = sc.icon;
          const isExpanded = expanded === source.id;

          return (
            <div key={source.id} className="bg-[#1e293b] border border-[#334155] rounded-xl overflow-hidden hover:border-[#475569] transition-colors">
              <div
                className="px-5 py-4 flex items-start gap-4 cursor-pointer"
                onClick={() => setExpanded(isExpanded ? null : source.id)}
              >
                {/* Icon */}
                <div className="w-12 h-12 rounded-xl bg-[#0f1f35] border border-[#334155] flex items-center justify-center shrink-0">
                  <source.icon className="w-6 h-6 text-blue-400" />
                </div>

                {/* Info */}
                <div className="flex-1 min-w-0">
                  <div className="flex flex-wrap items-center gap-3">
                    <div className="flex items-center gap-2">
                      <span className="text-[#f1f5f9] text-sm font-semibold">{source.name}</span>
                      <span className={`inline-flex items-center gap-1.5 px-2 py-0.5 rounded-full text-xs border ${sc.bg} ${sc.color} ${sc.border}`}>
                        <StatusIcon className="w-3 h-3" /> {sc.label}
                      </span>
                    </div>
                  </div>
                  <div className="text-[#4a7aa8] text-xs mt-0.5">{source.fullName}</div>

                  <div className="flex flex-wrap gap-3 mt-2.5">
                    <div className="flex items-center gap-1.5">
                      <Clock className="w-3 h-3 text-[#3a5a78]" />
                      <span className="text-[#64748b] text-xs">{source.lastUpdate}</span>
                    </div>
                    <div className="flex items-center gap-1.5">
                      <Database className="w-3 h-3 text-[#3a5a78]" />
                      <span className="text-[#64748b] text-xs">{fmt(source.totalRecords)} registros</span>
                    </div>
                    <div className="text-[#3a5a78] text-xs">{source.updateFreq}</div>
                  </div>

                  {/* Data type badges */}
                  <div className="flex flex-wrap gap-1.5 mt-2.5">
                    {source.dataTypes.map(dt => (
                      <span key={dt} className="px-2 py-0.5 rounded-full bg-[#0a1628] border border-[#334155] text-[#64748b] text-xs">
                        {dt}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Actions */}
                <div className="flex items-center gap-2 shrink-0">
                  <button className="p-1.5 rounded-lg text-[#4a7aa8] hover:text-[#94a3b8] hover:bg-[#0a1628] transition-colors" onClick={e => e.stopPropagation()}>
                    <RefreshCw className="w-3.5 h-3.5" />
                  </button>
                  <button className="p-1.5 rounded-lg text-[#4a7aa8] hover:text-[#94a3b8] hover:bg-[#0a1628] transition-colors" onClick={e => e.stopPropagation()}>
                    <ExternalLink className="w-3.5 h-3.5" />
                  </button>
                  <button className="p-1.5 rounded-lg text-[#4a7aa8] hover:text-[#94a3b8] transition-colors">
                    {isExpanded ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                  </button>
                </div>
              </div>

              {/* Expanded detail */}
              {isExpanded && (
                <div className="px-5 pb-5 border-t border-[#1e3a5f] pt-4">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <div className="text-[#64748b] text-xs uppercase tracking-wide mb-2">Descripción</div>
                      <p className="text-[#94a3b8] text-xs leading-relaxed">{source.description}</p>
                    </div>
                    <div>
                      <div className="text-[#64748b] text-xs uppercase tracking-wide mb-2">Detalles técnicos</div>
                      <div className="flex flex-col gap-1.5">
                        {[
                          ["Cobertura geográfica", source.coverage],
                          ["Frecuencia de actualización", source.updateFreq],
                          ["Total de registros", fmt(source.totalRecords)],
                          ["Última actualización", source.lastUpdate],
                        ].map(([k,v]) => (
                          <div key={k} className="flex justify-between py-1.5 border-b border-[#1e3a5f] last:border-b-0">
                            <span className="text-[#4a7aa8] text-xs">{k}</span>
                            <span className="text-[#94a3b8] text-xs font-medium">{v}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                  <div className="flex gap-2 mt-4">
                    <button className="px-4 py-2 rounded-lg bg-blue-600/20 border border-blue-500/40 text-blue-400 hover:bg-blue-600/30 text-xs transition-colors">Ver logs de sincronización</button>
                    <button className="px-4 py-2 rounded-lg border border-[#334155] text-[#64748b] hover:text-[#94a3b8] text-xs transition-colors">Configurar fuente</button>
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}
