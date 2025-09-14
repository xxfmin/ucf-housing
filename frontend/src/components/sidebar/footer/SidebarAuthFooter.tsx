"use client";

import { useState } from "react";
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";
import { SidebarUserButton } from "@/components/sidebar/footer/SidebarUserButton";
import { LoginModal } from "@/components/auth/LoginModal";
import { useAuth } from "@/contexts/AuthContext";
import { LogInIcon } from "lucide-react";

export function SidebarAuthFooter() {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <SidebarMenu>
        <div className="flex items-center gap-2 p-2">
          <div className="w-8 h-8 bg-muted rounded-lg animate-pulse" />
          <div className="flex flex-col gap-1 flex-1">
            <div className="h-3 bg-muted rounded animate-pulse" />
            <div className="h-2 bg-muted rounded animate-pulse w-3/4" />
          </div>
        </div>
      </SidebarMenu>
    );
  }

  return (
    <SidebarMenu>
      {isAuthenticated ? <SidebarUserButton /> : <GuestButtons />}
    </SidebarMenu>
  );
}

function GuestButtons() {
  const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);

  return (
    <>
      <SidebarMenuItem>
        <SidebarMenuButton
          size="lg"
          className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground cursor-pointer"
          onClick={() => setIsLoginModalOpen(true)}
        >
          <div className="flex items-center gap-2 overflow-hidden">
            <div className="rounded-lg size-8 bg-primary flex items-center justify-center">
              <LogInIcon className="size-4 text-primary-foreground" />
            </div>
            <div className="flex flex-col flex-1 min-w-0 leading-tight group-data-[state=collapsed]:hidden">
              <span className="truncate text-sm font-semibold">Log In</span>
              <span className="truncate text-xs">Sign in to your account</span>
            </div>
          </div>
        </SidebarMenuButton>
      </SidebarMenuItem>

      <LoginModal
        isOpen={isLoginModalOpen}
        onClose={() => setIsLoginModalOpen(false)}
      />
    </>
  );
}
