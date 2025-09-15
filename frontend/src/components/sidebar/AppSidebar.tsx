"use client";

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarProvider,
  SidebarTrigger,
} from "@/components/ui/sidebar";
import { AppSidebarClient } from "@/components/sidebar/_AppSidebarClient";
import { SidebarAuthFooter } from "@/components/sidebar/footer/SidebarAuthFooter";
import { ReactNode } from "react";

// Separate component that uses the useSidebar hook
function SidebarWithContent() {
  return (
    <Sidebar collapsible="icon" className="overflow-y-hidden h-screen">
      {/* Header */}
      <SidebarHeader className="flex-row items-center">
        <SidebarTrigger />
        <span className="text-xl font-bold text-nowrap ml-2">
          Knight Housing
        </span>
      </SidebarHeader>

      {/* Content */}
      <SidebarContent className="flex-1 overflow-hidden">
        <h1>hey</h1>
      </SidebarContent>

      {/* Footer */}
      <SidebarFooter className="shrink-0 border-t">
        <SidebarAuthFooter />
      </SidebarFooter>
    </Sidebar>
  );
}

export function AppSidebar({ children }: { children: ReactNode }) {
  return (
    <SidebarProvider className="overflow-y-hidden">
      <AppSidebarClient>
        <SidebarWithContent />
        <main className="flex-1 overflow-hidden">{children}</main>
      </AppSidebarClient>
    </SidebarProvider>
  );
}
