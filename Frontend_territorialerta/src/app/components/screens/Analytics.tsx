import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, LineChart, Line, CartesianGrid, Legend } from "recharts";
import { StatCard } from "../shared/StatCard";
import { AlertTriangle, MapPin, Home, Bell } from "lucide-react";

const categoryData = [
  { name: "Deslizamientos", value: 58, fill: "#f97316" },
  { name: "Inundaciones",   value: 72, fill: "#3b82f6" },
  { name: "Residuos",       value: 45, fill: "#a855f7" },
  { name: "Infraestructura",value: 31, fill: "#f59e0b" },
  { name: "Servicios",      value: 24, fill: "#06b6d4" },
  { name: "Alumbrado",      value: 19, fill: "#eab308" },
];

const trendData = [
  { mes: "Ene", incidentes: 28, alertas: 12, cerrados: 22 },
  { mes: "Feb", incidentes: 32, alertas: 15, cerrados: 28 },
  { mes: "Mar", incidentes: 55, alertas: 28, cerrados: 38 },
  { mes: "Abr", incidentes: 42, alertas: 20, cerrados: 35 },
  { mes: "May", incidentes: 68, alertas: 35, cerrados: 51 },
  { mes: "Jun", incidentes: 54, alertas: 26, cerrados: 44 },
  { mes: "Jul", incidentes: 38, alertas: 18, cerrados: 24 },
];

const riskDist = [
  { name: "Crítico", value: 8,  color: "#ef4444" },
  { name: "Alto",    value: 22, color: "#f97316" },
  { name: "Medio",   value: 35, color: "#eab308" },
  { name: "Bajo",    value: 49, color: "#22c55e" },
];

const heatData = [
  { zona: "Popular",       ene:3, feb:2, mar:6, abr:4, may:8, jun:5, jul:3 },
  { zona: "Robledo",       ene:5, feb:4, mar:8, abr:6, may:9, jun:7, jul:5 },
  { zona: "Manrique",      ene:2, feb:3, mar:5, abr:3, may:7, jun:4, jul:2 },
  { zona: "Aranjuez",      ene:4, feb:5, mar:7, abr:5, may:6, jun:4, jul:3 },
  { zona: "El Centro",     ene:6, feb:4, mar:9, abr:7, may:10,jun:8, jul:6 },
  { zona: "San Javier",    ene:3, feb:2, mar:4, abr:3, may:5, jun:3, jul:2 },
  { zona: "Laureles",      ene:1, feb:1, mar:2, abr:2, may:3, jun:2, jul:1 },
  { zona: "El Poblado",    ene:1, feb:0, mar:1, abr:1, may:2, jun:1, jul:0 },
];

const months = ["Ene","Feb","Mar","Abr","May","Jun","Jul"];

function heatColor(v: number): string {
  if (v >= 9)  return "#ef4444";
  if (v >= 7)  return "#f97316";
  if (v >= 5)  return "#eab308";
  if (v >= 3)  return "#22c55e";
  return "#1e3a5f";
}

const CustomTooltip = ({ active, payload, label }: any) => {
  if (!active || !payload?.length) return null;
  return (
    <div className="bg-[#0f1f35] border border-[#334155] rounded-lg p-3 text-xs">
      <div className="text-[#94a3b8] mb-2">{label}</div>
      {payload.map((p: any) => (
        <div key={p.dataKey} className="flex items-center gap-2 mb-1">
          <span className="w-2 h-2 rounded-full" style={{ background: p.stroke || p.fill }} />
          <span className="text-[#64748b]">{p.name}:</span>
          <span className="text-[#f1f5f9]">{p.value}</span>
        </div>
      ))}
    </div>
  );
};

export function Analytics() {
  const totalIncidentes = categoryData.reduce((s, d) => s + d.value, 0);

  return (
    <div className="p-4 md:p-6 flex flex-col gap-6">
      {/* Stat cards */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard title="Incidentes totales" value={totalIncidentes} subtitle="Período: 2026"    icon={AlertTriangle} color="red"    />
        <StatCard title="Riesgo promedio"     value="3.2/5"          subtitle="Índice compuesto" icon={AlertTriangle} color="orange" />
        <StatCard title="Barrios afectados"   value="18"             subtitle="de 16 comunas"    icon={Home}          color="blue"   />
        <StatCard title="Alertas activas"     value="12"             subtitle="5 sin resolver"   icon={Bell}          color="yellow" />
      </div>

      {/* Row 1: Bar + Pie */}
      <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
        {/* Bar chart */}
        <div className="md:col-span-3 bg-[#1e293b] border border-[#334155] rounded-xl">
          <div className="px-4 py-3 border-b border-[#334155] flex items-center gap-2">
            <div className="w-1.5 h-4 rounded-full bg-blue-500" />
            <span className="text-[#e2e8f0] text-sm font-medium">Incidentes por categoría</span>
          </div>
          <div className="p-4">
            <ResponsiveContainer width="100%" height={220}>
              <BarChart data={categoryData} layout="vertical" barSize={14}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e3a5f" horizontal={false} />
                <XAxis type="number" tick={{ fill: "#64748b", fontSize: 11 }} axisLine={false} tickLine={false} />
                <YAxis dataKey="name" type="category" tick={{ fill: "#94a3b8", fontSize: 11 }} axisLine={false} tickLine={false} width={90} />
                <Tooltip content={<CustomTooltip />} cursor={{ fill: "rgba(255,255,255,0.03)" }} />
                <Bar dataKey="value" name="Incidentes" radius={[0,4,4,0]}>
                  {categoryData.map((entry, i) => <Cell key={i} fill={entry.fill} />)}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Pie chart */}
        <div className="md:col-span-2 bg-[#1e293b] border border-[#334155] rounded-xl">
          <div className="px-4 py-3 border-b border-[#334155] flex items-center gap-2">
            <div className="w-1.5 h-4 rounded-full bg-red-500" />
            <span className="text-[#e2e8f0] text-sm font-medium">Distribución por riesgo</span>
          </div>
          <div className="p-4 flex flex-col items-center">
            <ResponsiveContainer width="100%" height={160}>
              <PieChart>
                <Pie data={riskDist} cx="50%" cy="50%" innerRadius={45} outerRadius={70} dataKey="value" paddingAngle={3}>
                  {riskDist.map((entry, i) => <Cell key={i} fill={entry.color} />)}
                </Pie>
                <Tooltip
                  content={({ active, payload }) => {
                    if (!active || !payload?.length) return null;
                    const d = payload[0].payload;
                    return (
                      <div className="bg-[#0f1f35] border border-[#334155] rounded-lg p-2 text-xs">
                        <span style={{ color: d.color }}>{d.name}: </span>
                        <span className="text-white">{d.value}</span>
                      </div>
                    );
                  }}
                />
              </PieChart>
            </ResponsiveContainer>
            <div className="flex flex-col gap-1.5 w-full mt-2">
              {riskDist.map(d => (
                <div key={d.name} className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <span className="w-2 h-2 rounded-full" style={{ background: d.color }} />
                    <span className="text-[#64748b] text-xs">{d.name}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-[#94a3b8] text-xs tabular-nums">{d.value}</span>
                    <span className="text-[#3a5a78] text-xs tabular-nums">{Math.round(d.value / 114 * 100)}%</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Row 2: Line trend */}
      <div className="bg-[#1e293b] border border-[#334155] rounded-xl">
        <div className="px-4 py-3 border-b border-[#334155] flex items-center gap-2">
          <div className="w-1.5 h-4 rounded-full bg-green-500" />
          <span className="text-[#e2e8f0] text-sm font-medium">Tendencia mensual 2026</span>
        </div>
        <div className="p-4">
          <ResponsiveContainer width="100%" height={180}>
            <LineChart data={trendData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1e3a5f" vertical={false} />
              <XAxis dataKey="mes" tick={{ fill: "#64748b", fontSize: 11 }} axisLine={false} tickLine={false} />
              <YAxis tick={{ fill: "#64748b", fontSize: 11 }} axisLine={false} tickLine={false} />
              <Tooltip content={<CustomTooltip />} cursor={{ stroke: "#334155" }} />
              <Legend
                wrapperStyle={{ paddingTop: "8px" }}
                formatter={(value) => <span style={{ color: "#64748b", fontSize: 11 }}>{value}</span>}
              />
              <Line type="monotone" dataKey="incidentes" name="Incidentes"  stroke="#f97316" strokeWidth={2.5} dot={{ r: 3, fill: "#f97316" }} />
              <Line type="monotone" dataKey="alertas"    name="Alertas"     stroke="#ef4444" strokeWidth={2}   dot={{ r: 3, fill: "#ef4444" }} />
              <Line type="monotone" dataKey="cerrados"   name="Cerrados"    stroke="#22c55e" strokeWidth={2}   dot={{ r: 3, fill: "#22c55e" }} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Heatmap */}
      <div className="bg-[#1e293b] border border-[#334155] rounded-xl">
        <div className="px-4 py-3 border-b border-[#334155] flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-1.5 h-4 rounded-full bg-orange-500" />
            <span className="text-[#e2e8f0] text-sm font-medium">Mapa de calor — Incidentes por barrio / mes</span>
          </div>
        </div>
        <div className="p-4 overflow-x-auto">
          <table className="w-full border-separate border-spacing-1">
            <thead>
              <tr>
                <th className="text-[#3a5a78] text-xs text-left py-1 pr-4 font-normal w-28">Barrio</th>
                {months.map(m => (
                  <th key={m} className="text-[#3a5a78] text-xs text-center py-1 font-normal w-10">{m}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {heatData.map(row => (
                <tr key={row.zona}>
                  <td className="text-[#64748b] text-xs pr-4 py-0.5">{row.zona}</td>
                  {months.map(m => {
                    const val = row[m.toLowerCase() as keyof typeof row] as number;
                    return (
                      <td key={m} className="text-center p-0">
                        <div
                          className="w-9 h-7 rounded flex items-center justify-center text-xs font-medium mx-auto transition-opacity hover:opacity-80"
                          style={{ background: heatColor(val), color: val >= 3 ? "#fff" : "#3a5a78" }}
                          title={`${row.zona} — ${m}: ${val}`}
                        >
                          {val || ""}
                        </div>
                      </td>
                    );
                  })}
                </tr>
              ))}
            </tbody>
          </table>

          {/* Legend */}
          <div className="flex items-center gap-4 mt-4 flex-wrap">
            <span className="text-[#3a5a78] text-xs">Intensidad:</span>
            {[["#1e3a5f","0-2"],["#22c55e","3-4"],["#eab308","5-6"],["#f97316","7-8"],["#ef4444","9+"]].map(([c,l]) => (
              <div key={l} className="flex items-center gap-1.5">
                <span className="w-5 h-3 rounded" style={{ background: c, display: "inline-block" }} />
                <span className="text-[#64748b] text-xs">{l}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
