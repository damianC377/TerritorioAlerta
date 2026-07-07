import { useState } from "react";
import { Sidebar } from "./Sidebar";
import { Header } from "./Header";
import { Dashboard } from "../screens/Dashboard";
import { MapView } from "../screens/MapView";
import { Analytics } from "../screens/Analytics";
import { Alerts } from "../screens/Alerts";
import { DataSources } from "../screens/DataSources";
import { Admin } from "../screens/Admin";
import { AuthModal } from "../AuthModal";
import type { Screen, AppUser } from "../../App";

interface MainLayoutProps {
  currentScreen: Screen;
  onNavigate:    (s: Screen) => void;
  user:          AppUser | null;
  onLogin:       (u: AppUser) => void;
  onLogout:      () => void;
}

export function MainLayout({ currentScreen, onNavigate, user, onLogin, onLogout }: MainLayoutProps) {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [authModalOpen,  setAuthModalOpen]  = useState(false);
  const [pendingAction,  setPendingAction]  = useState<(() => void) | null>(null);

  const openAuth = (afterLogin?: () => void) => {
    setPendingAction(() => afterLogin ?? null);
    setAuthModalOpen(true);
  };

  const handleAuthSuccess = (u: AppUser) => {
    onLogin(u);
    setAuthModalOpen(false);
    if (pendingAction) { pendingAction(); setPendingAction(null); }
  };

  /* Si el usuario navegó a una pantalla sin permiso, vuelve al dashboard */
  const safeNavigate = (s: Screen) => {
    if (!canAccess(user, s)) onNavigate("dashboard");
    else onNavigate(s);
  };

  return (
    <div className="flex h-screen bg-[#060f1e] overflow-hidden">
      <Sidebar
        current={currentScreen}
        onNavigate={safeNavigate}
        mobileOpen={mobileMenuOpen}
        onMobileClose={() => setMobileMenuOpen(false)}
        user={user}
      />

      <div className="flex-1 flex flex-col overflow-hidden min-w-0">
        <Header
          currentScreen={currentScreen}
          onMenuToggle={() => setMobileMenuOpen(true)}
          user={user}
          onOpenAuth={() => openAuth()}
          onLogout={onLogout}
        />

        <main className="flex-1 overflow-y-auto overflow-x-hidden">
          {currentScreen === "dashboard" && (
            <Dashboard
              onNavigate={safeNavigate}
              user={user}
              onRequireAuth={openAuth}
            />
          )}
          {currentScreen === "map"       && <MapView />}
          {currentScreen === "analytics" && canAccess(user, "analytics") && <Analytics />}
          {currentScreen === "alerts"    && <Alerts />}
          {currentScreen === "sources"   && canAccess(user, "sources")   && <DataSources />}
          {currentScreen === "admin"     && canAccess(user, "admin")     && <Admin />}
        </main>
      </div>

      {authModalOpen && (
        <AuthModal
          onSuccess={handleAuthSuccess}
          onClose={() => { setAuthModalOpen(false); setPendingAction(null); }}
        />
      )}
    </div>
  );
}

/* Qué pantallas puede ver cada rol */
export function canAccess(user: AppUser | null, screen: Screen): boolean {
  if (screen === "dashboard" || screen === "map" || screen === "alerts") return true;
  if (!user) return false;
  if (user.role === "admin")    return true;
  if (user.role === "analyst")  return screen === "analytics" || screen === "sources";
  return false;
}
