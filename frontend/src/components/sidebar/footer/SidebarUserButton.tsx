"use client";

import { useAuth } from "@/contexts/AuthContext";
import { SidebarUserButtonClient } from "./_SidebarUserButtonClient";

export function SidebarUserButton() {
  const { user, isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="flex items-center gap-2 p-2">
        <div className="w-8 h-8 bg-muted rounded-lg animate-pulse" />
        <div className="flex flex-col gap-1 flex-1">
          <div className="h-3 bg-muted rounded animate-pulse" />
          <div className="h-2 bg-muted rounded animate-pulse w-3/4" />
        </div>
      </div>
    );
  }

  if (!isAuthenticated || !user) {
    return <div>Please log in</div>;
  }

  const userForClient = {
    fullName: user.username,
    email: user.email,
  };

  return <SidebarUserButtonClient user={userForClient} />;
}
