import { useState } from "react";
import { Search, Bell, User, Menu, ChevronDown, AlertTriangle, RefreshCw, LogOut, LogIn } from "lucide-react";
import type { Screen, AppUser, UserRole } from "../../App";

const screenTitles: Record<Screen, string> = {
  dashboard: "Inicio",
  map:       "Mapa Territorial",
  alerts:    "Alertas",
  analytics: "Analítica",
  sources:   "Fuentes de Datos",
  admin:     "Administración",
};

const roleLabels: Record<UserRole, string> = {
  admin:    "Administrador",
  analyst:  "Analista",
  operator: "Operador",
  citizen:  "Ciudadano",
};

const roleBadge: Record<UserRole, string> = {
  admin:    "bg-purple-500/20 text-purple-400 border-purple-500/40",
  analyst:  "bg-blue-500/20   text-blue-400   border-blue-500/40",
  operator: "bg-cyan-500/20   text-cyan-400   border-cyan-500/40",
  citizen:  "bg-green-500/20  text-green-400  border-green-500/40",
};

const roleAvatar: Record<UserRole, string> = {
  admin:    "from-purple-700 to-indigo-700",
  analyst:  "from-blue-700   to-indigo-700",
  operator: "from-cyan-700   to-blue-700",
  citizen:  "from-teal-700   to-cyan-700",
};

interface HeaderProps {
  currentScreen: Screen;
  onMenuToggle:  () => void;
  user:          AppUser | null;
  onOpenAuth:    () => void;
  onLogout:      () => void;
}

export function Header({ currentScreen, onMenuToggle, user, onOpenAuth, onLogout }: HeaderProps) {
  const [notifOpen, setNotifOpen] = useState(false);
  const [userOpen,  setUserOpen]  = useState(false);

  const notifications = [
    { id: 1, text: "Alerta crítica — Deslizamiento en Manrique",  time: "Hace 12 min", color: "text-red-400"    },
    { id: 2, text: "Lluvia intensa — umbral superado Zona NE",    time: "Hace 28 min", color: "text-orange-400" },
    { id: 3, text: "SIATA: nivel quebrada La Rosa en alerta",      time: "Hace 45 min", color: "text-yellow-400" },
  ];

  const initials = user
    ? user.name.split(" ").map(n => n[0]).join("").slice(0, 2).toUpperCase()
    : "";

  return (
    <header className="bg-[#0f1f35] border-b border-[#1e3a5f] px-4 py-3 flex items-center gap-4 sticky top-0 z-30">
      <button className="md:hidden text-[#4a7aa8] hover:text-white transition-colors" onClick={onMenuToggle}>
        <Menu className="w-5 h-5" />
      </button>

      {/* Breadcrumb */}
      <div className="hidden md:flex items-center gap-2 min-w-0">
        <span className="text-[#3a5a78] text-sm">TerritorioAlerta</span>
        <span className="text-[#2a3a4a] text-sm">/</span>
        <span className="text-[#3a5a78] text-sm">Manrique</span>
        <span className="text-[#2a3a4a] text-sm">/</span>
        <span className="text-[#e2e8f0] text-sm font-medium">{screenTitles[currentScreen]}</span>
      </div>
      <h1 className="md:hidden text-[#e2e8f0] text-sm font-medium">{screenTitles[currentScreen]}</h1>

      {/* Search */}
      <div className="flex-1 max-w-md hidden sm:block">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[#3a5a78]" />
          <input
            placeholder="Buscar alertas, incidentes en Manrique…"
            className="w-full bg-[#0a1628] border border-[#1e3a5f] rounded-lg pl-9 pr-4 py-2 text-[#94a3b8] placeholder-[#2a4a6a] text-xs outline-none focus:border-blue-500/50 transition-colors"
          />
        </div>
      </div>

      <div className="flex items-center gap-2 ml-auto">
        <button className="hidden sm:flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-[#0a1628] border border-[#1e3a5f] text-[#4a7aa8] hover:text-[#94a3b8] hover:border-[#334155] transition-colors text-xs">
          <RefreshCw className="w-3.5 h-3.5" />
          <span>Actualizar</span>
        </button>

        {/* Notifications */}
        <div className="relative">
          <button
            onClick={() => { setNotifOpen(!notifOpen); setUserOpen(false); }}
            className="relative w-9 h-9 rounded-lg bg-[#0a1628] border border-[#1e3a5f] flex items-center justify-center text-[#4a7aa8] hover:text-[#94a3b8] hover:border-[#334155] transition-colors"
          >
            <Bell className="w-4 h-4" />
            <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-red-500" />
          </button>

          {notifOpen && (
            <div className="absolute right-0 top-11 w-80 bg-[#0f1f35] border border-[#1e3a5f] rounded-xl shadow-2xl shadow-black/50 z-50">
              <div className="px-4 py-3 border-b border-[#1e3a5f] flex items-center justify-between">
                <span className="text-[#94a3b8] text-sm font-medium">Notificaciones</span>
                <span className="text-xs bg-red-500/20 text-red-400 px-2 py-0.5 rounded-full">{notifications.length} nuevas</span>
              </div>
              {notifications.map(n => (
                <div key={n.id} className="px-4 py-3 border-b border-[#0d1a2a] hover:bg-[#0a1628] cursor-pointer transition-colors">
                  <div className="flex items-start gap-2.5">
                    <AlertTriangle className={`w-4 h-4 mt-0.5 shrink-0 ${n.color}`} />
                    <div>
                      <div className="text-[#c4d4e4] text-xs leading-snug">{n.text}</div>
                      <div className="text-[#3a5a78] text-xs mt-1">{n.time}</div>
                    </div>
                  </div>
                </div>
              ))}
              <div className="px-4 py-2.5 text-center">
                <button className="text-blue-400 text-xs hover:text-blue-300 transition-colors">Ver todas</button>
              </div>
            </div>
          )}
        </div>

        {/* Sin sesión */}
        {!user && (
          <button onClick={onOpenAuth}
            className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors">
            <LogIn className="w-3.5 h-3.5" />
            <span className="hidden sm:inline">Iniciar sesión</span>
          </button>
        )}

        {/* Con sesión */}
        {user && (
          <div className="relative">
            <button
              onClick={() => { setUserOpen(!userOpen); setNotifOpen(false); }}
              className="flex items-center gap-2 px-2 py-1.5 rounded-lg bg-[#0a1628] border border-[#1e3a5f] hover:border-[#334155] transition-colors"
            >
              <div className={`w-6 h-6 rounded-full bg-gradient-to-br ${roleAvatar[user.role]} flex items-center justify-center text-white text-xs font-semibold shrink-0`}>
                {initials}
              </div>
              <div className="hidden sm:flex items-center gap-1.5">
                <span className="text-[#94a3b8] text-xs">{user.name.split(" ")[0]}</span>
                <span className={`text-xs px-1.5 py-0.5 rounded-full border ${roleBadge[user.role]}`}>
                  {roleLabels[user.role]}
                </span>
              </div>
              <ChevronDown className="w-3.5 h-3.5 text-[#4a7aa8]" />
            </button>

            {userOpen && (
              <div className="absolute right-0 top-11 w-52 bg-[#0f1f35] border border-[#1e3a5f] rounded-xl shadow-2xl shadow-black/50 z-50 overflow-hidden">
                <div className="px-4 py-3 border-b border-[#1e3a5f]">
                  <div className="text-[#94a3b8] text-xs font-medium">{user.name}</div>
                  <div className="text-[#3a5a78] text-xs truncate">{user.email}</div>
                  <span className={`inline-flex mt-1.5 text-xs px-2 py-0.5 rounded-full border ${roleBadge[user.role]}`}>
                    {roleLabels[user.role]}
                  </span>
                </div>
                <div className="p-1">
                  <button className="w-full flex items-center gap-2 px-3 py-2 rounded-lg text-[#64748b] hover:text-[#94a3b8] hover:bg-[#0a1628] text-xs transition-colors">
                    <User className="w-3.5 h-3.5" /> Mi perfil
                  </button>
                  <button
                    onClick={() => { setUserOpen(false); onLogout(); }}
                    className="w-full flex items-center gap-2 px-3 py-2 rounded-lg text-red-400/70 hover:text-red-400 hover:bg-red-500/10 text-xs transition-colors mt-1 border-t border-[#1e3a5f] pt-2"
                  >
                    <LogOut className="w-3.5 h-3.5" /> Cerrar sesión
                  </button>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </header>
  );
}
