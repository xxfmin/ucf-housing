"use client";

import { useIsMobile } from "@/hooks/use-mobile";
import { SidebarMenuButton } from "../../ui/sidebar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../../ui/dropdown-menu";
import {
  ChevronsUpDown,
  LogOutIcon,
  SettingsIcon,
  UserIcon,
} from "lucide-react";
import { Avatar, AvatarFallback } from "../../ui/avatar";
import { useAuth } from "@/contexts/AuthContext";
import Link from "next/link";

type User = {
  fullName: string | undefined;
  email: string | undefined;
};

export function SidebarUserButtonClient({ user }: { user: User }) {
  const isMobile = useIsMobile();
  const { logout } = useAuth();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <SidebarMenuButton
          size="lg"
          className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
        >
          <UserInfo {...user} />
          <ChevronsUpDown className="ml-auto group-data-[state=collapsed]:hidden" />
        </SidebarMenuButton>
      </DropdownMenuTrigger>
      <DropdownMenuContent
        sideOffset={4}
        align="end"
        side={isMobile ? "bottom" : "right"}
        className="min-w-64 max-w-80"
      >
        <DropdownMenuLabel className="font-normal p-1">
          <UserInfo {...user} />
        </DropdownMenuLabel>

        <DropdownMenuSeparator />

        <DropdownMenuItem onClick={() => OpenUserProfile()}>
          <UserIcon className="mr-1" /> Profile
        </DropdownMenuItem>

        <DropdownMenuItem asChild>
          <Link href="/user/settings">
            <SettingsIcon className="mr-1" /> Settings
          </Link>
        </DropdownMenuItem>

        <DropdownMenuSeparator />

        <DropdownMenuItem onClick={() => logout()}>
          <LogOutIcon className="mr-1" /> Log Out
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

function UserInfo({ fullName, email }: User) {
  let nameInitials = "??";
  if (fullName) {
    nameInitials = fullName
      .split(" ")
      .slice(0, 2)
      .map((str) => str[0])
      .join("");
  }

  return (
    <div className="flex items-center gap-2 overflow-hidden">
      <Avatar className="rounded-lg size-8">
        <AvatarFallback className="uppercase bg-primary text-primary-foreground">
          {nameInitials}
        </AvatarFallback>
      </Avatar>
      <div className="flex flex-col flex-1 min-w-0 leading-tight group-data-[state=collapsed]:hidden">
        <span className="truncate text-sm font-semibold">{fullName}</span>
        <span className="truncate text-xs">{email}</span>
      </div>
    </div>
  );
}
