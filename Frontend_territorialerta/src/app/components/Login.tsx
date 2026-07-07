import { useState } from "react";
import { Shield, Eye, EyeOff, AlertTriangle, Loader2 } from "lucide-react";

interface LoginProps {
  onLogin: () => void;
}

export function Login({ onLogin }: LoginProps) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPass, setShowPass] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    if (!email || !password) { setError("Completa todos los campos."); return; }
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      if (password === "admin123" || password.length >= 4) {
        onLogin();
      } else {
        setError("Credenciales inválidas. Intenta de nuevo.");
      }
    }, 1200);
  };

  return (
    <div className="min-h-screen bg-[#060f1e] flex items-center justify-center p-4 relative overflow-hidden">
      {/* Background grid */}
      <div className="absolute inset-0 opacity-10"
           style={{ backgroundImage: "linear-gradient(#1e3a5f 1px, transparent 1px), linear-gradient(90deg, #1e3a5f 1px, transparent 1px)", backgroundSize: "40px 40px" }} />

      {/* Glow blobs */}
      <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-blue-600/10 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-1/4 right-1/4 w-64 h-64 bg-cyan-600/8 rounded-full blur-3xl pointer-events-none" />

      <div className="relative z-10 w-full max-w-sm">
        {/* Logo */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-br from-blue-600 to-blue-800 shadow-lg shadow-blue-900/50 mb-4">
            <Shield className="w-8 h-8 text-white" />
          </div>
          <h1 className="text-white text-2xl font-semibold tracking-tight">TerritorioAlerta</h1>
          <p className="text-[#4a7aa8] text-sm mt-1">Sistema de Inteligencia Territorial</p>
        </div>

        {/* Card */}
        <div className="bg-[#0f1f35]/80 backdrop-blur-md border border-[#1e3a5f] rounded-2xl p-6">
          <div className="mb-5">
            <h2 className="text-[#e2e8f0] text-base font-medium">Iniciar sesión</h2>
            <p className="text-[#4a7aa8] text-xs mt-0.5">Acceso restringido a personal autorizado</p>
          </div>

          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <div>
              <label className="block text-[#94a3b8] text-xs mb-1.5 uppercase tracking-wide">Usuario / Correo</label>
              <input
                type="text"
                value={email}
                onChange={e => setEmail(e.target.value)}
                placeholder="usuario@dagrd.gov.co"
                className="w-full bg-[#0a1628] border border-[#334155] rounded-lg px-3 py-2.5 text-[#e2e8f0] placeholder-[#3a5a78] text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500/30 transition-colors"
              />
            </div>

            <div>
              <label className="block text-[#94a3b8] text-xs mb-1.5 uppercase tracking-wide">Contraseña</label>
              <div className="relative">
                <input
                  type={showPass ? "text" : "password"}
                  value={password}
                  onChange={e => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="w-full bg-[#0a1628] border border-[#334155] rounded-lg px-3 py-2.5 pr-10 text-[#e2e8f0] placeholder-[#3a5a78] text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500/30 transition-colors"
                />
                <button type="button" onClick={() => setShowPass(!showPass)}
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-[#4a7aa8] hover:text-[#94a3b8] transition-colors">
                  {showPass ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
            </div>

            {error && (
              <div className="flex items-center gap-2 bg-red-500/10 border border-red-500/30 rounded-lg px-3 py-2">
                <AlertTriangle className="w-4 h-4 text-red-400 shrink-0" />
                <span className="text-red-400 text-xs">{error}</span>
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-blue-600 hover:bg-blue-500 disabled:bg-blue-800 disabled:opacity-60 text-white rounded-lg py-2.5 text-sm font-medium flex items-center justify-center gap-2 transition-colors mt-1"
            >
              {loading ? <><Loader2 className="w-4 h-4 animate-spin" />Verificando...</> : "Ingresar al sistema"}
            </button>
          </form>

          <div className="mt-4 pt-4 border-t border-[#1e3a5f] flex items-center justify-between">
            <button className="text-[#3b82f6] text-xs hover:text-blue-300 transition-colors">¿Olvidaste tu contraseña?</button>
            <button className="text-[#4a7aa8] text-xs hover:text-[#94a3b8] transition-colors">Solicitar acceso</button>
          </div>
        </div>

        {/* Footer */}
        <div className="mt-6 text-center">
          <p className="text-[#2a4a6a] text-xs">
            DAGRD · Alcaldía de Medellín · v2.4.1
          </p>
          <p className="text-[#1e3a5f] text-xs mt-1">
            Plataforma de Gestión del Riesgo y Emergencias
          </p>
        </div>

        {/* Demo hint */}
        <div className="mt-4 bg-blue-900/20 border border-blue-800/40 rounded-lg px-4 py-2.5 text-center">
          <p className="text-[#4a7aa8] text-xs">Demo: ingresa cualquier usuario y contraseña (mín. 4 chars)</p>
        </div>
      </div>
    </div>
  );
}
