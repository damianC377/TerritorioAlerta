import { useState } from "react";
import {
  AlertTriangle, Droplets, MapPin, Phone, Bell, Shield,
  Plus, CheckCircle, Clock, ArrowRight, Thermometer,
  Info, Megaphone, TriangleAlert, Lock,
} from "lucide-react";
import { AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from "recharts";
import { RiskBadge, type RiskLevel } from "../shared/RiskBadge";
import { MockMap } from "../shared/MockMap";
import type { Screen, AppUser } from "../../App";

const neighborhoodRisk: Record<string, { level: RiskLevel; label: string }> = {
  "Robledo":       { level: "critical", label: "Riesgo crítico por deslizamiento activo" },
  "Castilla":      { level: "critical", label: "Creciente río Medellín — nivel 3" },
  "Popular":       { level: "high",     label: "Inestabilidad de talud detectada" },
  "Aranjuez":      { level: "high",     label: "Inundación en zonas bajas" },
  "Manrique":      { level: "medium",   label: "Lluvia acumulada supera umbral medio" },
  "El Centro":     { level: "high",     label: "Vía deteriorada — circulación limitada" },
  "San Javier":    { level: "medium",   label: "Monitoreo activo de taludes" },
  "Laureles":      { level: "low",      label: "Condiciones normales de riesgo" },
  "El Poblado":    { level: "low",      label: "Sin alertas activas" },
  "Belén":         { level: "low",      label: "Punto de residuos saturado — reporte activo" },
  "Buenos Aires":  { level: "medium",   label: "Quebrada con nivel elevado" },
  "Guayabal":      { level: "low",      label: "Puente en revisión preventiva" },
  "Envigado":      { level: "medium",   label: "Lluvia intensa prevista 14h–18h" },
  "Itagüí":        { level: "low",      label: "Sin alertas activas" },
};

const rainData = [
  { hora: "00h", mm: 2  },
  { hora: "03h", mm: 8  },
  { hora: "06h", mm: 15 },
  { hora: "09h", mm: 28 },
  { hora: "12h", mm: 18 },
  { hora: "15h", mm: 35 },
  { hora: "18h", mm: 42 },
  { hora: "21h", mm: 12 },
  { hora: "Ahora", mm: 7 },
];

const nearbyAlerts = [
  {
    id: "A-2847",
    type: "Deslizamiento activo",
    location: "Calle 13 #80-20 — Robledo",
    risk: "critical" as RiskLevel,
    time: "Hace 12 min",
    advice: "Evita acercarte a laderas y taludes. Atención a ruidos extraños.",
    source: "SIATA",
  },
  {
    id: "A-2846",
    type: "Creciente río Medellín",
    location: "Castilla — Sector Río",
    risk: "critical" as RiskLevel,
    time: "Hace 28 min",
    advice: "No te acerques a las orillas del río. Desplázate a zonas altas.",
    source: "IDEAM",
  },
  {
    id: "A-2845",
    type: "Talud inestable",
    location: "Popular — Sector 4",
    risk: "high" as RiskLevel,
    time: "Hace 1h",
    advice: "Mantente alejado de la zona. Sigue instrucciones de la autoridad.",
    source: "DAGRD",
  },
  {
    id: "A-2843",
    type: "Residuos acumulados",
    location: "Belén — Parque Sur",
    risk: "medium" as RiskLevel,
    time: "Hace 3h",
    advice: "Reportado. No deposites más residuos en esa zona.",
    source: "MDE",
  },
];

const recommendations: Record<RiskLevel, { title: string; tips: string[]; color: string; bg: string; border: string; icon: typeof Shield }> = {
  critical: {
    title: "Zona en alerta crítica — actúa ahora",
    tips: [
      "Aléjate de laderas, taludes y orillas de ríos o quebradas inmediatamente.",
      "Prepara documentos y objetos de valor en un morral listo para evacuar.",
      "Mantén el celular cargado y con datos. Escucha avisos oficiales.",
      "Si hay orden de evacuación, sigue las rutas señalizadas sin demora.",
    ],
    color: "text-red-400", bg: "bg-red-500/10", border: "border-red-500/30", icon: TriangleAlert,
  },
  high: {
    title: "Riesgo alto — mantente alerta",
    tips: [
      "Evita zonas bajas propensas a inundaciones o taludes con humedad.",
      "Revisa las salidas de emergencia de tu hogar o lugar de trabajo.",
      "Prepara un kit básico: agua, linterna, documentos, medicamentos.",
      "Monitorea las actualizaciones en esta plataforma cada hora.",
    ],
    color: "text-orange-400", bg: "bg-orange-500/10", border: "border-orange-500/30", icon: TriangleAlert,
  },
  medium: {
    title: "Riesgo medio — precaución",
    tips: [
      "Evita circular cerca de quebradas durante lluvias fuertes.",
      "No deposites residuos en vías o zonas no autorizadas.",
      "Reporta a las autoridades cualquier signo de agrietamiento en vías o paredes.",
      "Mantente informado sobre el pronóstico del tiempo.",
    ],
    color: "text-yellow-400", bg: "bg-yellow-500/10", border: "border-yellow-500/30", icon: AlertTriangle,
  },
  low: {
    title: "Riesgo bajo — todo en orden",
    tips: [
      "Condiciones de riesgo normales para tu zona.",
      "Revisa tu kit de emergencias periódicamente.",
      "Identifica las rutas de evacuación de tu barrio.",
      "Si detectas algo irregular, repórtalo usando el botón de incidente.",
    ],
    color: "text-green-400", bg: "bg-green-500/10", border: "border-green-500/30", icon: CheckCircle,
  },
  info: {
    title: "Sin alertas activas",
    tips: ["No hay alertas activas en este momento.", "Mantente informado."],
    color: "text-blue-400", bg: "bg-blue-500/10", border: "border-blue-500/30", icon: Info,
  },
};

const emergencyContacts = [
  { name: "Línea de Emergencias", number: "123",  color: "bg-red-600 hover:bg-red-500"   },
  { name: "DAGRD",                number: "4441",  color: "bg-orange-600 hover:bg-orange-500" },
  { name: "Bomberos",             number: "119",   color: "bg-yellow-600 hover:bg-yellow-500" },
  { name: "Cruz Roja",            number: "132",   color: "bg-blue-600 hover:bg-blue-500"   },
];

const RainTooltip = ({ active, payload, label }: any) => {
  if (!active || !payload?.length) return null;
  return (
    <div className="bg-[#0f1f35] border border-[#334155] rounded-lg p-2.5 text-xs">
      <div className="text-[#94a3b8]">{label}</div>
      <div className="text-blue-300 font-medium mt-0.5">{payload[0].value} mm</div>
    </div>
  );
};

const BARRIO = "Manrique";

interface DashboardProps {
  onNavigate:    (s: Screen) => void;
  user:          AppUser | null;
  onRequireAuth: (afterLogin?: () => void) => void;
}

export function Dashboard({ onNavigate, user, onRequireAuth }: DashboardProps) {
  const [reportModal, setReportModal] = useState(false);
  const [reportSent,  setReportSent]  = useState(false);
  const [reportType,  setReportType]  = useState("");
  const [reportDesc,  setReportDesc]  = useState("");

  const currentRisk = neighborhoodRisk[BARRIO] ?? { level: "low" as RiskLevel, label: "Sin información" };
  const rec     = recommendations[currentRisk.level];
  const RecIcon = rec.icon;

  const handleReport = () => {
    if (!reportType) return;
    setReportSent(true);
    setTimeout(() => { setReportModal(false); setReportSent(false); setReportType(""); setReportDesc(""); }, 1800);
  };

  return (
    <div className="p-4 md:p-6 flex flex-col gap-5">

      {/* ── Banner de bienvenida ── */}
      <div className="bg-[#0f1f35] border border-[#1e3a5f] rounded-2xl px-5 py-4 flex flex-col sm:flex-row sm:items-center gap-4">
        <div className="flex-1 min-w-0">
          <div className="text-[#4a7aa8] text-xs uppercase tracking-wide mb-0.5">Martes 7 de julio, 2026 · 09:47 a.m.</div>
          <h1 className="text-[#f1f5f9] text-base font-semibold">
            {user ? `Bienvenido, ${user.name.split(" ")[0]}` : "Bienvenido al sistema de alertas"}
          </h1>
          <p className="text-[#64748b] text-xs mt-0.5 leading-relaxed">
            Consulta el estado de riesgo, alertas activas y recomendaciones para tu zona en tiempo real.
          </p>
        </div>
        {/* Barrio fijo */}
        <div className="flex items-center gap-2 px-4 py-2 bg-[#1e293b] border border-[#334155] rounded-xl shrink-0">
          <MapPin className="w-3.5 h-3.5 text-blue-400 shrink-0" />
          <div>
            <div className="text-[#3a5a78] text-xs">Barrio</div>
            <div className="text-[#e2e8f0] text-sm font-medium">{BARRIO}</div>
          </div>
        </div>
      </div>

      {/* ── Nivel de riesgo destacado ── */}
      <div className={`rounded-2xl border ${rec.border} ${rec.bg} px-5 py-4 flex items-start gap-4`}>
        <div className={`w-10 h-10 rounded-xl ${rec.bg} border ${rec.border} flex items-center justify-center shrink-0 mt-0.5`}>
          <RecIcon className={`w-5 h-5 ${rec.color}`} />
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex flex-wrap items-center gap-2 mb-1">
            <span className={`text-sm font-semibold ${rec.color}`}>{rec.title}</span>
            <RiskBadge level={currentRisk.level} />
          </div>
          <p className="text-[#94a3b8] text-xs leading-relaxed">{currentRisk.label}</p>
        </div>
        <div className="text-[#3a5a78] text-xs text-right shrink-0 hidden sm:block">
          <div>Actualizado</div>
          <div className="text-[#64748b]">Hace 5 min</div>
        </div>
      </div>

      {/* ── 4 Stat cards ciudadanas ── */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Riesgo zona */}
        <div className={`bg-[#1e293b] border border-[#334155] rounded-xl p-4 flex flex-col gap-2`}>
          <div className="flex items-center justify-between">
            <span className="text-[#94a3b8] text-xs uppercase tracking-wide">Riesgo en tu barrio</span>
            <Shield className="w-4 h-4 text-[#4a7aa8]" />
          </div>
          <RiskBadge level={currentRisk.level} />
          <div className="text-[#64748b] text-xs">{BARRIO}</div>
        </div>

        {/* Alertas cerca */}
        <div className="bg-[#1e293b] border border-[#334155] rounded-xl p-4 flex flex-col gap-2">
          <div className="flex items-center justify-between">
            <span className="text-[#94a3b8] text-xs uppercase tracking-wide">Alertas activas</span>
            <Bell className="w-4 h-4 text-[#4a7aa8]" />
          </div>
          <div className="text-[#f1f5f9] text-2xl font-semibold tabular-nums">12</div>
          <div className="text-[#64748b] text-xs">3 cerca de tu zona</div>
        </div>

        {/* Lluvia */}
        <div className="bg-[#1e293b] border border-[#334155] rounded-xl p-4 flex flex-col gap-2">
          <div className="flex items-center justify-between">
            <span className="text-[#94a3b8] text-xs uppercase tracking-wide">Lluvia acumulada</span>
            <Droplets className="w-4 h-4 text-blue-400" />
          </div>
          <div className="text-[#f1f5f9] text-2xl font-semibold tabular-nums">82<span className="text-[#64748b] text-sm font-normal">mm</span></div>
          <div className="text-[#64748b] text-xs">Últimas 48 horas</div>
        </div>

        {/* Condición */}
        <div className="bg-[#1e293b] border border-[#334155] rounded-xl p-4 flex flex-col gap-2">
          <div className="flex items-center justify-between">
            <span className="text-[#94a3b8] text-xs uppercase tracking-wide">Temperatura</span>
            <Thermometer className="w-4 h-4 text-orange-400" />
          </div>
          <div className="text-[#f1f5f9] text-2xl font-semibold tabular-nums">17<span className="text-[#64748b] text-sm font-normal">°C</span></div>
          <div className="text-[#64748b] text-xs">Parcialmente nublado</div>
        </div>
      </div>

      {/* ── Mapa + panel derecho ── */}
      <div className="grid grid-cols-1 xl:grid-cols-5 gap-4">
        {/* Mapa */}
        <div className="xl:col-span-3 bg-[#1e293b] border border-[#334155] rounded-xl overflow-hidden">
          <div className="px-4 py-3 border-b border-[#334155] flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-1.5 h-4 rounded-full bg-blue-500" />
              <span className="text-[#e2e8f0] text-sm font-medium">¿Qué pasa cerca de ti?</span>
            </div>
            <button
              onClick={() => onNavigate("map")}
              className="flex items-center gap-1 text-blue-400 text-xs hover:text-blue-300 transition-colors"
            >
              Ver mapa completo <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </div>
          <div className="h-72 md:h-80">
            <MockMap compact={false} />
          </div>
        </div>

        {/* Panel derecho */}
        <div className="xl:col-span-2 flex flex-col gap-4">
          {/* Reportar incidente — CTA principal */}
          <button
            onClick={() => user ? setReportModal(true) : onRequireAuth(() => setReportModal(true))}
            className="w-full bg-gradient-to-br from-blue-600 to-blue-700 hover:from-blue-500 hover:to-blue-600 border border-blue-500/40 rounded-xl p-5 flex items-center gap-4 transition-all group text-left"
          >
            <div className="w-12 h-12 rounded-xl bg-white/10 border border-white/20 flex items-center justify-center shrink-0 group-hover:bg-white/15 transition-colors">
              {user ? <Plus className="w-6 h-6 text-white" /> : <Lock className="w-5 h-5 text-white/80" />}
            </div>
            <div>
              <div className="text-white text-sm font-semibold">Reportar un incidente</div>
              <div className="text-blue-200 text-xs mt-0.5">
                {user ? "¿Viste algo en tu barrio? Infórmalo aquí" : "Inicia sesión para reportar"}
              </div>
            </div>
          </button>

          {/* Lluvia últimas horas */}
          <div className="bg-[#1e293b] border border-[#334155] rounded-xl">
            <div className="px-4 py-3 border-b border-[#334155] flex items-center gap-2">
              <Droplets className="w-3.5 h-3.5 text-blue-400" />
              <span className="text-[#e2e8f0] text-xs font-medium">Lluvia del día (mm)</span>
            </div>
            <div className="px-3 pt-2 pb-3">
              <ResponsiveContainer width="100%" height={100}>
                <AreaChart data={rainData}>
                  <defs>
                    <linearGradient id="rainGrad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%"  stopColor="#3b82f6" stopOpacity={0.3} />
                      <stop offset="95%" stopColor="#3b82f6" stopOpacity={0}   />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#1e3a5f" vertical={false} />
                  <XAxis dataKey="hora" tick={{ fill: "#64748b", fontSize: 9 }} axisLine={false} tickLine={false} />
                  <YAxis tick={{ fill: "#64748b", fontSize: 9 }} axisLine={false} tickLine={false} width={24} />
                  <Tooltip content={<RainTooltip />} cursor={{ stroke: "#334155" }} />
                  <Area type="monotone" dataKey="mm" name="mm" stroke="#3b82f6" strokeWidth={2} fill="url(#rainGrad)" dot={false} />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Contactos de emergencia */}
          <div className="bg-[#1e293b] border border-[#334155] rounded-xl">
            <div className="px-4 py-3 border-b border-[#334155] flex items-center gap-2">
              <Phone className="w-3.5 h-3.5 text-red-400" />
              <span className="text-[#e2e8f0] text-xs font-medium">Números de emergencia</span>
            </div>
            <div className="p-3 grid grid-cols-2 gap-2">
              {emergencyContacts.map(c => (
                <a
                  key={c.name}
                  href={`tel:${c.number}`}
                  className={`${c.color} rounded-xl px-3 py-2.5 flex flex-col items-center justify-center gap-0.5 transition-colors`}
                >
                  <span className="text-white text-base font-bold tabular-nums">{c.number}</span>
                  <span className="text-white/80 text-xs text-center leading-tight">{c.name}</span>
                </a>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* ── Alertas que te afectan ── */}
      <div className="bg-[#1e293b] border border-[#334155] rounded-xl">
        <div className="px-4 py-3 border-b border-[#334155] flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-1.5 h-4 rounded-full bg-red-500" />
            <span className="text-[#e2e8f0] text-sm font-medium">Alertas que te pueden afectar</span>
            <span className="px-2 py-0.5 rounded-full bg-red-500/20 text-red-400 text-xs">4 activas</span>
          </div>
          <button onClick={() => onNavigate("alerts")} className="flex items-center gap-1 text-blue-400 text-xs hover:text-blue-300 transition-colors">
            Ver todas <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>
        <div className="divide-y divide-[#0d1a2a]">
          {nearbyAlerts.map(alert => (
            <div key={alert.id} className="px-4 py-4 hover:bg-[#0a1628]/50 transition-colors">
              <div className="flex items-start gap-3">
                <div className={`w-8 h-8 rounded-lg flex items-center justify-center shrink-0 mt-0.5 ${
                  alert.risk === "critical" ? "bg-red-500/15 border border-red-500/30"    :
                  alert.risk === "high"     ? "bg-orange-500/15 border border-orange-500/30" :
                  "bg-yellow-500/15 border border-yellow-500/30"
                }`}>
                  <AlertTriangle className={`w-4 h-4 ${
                    alert.risk === "critical" ? "text-red-400"    :
                    alert.risk === "high"     ? "text-orange-400" :
                    "text-yellow-400"
                  }`} />
                </div>
                <div className="flex-1 min-w-0">
                  <div className="flex flex-wrap items-center gap-2 mb-1">
                    <span className="text-[#e2e8f0] text-xs font-medium">{alert.type}</span>
                    <RiskBadge level={alert.risk} size="sm" />
                    <span className="text-[#3a5a78] text-xs ml-auto hidden sm:block">{alert.source}</span>
                  </div>
                  <div className="flex items-center gap-1.5 mb-2">
                    <MapPin className="w-3 h-3 text-[#3a5a78] shrink-0" />
                    <span className="text-[#64748b] text-xs">{alert.location}</span>
                  </div>
                  <div className="flex items-start gap-2 bg-[#0f1f35] border border-[#1e3a5f] rounded-lg px-3 py-2">
                    <Megaphone className="w-3 h-3 text-blue-400 shrink-0 mt-0.5" />
                    <span className="text-[#94a3b8] text-xs leading-relaxed">{alert.advice}</span>
                  </div>
                </div>
                <div className="flex items-center gap-1 text-[#3a5a78] text-xs shrink-0">
                  <Clock className="w-3 h-3" />{alert.time}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* ── Recomendaciones de seguridad ── */}
      <div className={`rounded-2xl border ${rec.border} ${rec.bg}`}>
        <div className={`px-5 py-3.5 border-b ${rec.border} flex items-center gap-2`}>
          <RecIcon className={`w-4 h-4 ${rec.color}`} />
          <span className={`text-sm font-medium ${rec.color}`}>¿Qué debo hacer? — Recomendaciones actuales</span>
        </div>
        <div className="p-5 grid grid-cols-1 sm:grid-cols-2 gap-3">
          {rec.tips.map((tip, i) => (
            <div key={i} className="flex items-start gap-3">
              <div className={`w-5 h-5 rounded-full ${rec.bg} border ${rec.border} flex items-center justify-center shrink-0 mt-0.5`}>
                <span className={`text-xs font-semibold ${rec.color}`}>{i + 1}</span>
              </div>
              <p className="text-[#94a3b8] text-xs leading-relaxed">{tip}</p>
            </div>
          ))}
        </div>
      </div>

      {/* ── Modal reportar incidente ── */}
      {reportModal && (
        <div className="fixed inset-0 bg-black/70 z-50 flex items-center justify-center p-4" onClick={() => setReportModal(false)}>
          <div className="bg-[#0f1f35] border border-[#1e3a5f] rounded-2xl w-full max-w-md shadow-2xl" onClick={e => e.stopPropagation()}>
            {reportSent ? (
              <div className="p-8 flex flex-col items-center gap-3 text-center">
                <div className="w-14 h-14 rounded-full bg-green-500/20 border border-green-500/40 flex items-center justify-center">
                  <CheckCircle className="w-7 h-7 text-green-400" />
                </div>
                <div className="text-[#f1f5f9] text-sm font-semibold">¡Reporte enviado!</div>
                <p className="text-[#64748b] text-xs">El equipo de DAGRD revisará tu reporte en breve. Gracias por contribuir.</p>
              </div>
            ) : (
              <>
                <div className="px-5 py-4 border-b border-[#1e3a5f] flex items-center gap-3">
                  <div className="w-8 h-8 rounded-lg bg-blue-600/20 border border-blue-500/40 flex items-center justify-center">
                    <Plus className="w-4 h-4 text-blue-400" />
                  </div>
                  <div>
                    <div className="text-[#f1f5f9] text-sm font-medium">Reportar incidente</div>
                    <div className="text-[#3a5a78] text-xs">Barrio: {BARRIO}</div>
                  </div>
                </div>
                <div className="p-5 flex flex-col gap-4">
                  <div>
                    <label className="block text-[#94a3b8] text-xs mb-2 uppercase tracking-wide">Tipo de incidente</label>
                    <div className="grid grid-cols-2 gap-2">
                      {["Deslizamiento","Inundación","Residuos","Vía dañada","Alumbrado","Otro"].map(t => (
                        <button
                          key={t}
                          onClick={() => setReportType(t)}
                          className={`px-3 py-2 rounded-lg border text-xs transition-colors ${
                            reportType === t
                              ? "bg-blue-600/20 border-blue-500/50 text-blue-300"
                              : "border-[#334155] text-[#64748b] hover:border-[#475569] hover:text-[#94a3b8]"
                          }`}
                        >
                          {t}
                        </button>
                      ))}
                    </div>
                  </div>
                  <div>
                    <label className="block text-[#94a3b8] text-xs mb-2 uppercase tracking-wide">Descripción (opcional)</label>
                    <textarea
                      rows={3}
                      value={reportDesc}
                      onChange={e => setReportDesc(e.target.value)}
                      placeholder="Describe brevemente lo que observaste..."
                      className="w-full bg-[#0a1628] border border-[#1e3a5f] rounded-lg px-3 py-2 text-[#94a3b8] placeholder-[#2a4a6a] text-xs outline-none focus:border-blue-500/50 resize-none transition-colors"
                    />
                  </div>
                  <div className="flex gap-2">
                    <button
                      onClick={handleReport}
                      disabled={!reportType}
                      className="flex-1 py-2.5 rounded-lg bg-blue-600 hover:bg-blue-500 disabled:bg-[#1e293b] disabled:text-[#3a5a78] text-white text-xs font-medium transition-colors"
                    >
                      Enviar reporte
                    </button>
                    <button
                      onClick={() => setReportModal(false)}
                      className="flex-1 py-2.5 rounded-lg border border-[#334155] text-[#64748b] hover:text-[#94a3b8] text-xs transition-colors"
                    >
                      Cancelar
                    </button>
                  </div>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
