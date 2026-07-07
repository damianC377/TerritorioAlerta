export type RiskLevel = "low" | "medium" | "high" | "critical" | "info";

interface RiskBadgeProps {
  level: RiskLevel;
  size?: "sm" | "md";
  showDot?: boolean;
}

const riskConfig: Record<RiskLevel, { label: string; bg: string; text: string; dot: string }> = {
  low:      { label: "Bajo",     bg: "bg-green-500/20",  text: "text-green-400",  dot: "bg-green-400"  },
  medium:   { label: "Medio",    bg: "bg-yellow-500/20", text: "text-yellow-400", dot: "bg-yellow-400" },
  high:     { label: "Alto",     bg: "bg-orange-500/20", text: "text-orange-400", dot: "bg-orange-400" },
  critical: { label: "Crítico",  bg: "bg-red-500/20",    text: "text-red-400",    dot: "bg-red-400"    },
  info:     { label: "Info",     bg: "bg-blue-500/20",   text: "text-blue-400",   dot: "bg-blue-400"   },
};

export function RiskBadge({ level, size = "md", showDot = true }: RiskBadgeProps) {
  const c = riskConfig[level];
  return (
    <span className={`inline-flex items-center gap-1.5 px-2 py-0.5 rounded-full ${c.bg} ${c.text} ${size === "sm" ? "text-xs" : "text-xs"} font-medium`}>
      {showDot && <span className={`w-1.5 h-1.5 rounded-full ${c.dot} shrink-0`} />}
      {c.label}
    </span>
  );
}
