import { useState } from "react";
import { Shield, Eye, EyeOff, X, Loader2, CheckCircle, AlertTriangle, User, Mail, Lock } from "lucide-react";
import type { AppUser, UserRole } from "../App";

interface AuthModalProps {
  onSuccess: (user: AppUser) => void;
  onClose:   () => void;
}

type Tab = "login" | "register";

/* Credenciales de demo — email → rol */
const DEMO_USERS: Record<string, { password: string; role: UserRole; name: string }> = {
  "admin@dagrd.gov.co":    { password: "admin123",    role: "admin",    name: "Carlos Mendoza"  },
  "analista@dagrd.gov.co": { password: "analista123", role: "analyst",  name: "Luisa Fernández" },
  "operador@dagrd.gov.co": { password: "operador123", role: "operator", name: "Pedro García"    },
};

const roleLabels: Record<UserRole, string> = {
  admin:    "Administrador",
  analyst:  "Analista",
  operator: "Operador",
  citizen:  "Ciudadano",
};

export function AuthModal({ onSuccess, onClose }: AuthModalProps) {
  const [tab,      setTab]      = useState<Tab>("login");
  const [email,    setEmail]    = useState("");
  const [name,     setName]     = useState("");
  const [password, setPassword] = useState("");
  const [confirm,  setConfirm]  = useState("");
  const [showPass, setShowPass] = useState(false);
  const [loading,  setLoading]  = useState(false);
  const [error,    setError]    = useState("");
  const [done,     setDone]     = useState(false);
  const [doneUser, setDoneUser] = useState<AppUser | null>(null);

  const reset = () => setError("");

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    reset();
    if (!email || !password) { setError("Completa todos los campos."); return; }
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      const demo = DEMO_USERS[email.toLowerCase()];
      if (demo && demo.password === password) {
        const u: AppUser = { name: demo.name, email, role: demo.role };
        setDoneUser(u);
        setDone(true);
        setTimeout(() => onSuccess(u), 900);
      } else if (!demo && password.length >= 4) {
        /* Cualquier otro correo válido → ciudadano */
        const u: AppUser = { name: email.split("@")[0], email, role: "citizen" };
        setDoneUser(u);
        setDone(true);
        setTimeout(() => onSuccess(u), 900);
      } else {
        setError("Credenciales incorrectas. Revisa tu correo y contraseña.");
      }
    }, 1100);
  };

  const handleRegister = (e: React.FormEvent) => {
    e.preventDefault();
    reset();
    if (!name || !email || !password || !confirm) { setError("Completa todos los campos."); return; }
    if (password !== confirm) { setError("Las contraseñas no coinciden."); return; }
    if (password.length < 6)  { setError("La contraseña debe tener mínimo 6 caracteres."); return; }
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      const u: AppUser = { name, email, role: "citizen" };
      setDoneUser(u);
      setDone(true);
      setTimeout(() => onSuccess(u), 900);
    }, 1200);
  };

  return (
    <div className="fixed inset-0 bg-black/75 backdrop-blur-sm z-50 flex items-center justify-center p-4" onClick={onClose}>
      <div className="bg-[#0f1f35] border border-[#1e3a5f] rounded-2xl w-full max-w-sm shadow-2xl overflow-hidden" onClick={e => e.stopPropagation()}>

        {done && doneUser ? (
          <div className="p-8 flex flex-col items-center gap-3 text-center">
            <div className="w-14 h-14 rounded-full bg-green-500/20 border border-green-500/40 flex items-center justify-center">
              <CheckCircle className="w-7 h-7 text-green-400" />
            </div>
            <div className="text-[#f1f5f9] text-sm font-semibold">
              {tab === "login" ? `¡Bienvenido, ${doneUser.name.split(" ")[0]}!` : "¡Cuenta creada!"}
            </div>
            <span className={`px-3 py-1 rounded-full text-xs font-medium border ${roleBadge(doneUser.role)}`}>
              {roleLabels[doneUser.role]}
            </span>
            <p className="text-[#64748b] text-xs">Cargando tu sesión…</p>
          </div>
        ) : (
          <>
            {/* Header */}
            <div className="px-5 py-4 border-b border-[#1e3a5f] flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-blue-600 to-blue-800 flex items-center justify-center shrink-0">
                  <Shield className="w-4 h-4 text-white" />
                </div>
                <div>
                  <div className="text-[#f1f5f9] text-sm font-semibold">TerritorioAlerta</div>
                  <div className="text-[#3a5a78] text-xs">Acceso al sistema</div>
                </div>
              </div>
              <button onClick={onClose} className="text-[#3a5a78] hover:text-[#94a3b8] transition-colors">
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Tabs */}
            <div className="flex border-b border-[#1e3a5f]">
              {(["login", "register"] as Tab[]).map(t => (
                <button key={t} onClick={() => { setTab(t); reset(); }}
                  className={`flex-1 py-3 text-xs font-medium transition-colors border-b-2 ${
                    tab === t ? "text-blue-400 border-blue-500" : "text-[#4a7aa8] border-transparent hover:text-[#94a3b8]"
                  }`}>
                  {t === "login" ? "Iniciar sesión" : "Crear cuenta"}
                </button>
              ))}
            </div>

            <div className="p-5">
              {tab === "login" ? (
                <form onSubmit={handleLogin} className="flex flex-col gap-4">
                  <Field icon={Mail}  label="Correo electrónico" type="email"    value={email}    onChange={setEmail}    placeholder="correo@ejemplo.com" />
                  <FieldPass label="Contraseña" value={password} onChange={setPassword} show={showPass} onToggle={() => setShowPass(!showPass)} />
                  {error && <ErrorMsg text={error} />}
                  <button type="submit" disabled={loading}
                    className="w-full bg-blue-600 hover:bg-blue-500 disabled:opacity-60 text-white rounded-xl py-2.5 text-sm font-medium flex items-center justify-center gap-2 transition-colors">
                    {loading ? <><Loader2 className="w-4 h-4 animate-spin" />Verificando…</> : "Ingresar"}
                  </button>

                  {/* Demo credentials */}
                  <div className="bg-[#0a1628] border border-[#1e3a5f] rounded-xl p-3">
                    <div className="text-[#3a5a78] text-xs mb-2 uppercase tracking-wide">Credenciales de prueba</div>
                    <div className="flex flex-col gap-1.5">
                      {Object.entries(DEMO_USERS).map(([em, u]) => (
                        <button key={em} type="button"
                          onClick={() => { setEmail(em); setPassword(u.password); reset(); }}
                          className="flex items-center justify-between px-2 py-1.5 rounded-lg hover:bg-[#1e293b] transition-colors text-left group">
                          <span className="text-[#64748b] text-xs group-hover:text-[#94a3b8] truncate">{em}</span>
                          <span className={`text-xs px-2 py-0.5 rounded-full border shrink-0 ml-2 ${roleBadge(u.role)}`}>
                            {roleLabels[u.role]}
                          </span>
                        </button>
                      ))}
                      <div className="text-[#2a4a6a] text-xs pt-1 border-t border-[#1e3a5f] mt-1">
                        Otro correo + 4+ chars → Ciudadano
                      </div>
                    </div>
                  </div>
                </form>
              ) : (
                <form onSubmit={handleRegister} className="flex flex-col gap-3.5">
                  <Field icon={User} label="Nombre completo"    type="text"  value={name}     onChange={setName}     placeholder="Tu nombre"          />
                  <Field icon={Mail} label="Correo electrónico" type="email" value={email}    onChange={setEmail}    placeholder="correo@ejemplo.com"  />
                  <FieldPass label="Contraseña" value={password} onChange={setPassword} show={showPass} onToggle={() => setShowPass(!showPass)} />
                  <div>
                    <label className="block text-[#94a3b8] text-xs mb-1.5 uppercase tracking-wide">Confirmar contraseña</label>
                    <div className="relative">
                      <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-[#3a5a78]" />
                      <input type={showPass ? "text" : "password"} value={confirm} onChange={e => setConfirm(e.target.value)} placeholder="••••••"
                        className="w-full bg-[#0a1628] border border-[#334155] rounded-xl pl-9 pr-4 py-2.5 text-[#e2e8f0] placeholder-[#3a5a78] text-sm outline-none focus:border-blue-500 transition-colors" />
                    </div>
                  </div>
                  <div className="bg-[#0a1628] border border-[#1e3a5f] rounded-xl px-3 py-2.5 flex items-center gap-2">
                    <User className="w-3.5 h-3.5 text-[#3a5a78] shrink-0" />
                    <p className="text-[#3a5a78] text-xs">Las cuentas nuevas se crean con rol <span className="text-[#64748b]">Ciudadano</span>. Un administrador puede cambiar el rol.</p>
                  </div>
                  {error && <ErrorMsg text={error} />}
                  <button type="submit" disabled={loading}
                    className="w-full bg-blue-600 hover:bg-blue-500 disabled:opacity-60 text-white rounded-xl py-2.5 text-sm font-medium flex items-center justify-center gap-2 transition-colors">
                    {loading ? <><Loader2 className="w-4 h-4 animate-spin" />Creando cuenta…</> : "Crear cuenta"}
                  </button>
                  <button type="button" onClick={() => { setTab("login"); reset(); }}
                    className="text-center text-[#4a7aa8] text-xs hover:text-blue-300 transition-colors">
                    ¿Ya tienes cuenta? <span className="text-blue-400 underline">Inicia sesión</span>
                  </button>
                </form>
              )}
            </div>
          </>
        )}
      </div>
    </div>
  );
}

function roleBadge(role: UserRole): string {
  return {
    admin:    "bg-purple-500/20 text-purple-400 border-purple-500/40",
    analyst:  "bg-blue-500/20   text-blue-400   border-blue-500/40",
    operator: "bg-cyan-500/20   text-cyan-400   border-cyan-500/40",
    citizen:  "bg-green-500/20  text-green-400  border-green-500/40",
  }[role];
}

function Field({ icon: Icon, label, type, value, onChange, placeholder }: {
  icon: typeof Mail; label: string; type: string; value: string;
  onChange: (v: string) => void; placeholder: string;
}) {
  return (
    <div>
      <label className="block text-[#94a3b8] text-xs mb-1.5 uppercase tracking-wide">{label}</label>
      <div className="relative">
        <Icon className="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-[#3a5a78]" />
        <input type={type} value={value} onChange={e => onChange(e.target.value)} placeholder={placeholder}
          className="w-full bg-[#0a1628] border border-[#334155] rounded-xl pl-9 pr-4 py-2.5 text-[#e2e8f0] placeholder-[#3a5a78] text-sm outline-none focus:border-blue-500 transition-colors" />
      </div>
    </div>
  );
}

function FieldPass({ label, value, onChange, show, onToggle }: {
  label: string; value: string; onChange: (v: string) => void; show: boolean; onToggle: () => void;
}) {
  return (
    <div>
      <label className="block text-[#94a3b8] text-xs mb-1.5 uppercase tracking-wide">{label}</label>
      <div className="relative">
        <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-[#3a5a78]" />
        <input type={show ? "text" : "password"} value={value} onChange={e => onChange(e.target.value)} placeholder="••••••"
          className="w-full bg-[#0a1628] border border-[#334155] rounded-xl pl-9 pr-10 py-2.5 text-[#e2e8f0] placeholder-[#3a5a78] text-sm outline-none focus:border-blue-500 transition-colors" />
        <button type="button" onClick={onToggle}
          className="absolute right-3 top-1/2 -translate-y-1/2 text-[#3a5a78] hover:text-[#94a3b8] transition-colors">
          {show ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
        </button>
      </div>
    </div>
  );
}

function ErrorMsg({ text }: { text: string }) {
  return (
    <div className="flex items-center gap-2 bg-red-500/10 border border-red-500/30 rounded-lg px-3 py-2">
      <AlertTriangle className="w-4 h-4 text-red-400 shrink-0" />
      <span className="text-red-400 text-xs">{text}</span>
    </div>
  );
}
