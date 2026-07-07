import { Shield, LayoutDashboard, Map, Bell, BarChart3, Database, Settings, ChevronRight, X } from "lucide-react";
import type { Screen, AppUser, UserRole } from "../../App";
import { canAccess } from "./MainLayout";

const allNavItems: { id: Screen; label: string; icon: typeof LayoutDashboard; badge?: number }[] = [
  { id: "dashboard", label: "Inicio",            icon: LayoutDashboard           },
  { id: "map",       label: "Mapa",              icon: Map                       },
  { id: "alerts",    label: "Alertas",           icon: Bell,       badge: 5      },
  { id: "analytics", label: "Analítica",         icon: BarChart3                 },
  { id: "sources",   label: "Fuentes de datos",  icon: Database                  },
  { id: "admin",     label: "Administración",    icon: Settings                  },
];

const roleLabels: Record<UserRole, string> = {
  admin:    "Administrador",
  analyst:  "Analista",
  operator: "Operador",
  citizen:  "Ciudadano",
};

const roleBadgeClass: Record<UserRole, string> = {
  admin:    "bg-purple-500/20 text-purple-400 border-purple-500/40",
  analyst:  "bg-blue-500/20   text-blue-400   border-blue-500/40",
  operator: "bg-cyan-500/20   text-cyan-400   border-cyan-500/40",
  citizen:  "bg-green-500/20  text-green-400  border-green-500/40",
};

const roleAvatarClass: Record<UserRole, string> = {
  admin:    "from-purple-700 to-indigo-700",
  analyst:  "from-blue-700   to-indigo-700",
  operator: "from-cyan-700   to-blue-700",
  citizen:  "from-teal-700   to-cyan-700",
};

interface SidebarProps {
  current:       Screen;
  onNavigate:    (s: Screen) => void;
  mobileOpen?:   boolean;
  onMobileClose?: () => void;
  user:          AppUser | null;
}

export function Sidebar({ current, onNavigate, mobileOpen, onMobileClose, user }: SidebarProps) {
  const navItems = allNavItems.filter(item => canAccess(user, item.id));

  return (
    <>
      {mobileOpen && (
        <div className="fixed inset-0 bg-black/60 z-40 md:hidden" onClick={onMobileClose} />
      )}

      <aside className={`
        fixed md:static inset-y-0 left-0 z-50 md:z-auto
        w-64 bg-[#0a1628] border-r border-[#1e3a5f] flex flex-col
        transition-transform duration-300 md:translate-x-0
        ${mobileOpen ? "translate-x-0" : "-translate-x-full"}
      `}>
        {/* Logo */}
        <div className="px-5 py-4 border-b border-[#1e3a5f] flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-blue-600 to-blue-800 flex items-center justify-center shrink-0">
              <Shield className="w-4 h-4 text-white" />
            </div>
            <div>
              <div className="text-white text-sm font-semibold leading-tight">TerritorioAlerta</div>
              <div className="text-[#3a6a9a] text-xs">SIT v2.4.1 · Manrique</div>
            </div>
          </div>
          <button className="md:hidden text-[#4a7aa8] hover:text-white transition-colors" onClick={onMobileClose}>
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Status */}
        <div className="px-5 py-3 border-b border-[#1e3a5f]">
          <div className="flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-green-400 animate-pulse" />
            <span className="text-[#4a9a6a] text-xs">Sistema operativo</span>
          </div>
          <div className="text-[#2a4a6a] text-xs mt-0.5">Última sync: 07/07/2026 09:45</div>
        </div>

        {/* Nav */}
        <nav className="flex-1 px-3 py-4 flex flex-col gap-1 overflow-y-auto">
          {navItems.map(item => {
            const active = current === item.id;
            return (
              <button
                key={item.id}
                onClick={() => { onNavigate(item.id); onMobileClose?.(); }}
                className={`
                  w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-left
                  transition-all duration-150
                  ${active
                    ? "bg-blue-600/20 border border-blue-600/40 text-blue-300"
                    : "text-[#4a7aa8] hover:text-[#94a3b8] hover:bg-[#1e293b]"
                  }
                `}
              >
                <item.icon className={`w-4 h-4 shrink-0 ${active ? "text-blue-400" : ""}`} />
                <span className="flex-1 text-sm">{item.label}</span>
                {item.badge && (
                  <span className="w-5 h-5 rounded-full bg-red-500 text-white text-xs flex items-center justify-center font-medium">
                    {item.badge}
                  </span>
                )}
                {active && <ChevronRight className="w-3.5 h-3.5 text-blue-400" />}
              </button>
            );
          })}
        </nav>

        {/* User */}
        <div className="px-4 py-4 border-t border-[#1e3a5f]">
          {user ? (
            <div className="flex items-center gap-3">
              <div className={`w-8 h-8 rounded-full bg-gradient-to-br ${roleAvatarClass[user.role]} flex items-center justify-center text-white text-xs font-semibold shrink-0`}>
                {user.name.split(" ").map(n => n[0]).join("").slice(0, 2).toUpperCase()}
              </div>
              <div className="flex-1 min-w-0">
                <div className="text-[#94a3b8] text-xs font-medium truncate">{user.name}</div>
                <span className={`inline-flex text-xs px-1.5 py-0.5 rounded-full border ${roleBadgeClass[user.role]}`}>
                  {roleLabels[user.role]}
                </span>
              </div>
            </div>
          ) : (
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 rounded-full bg-[#1e293b] border border-[#334155] flex items-center justify-center shrink-0">
                <span className="text-[#3a5a78] text-xs">?</span>
              </div>
              <div className="flex-1 min-w-0">
                <div className="text-[#3a5a78] text-xs">Visitante</div>
                <div className="text-[#1e3a5f] text-xs">Sin sesión activa</div>
              </div>
            </div>
          )}
        </div>
      </aside>
    </>
  );
}
