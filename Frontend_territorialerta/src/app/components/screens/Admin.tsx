import { useState } from "react";
import { Users, Tag, Database, Bell, Settings, Plus, Edit2, Trash2, MoreVertical, Shield, CheckCircle } from "lucide-react";
import { RiskBadge, type RiskLevel } from "../shared/RiskBadge";

type AdminTab = "usuarios" | "categorias" | "fuentes" | "alertas" | "configuracion";

const tabs: { id: AdminTab; label: string; icon: typeof Users }[] = [
  { id: "usuarios",     label: "Usuarios",      icon: Users    },
  { id: "categorias",   label: "Categorías",    icon: Tag      },
  { id: "fuentes",      label: "Fuentes",       icon: Database },
  { id: "alertas",      label: "Alertas",       icon: Bell     },
  { id: "configuracion",label: "Configuración", icon: Settings },
];

const users = [
  { id: 1, name: "Carlos Mendoza",   email: "c.mendoza@dagrd.gov.co",  role: "Administrador", status: "active",   lastLogin: "07/07/2026 09:00" },
  { id: 2, name: "Luisa Fernández",  email: "l.fernandez@dagrd.gov.co",role: "Analista",      status: "active",   lastLogin: "07/07/2026 08:45" },
  { id: 3, name: "Pedro García",     email: "p.garcia@mde.gov.co",     role: "Operador",      status: "active",   lastLogin: "06/07/2026 17:30" },
  { id: 4, name: "Silvia Torres",    email: "s.torres@siata.gov.co",   role: "Visualizador",  status: "inactive", lastLogin: "01/07/2026 12:00" },
  { id: 5, name: "Jorge Ramirez",    email: "j.ramirez@dagrd.gov.co",  role: "Analista",      status: "active",   lastLogin: "07/07/2026 07:55" },
];

const categories = [
  { id: 1, name: "Deslizamiento",   icon: "⛰", color: "#f97316", risk: "critical" as RiskLevel, count: 28 },
  { id: 2, name: "Inundación",      icon: "🌊", color: "#3b82f6", risk: "high"     as RiskLevel, count: 35 },
  { id: 3, name: "Residuos",        icon: "🗑", color: "#a855f7", risk: "medium"   as RiskLevel, count: 22 },
  { id: 4, name: "Infraestructura", icon: "🏗", color: "#f59e0b", risk: "high"     as RiskLevel, count: 15 },
  { id: 5, name: "Servicios",       icon: "⚡", color: "#06b6d4", risk: "low"      as RiskLevel, count: 12 },
  { id: 6, name: "Alumbrado",       icon: "💡", color: "#eab308", risk: "low"      as RiskLevel, count: 9  },
];

const configItems = [
  { section: "Notificaciones",   items: [
    { key: "Alertas críticas en tiempo real", value: true  },
    { key: "Resumen diario por correo",        value: true  },
    { key: "SMS para alertas críticas",        value: false },
  ]},
  { section: "Mapa",   items: [
    { key: "Centrar en Medellín al cargar",   value: true  },
    { key: "Mostrar etiquetas de barrios",     value: true  },
    { key: "Auto-refresh cada 5 minutos",      value: true  },
  ]},
  { section: "Sistema",   items: [
    { key: "Modo oscuro forzado",              value: true  },
    { key: "Sincronización automática",        value: true  },
    { key: "Log de acciones de usuario",       value: false },
  ]},
];

export function Admin() {
  const [activeTab, setActiveTab] = useState<AdminTab>("usuarios");
  const [toggles, setToggles] = useState<Record<string, boolean>>(
    configItems.flatMap(s => s.items).reduce((acc, i) => ({ ...acc, [i.key]: i.value }), {})
  );

  return (
    <div className="p-4 md:p-6 flex flex-col gap-6">
      {/* Stats */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        {[
          ["5", "Usuarios activos",    "text-blue-400",   "bg-blue-500/15",   "border-blue-500/30"  ],
          ["6", "Categorías",          "text-purple-400", "bg-purple-500/15", "border-purple-500/30"],
          ["4", "Fuentes de datos",    "text-green-400",  "bg-green-500/15",  "border-green-500/30" ],
          ["12","Alertas en curso",    "text-red-400",    "bg-red-500/15",    "border-red-500/30"   ],
        ].map(([val, label, tc, bg, border]) => (
          <div key={label} className={`${bg} border ${border} rounded-xl p-4`}>
            <div className={`${tc} text-2xl font-semibold tabular-nums`}>{val}</div>
            <div className="text-[#64748b] text-xs mt-1">{label}</div>
          </div>
        ))}
      </div>

      <div className="flex flex-col md:flex-row gap-5">
        {/* Sidebar tabs */}
        <div className="md:w-48 shrink-0">
          <div className="bg-[#1e293b] border border-[#334155] rounded-xl overflow-hidden">
            {tabs.map(t => (
              <button
                key={t.id}
                onClick={() => setActiveTab(t.id)}
                className={`w-full flex items-center gap-3 px-4 py-3 text-left border-b border-[#1e3a5f] last:border-b-0 transition-colors text-sm ${
                  activeTab === t.id
                    ? "bg-blue-600/20 text-blue-400"
                    : "text-[#64748b] hover:text-[#94a3b8] hover:bg-[#0a1628]"
                }`}
              >
                <t.icon className="w-4 h-4 shrink-0" />
                {t.label}
              </button>
            ))}
          </div>
        </div>

        {/* Content */}
        <div className="flex-1 min-w-0">
          {/* Usuarios */}
          {activeTab === "usuarios" && (
            <div className="bg-[#1e293b] border border-[#334155] rounded-xl overflow-hidden">
              <div className="px-5 py-4 border-b border-[#334155] flex items-center justify-between">
                <span className="text-[#e2e8f0] text-sm font-medium">Gestión de usuarios</span>
                <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs transition-colors">
                  <Plus className="w-3.5 h-3.5" /> Nuevo usuario
                </button>
              </div>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-[#0d1a2a]">
                      {["Nombre","Correo","Rol","Estado","Último ingreso",""].map(h => (
                        <th key={h} className="px-4 py-3 text-left text-[#3a5a78] text-xs uppercase tracking-wide font-medium">{h}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {users.map(u => (
                      <tr key={u.id} className="border-b border-[#0d1a2a] last:border-b-0 hover:bg-[#0a1628]/50 transition-colors">
                        <td className="px-4 py-3">
                          <div className="flex items-center gap-2.5">
                            <div className="w-7 h-7 rounded-full bg-gradient-to-br from-blue-700 to-indigo-700 flex items-center justify-center text-white text-xs shrink-0">
                              {u.name.split(" ").map(n => n[0]).join("").slice(0,2)}
                            </div>
                            <span className="text-[#94a3b8] text-xs font-medium">{u.name}</span>
                          </div>
                        </td>
                        <td className="px-4 py-3 text-[#64748b] text-xs">{u.email}</td>
                        <td className="px-4 py-3">
                          <span className="px-2 py-0.5 rounded-full bg-blue-500/15 border border-blue-500/30 text-blue-400 text-xs">{u.role}</span>
                        </td>
                        <td className="px-4 py-3">
                          <span className={`flex items-center gap-1.5 w-fit text-xs ${u.status === "active" ? "text-green-400" : "text-[#3a5a78]"}`}>
                            <span className={`w-1.5 h-1.5 rounded-full ${u.status === "active" ? "bg-green-400" : "bg-[#334155]"}`} />
                            {u.status === "active" ? "Activo" : "Inactivo"}
                          </span>
                        </td>
                        <td className="px-4 py-3 text-[#3a5a78] text-xs">{u.lastLogin}</td>
                        <td className="px-4 py-3">
                          <div className="flex items-center gap-1">
                            <button className="p-1.5 rounded text-[#3a5a78] hover:text-blue-400 hover:bg-blue-500/10 transition-colors"><Edit2 className="w-3.5 h-3.5" /></button>
                            <button className="p-1.5 rounded text-[#3a5a78] hover:text-red-400 hover:bg-red-500/10 transition-colors"><Trash2 className="w-3.5 h-3.5" /></button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Categorías */}
          {activeTab === "categorias" && (
            <div className="bg-[#1e293b] border border-[#334155] rounded-xl overflow-hidden">
              <div className="px-5 py-4 border-b border-[#334155] flex items-center justify-between">
                <span className="text-[#e2e8f0] text-sm font-medium">Categorías de incidentes</span>
                <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs transition-colors">
                  <Plus className="w-3.5 h-3.5" /> Nueva categoría
                </button>
              </div>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-0">
                {categories.map((c, i) => (
                  <div key={c.id} className={`px-5 py-4 flex items-center justify-between border-b border-r-0 sm:border-r border-[#0d1a2a] ${i % 2 !== 0 ? "sm:border-r-0" : ""} last:border-b-0 hover:bg-[#0a1628]/50 transition-colors`}>
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-xl flex items-center justify-center text-xl" style={{ background: c.color + "20", border: `1px solid ${c.color}40` }}>
                        {c.icon}
                      </div>
                      <div>
                        <div className="text-[#94a3b8] text-sm font-medium">{c.name}</div>
                        <div className="flex items-center gap-2 mt-1">
                          <RiskBadge level={c.risk} size="sm" />
                          <span className="text-[#3a5a78] text-xs">{c.count} registros</span>
                        </div>
                      </div>
                    </div>
                    <div className="flex gap-1">
                      <button className="p-1.5 rounded text-[#3a5a78] hover:text-blue-400 hover:bg-blue-500/10 transition-colors"><Edit2 className="w-3.5 h-3.5" /></button>
                      <button className="p-1.5 rounded text-[#3a5a78] hover:text-red-400 hover:bg-red-500/10 transition-colors"><Trash2 className="w-3.5 h-3.5" /></button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Fuentes */}
          {activeTab === "fuentes" && (
            <div className="bg-[#1e293b] border border-[#334155] rounded-xl">
              <div className="px-5 py-4 border-b border-[#334155] flex items-center justify-between">
                <span className="text-[#e2e8f0] text-sm font-medium">Gestión de fuentes de datos</span>
                <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs transition-colors">
                  <Plus className="w-3.5 h-3.5" /> Agregar fuente
                </button>
              </div>
              {["SIATA","IDEAM","DAGRD","Datos Abiertos MDE"].map((f, i) => (
                <div key={f} className="px-5 py-4 border-b border-[#0d1a2a] last:border-b-0 flex items-center justify-between hover:bg-[#0a1628]/50 transition-colors">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 rounded-lg bg-blue-500/15 border border-blue-500/30 flex items-center justify-center">
                      <Database className="w-4 h-4 text-blue-400" />
                    </div>
                    <div>
                      <div className="text-[#94a3b8] text-sm">{f}</div>
                      <div className="flex items-center gap-1.5 mt-0.5">
                        <span className="w-1.5 h-1.5 rounded-full bg-green-400" />
                        <span className="text-[#3a7a5a] text-xs">En línea</span>
                      </div>
                    </div>
                  </div>
                  <div className="flex gap-1">
                    <button className="px-3 py-1.5 rounded-lg text-[#4a7aa8] hover:text-[#94a3b8] hover:bg-[#0a1628] text-xs transition-colors">Editar</button>
                    <button className="p-1.5 rounded text-[#3a5a78] hover:text-[#94a3b8] transition-colors"><MoreVertical className="w-3.5 h-3.5" /></button>
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* Alertas (admin) */}
          {activeTab === "alertas" && (
            <div className="bg-[#1e293b] border border-[#334155] rounded-xl">
              <div className="px-5 py-4 border-b border-[#334155] flex items-center justify-between">
                <span className="text-[#e2e8f0] text-sm font-medium">Configuración de alertas</span>
                <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs transition-colors">
                  <Plus className="w-3.5 h-3.5" /> Nueva regla
                </button>
              </div>
              <div className="p-5 flex flex-col gap-4">
                {[
                  { name: "Umbral de lluvia 24h > 80mm",   trigger: "SIATA",  action: "Alerta roja",   active: true  },
                  { name: "Nivel río > 2.5m",               trigger: "SIATA",  action: "Evacuación",    active: true  },
                  { name: "Incidente sin asignar >2h",      trigger: "DAGRD",  action: "Notificación",  active: true  },
                  { name: "Nuevo reporte ciudadano",         trigger: "MDE",    action: "Revisión",      active: false },
                ].map((rule, i) => (
                  <div key={i} className="flex items-center justify-between py-3 border-b border-[#1e3a5f] last:border-b-0">
                    <div className="flex items-center gap-3">
                      <div className={`w-2 h-2 rounded-full ${rule.active ? "bg-green-400" : "bg-[#334155]"}`} />
                      <div>
                        <div className="text-[#94a3b8] text-xs font-medium">{rule.name}</div>
                        <div className="text-[#3a5a78] text-xs">Fuente: {rule.trigger} → {rule.action}</div>
                      </div>
                    </div>
                    <div className="flex items-center gap-2">
                      <span className={`text-xs ${rule.active ? "text-green-400" : "text-[#3a5a78]"}`}>{rule.active ? "Activa" : "Inactiva"}</span>
                      <button className="p-1.5 rounded text-[#3a5a78] hover:text-blue-400 transition-colors"><Edit2 className="w-3.5 h-3.5" /></button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Configuración */}
          {activeTab === "configuracion" && (
            <div className="flex flex-col gap-4">
              {configItems.map(section => (
                <div key={section.section} className="bg-[#1e293b] border border-[#334155] rounded-xl overflow-hidden">
                  <div className="px-5 py-3 border-b border-[#334155]">
                    <span className="text-[#e2e8f0] text-sm font-medium">{section.section}</span>
                  </div>
                  {section.items.map(item => (
                    <div key={item.key} className="px-5 py-3.5 flex items-center justify-between border-b border-[#0d1a2a] last:border-b-0">
                      <span className="text-[#94a3b8] text-xs">{item.key}</span>
                      <button
                        onClick={() => setToggles(prev => ({ ...prev, [item.key]: !prev[item.key] }))}
                        className={`relative w-10 h-5 rounded-full transition-colors shrink-0 ${toggles[item.key] ? "bg-blue-600" : "bg-[#334155]"}`}
                      >
                        <span className={`absolute top-0.5 w-4 h-4 rounded-full bg-white transition-transform ${toggles[item.key] ? "translate-x-5" : "translate-x-0.5"}`} />
                      </button>
                    </div>
                  ))}
                </div>
              ))}

              <div className="bg-[#1e293b] border border-[#334155] rounded-xl px-5 py-4">
                <div className="flex items-center gap-2 mb-3">
                  <Shield className="w-4 h-4 text-blue-400" />
                  <span className="text-[#e2e8f0] text-sm font-medium">Información del sistema</span>
                </div>
                {[
                  ["Versión",          "TerritorioAlerta v2.4.1"],
                  ["Entorno",          "Producción — DAGRD"],
                  ["Base de datos",    "PostgreSQL 15 — Activa"],
                  ["Última migración", "05/07/2026 02:00"],
                ].map(([k,v]) => (
                  <div key={k} className="flex justify-between py-2 border-b border-[#1e3a5f] last:border-b-0">
                    <span className="text-[#4a7aa8] text-xs">{k}</span>
                    <span className="text-[#64748b] text-xs">{v}</span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
