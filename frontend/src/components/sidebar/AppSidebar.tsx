"use client";

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarProvider,
  SidebarTrigger,
  useSidebar,
} from "@/components/ui/sidebar";
import { AppSidebarClient } from "@/app/_AppSidebarClient";
import Link from "next/link";
import { FilterIcon, LogInIcon } from "lucide-react";
import { SidebarAuthFooter } from "@/components/sidebar/footer/SidebarAuthFooter";
import { ReactNode } from "react";
import { FilterState, SidebarFilters } from "./filters/SidebarFilters";

// Separate component that uses the useSidebar hook
function SidebarWithContent() {
  const { state } = useSidebar();
  const isCollapsed = state === "collapsed";

  const handleFiltersChange = (filters: FilterState) => {
    console.log("Filters changed:", filters);
    // TODO: Update your property search results here
  };

  const handleApplyFilters = (filters: FilterState) => {
    console.log("Apply filters:", filters);
    // TODO: Trigger search with the applied filters
  };

  return (
    <Sidebar collapsible="icon" className="overflow-y-hidden h-screen">
      {/* Header */}
      <SidebarHeader className="flex-row">
        <SidebarTrigger />
        <span className="text-xl font-bold text-nowrap ml-2">UCF Housing</span>
      </SidebarHeader>

      {/* Content */}
      <SidebarContent className="flex-1 overflow-hidden">
        <SidebarGroup className="flex-1 overflow-hidden">
          <SidebarGroupLabel className="text-md font-semibold pb-2">
            <FilterIcon className="mr-1" /> Property Filters
          </SidebarGroupLabel>
          <SidebarGroupContent className="h-full overflow-y-auto px-4 py-2">
            <SidebarFilters
              onFiltersChange={handleFiltersChange}
              onApplyFilters={handleApplyFilters}
            />
          </SidebarGroupContent>
        </SidebarGroup>
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
