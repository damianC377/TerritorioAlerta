import { useState } from "react";
import { MainLayout } from "./components/layout/MainLayout";

export type Screen    = "dashboard" | "map" | "alerts" | "analytics" | "sources" | "admin";
export type UserRole  = "admin" | "analyst" | "operator" | "citizen";

export interface AppUser {
  name:  string;
  email: string;
  role:  UserRole;
}

export default function App() {
  const [user, setUser]               = useState<AppUser | null>(null);
  const [currentScreen, setCurrentScreen] = useState<Screen>("dashboard");

  return (
    <MainLayout
      currentScreen={currentScreen}
      onNavigate={setCurrentScreen}
      user={user}
      onLogin={setUser}
      onLogout={() => setUser(null)}
    />
  );
}
