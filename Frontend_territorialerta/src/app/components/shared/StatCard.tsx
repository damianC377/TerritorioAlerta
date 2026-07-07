import { type LucideIcon } from "lucide-react";

interface StatCardProps {
  title: string;
  value: string | number;
  subtitle?: string;
  icon: LucideIcon;
  trend?: { value: string; up: boolean };
  color?: "blue" | "green" | "yellow" | "orange" | "red";
}

const colorMap = {
  blue:   { icon: "text-blue-400",   bg: "bg-blue-500/15",   border: "border-blue-500/30"   },
  green:  { icon: "text-green-400",  bg: "bg-green-500/15",  border: "border-green-500/30"  },
  yellow: { icon: "text-yellow-400", bg: "bg-yellow-500/15", border: "border-yellow-500/30" },
  orange: { icon: "text-orange-400", bg: "bg-orange-500/15", border: "border-orange-500/30" },
  red:    { icon: "text-red-400",    bg: "bg-red-500/15",    border: "border-red-500/30"    },
};

export function StatCard({ title, value, subtitle, icon: Icon, trend, color = "blue" }: StatCardProps) {
  const c = colorMap[color];
  return (
    <div className="bg-[#1e293b] border border-[#334155] rounded-xl p-5 flex flex-col gap-3 hover:border-[#475569] transition-colors">
      <div className="flex items-start justify-between">
        <span className="text-[#94a3b8] text-xs uppercase tracking-wide">{title}</span>
        <div className={`w-9 h-9 rounded-lg ${c.bg} border ${c.border} flex items-center justify-center shrink-0`}>
          <Icon className={`w-4 h-4 ${c.icon}`} />
        </div>
      </div>
      <div>
        <div className="text-[#f1f5f9] text-2xl font-semibold tabular-nums">{value}</div>
        {subtitle && <div className="text-[#64748b] text-xs mt-0.5">{subtitle}</div>}
      </div>
      {trend && (
        <div className={`text-xs flex items-center gap-1 ${trend.up ? "text-red-400" : "text-green-400"}`}>
          <span>{trend.up ? "↑" : "↓"}</span>
          <span>{trend.value} vs semana anterior</span>
        </div>
      )}
    </div>
  );
}
