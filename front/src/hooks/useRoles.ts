"use client";

import { useContext, useCallback, useMemo } from "react";
import type { RoleKey } from "@/types/menu";
import { AuthContext } from "@/contexts/AuthContext";

interface UseRolesReturn {
  roles: RoleKey[];
  hasRole: (role: RoleKey) => boolean;
  hasAnyRole: (roles: RoleKey[]) => boolean;
  hasAllRoles: (roles: RoleKey[]) => boolean;
  isAdmin: boolean;
  isBarber: boolean;
  isSecretary: boolean;
  canAccess: (requiredRoles?: RoleKey[], requireAll?: boolean) => boolean;
}

export function useRoles(): UseRolesReturn {
  const context = useContext(AuthContext);

  const roles = useMemo(
    () => context?.user?.roles || [],
    [context?.user?.roles],
  );

  const hasRole = useCallback(
    (role: RoleKey): boolean => roles.includes(role),
    [roles],
  );

  const hasAnyRole = useCallback(
    (requiredRoles: RoleKey[]): boolean =>
      requiredRoles.some((role) => roles.includes(role)),
    [roles],
  );

  const hasAllRoles = useCallback(
    (requiredRoles: RoleKey[]): boolean =>
      requiredRoles.every((role) => roles.includes(role)),
    [roles],
  );

  const canAccess = useCallback(
    (requiredRoles?: RoleKey[], requireAll: boolean = false): boolean => {
      if (!requiredRoles || requiredRoles.length === 0) return true;
      if (roles.includes("ADMIN")) return true;
      return requireAll
        ? hasAllRoles(requiredRoles)
        : hasAnyRole(requiredRoles);
    },
    [roles, hasAllRoles, hasAnyRole],
  );

  const isAdmin = useMemo(() => hasRole("ADMIN"), [hasRole]);
  const isBarber = useMemo(() => hasAnyRole(["ADMIN", "BARBER"]), [hasAnyRole]);
  const isSecretary = useMemo(
    () => hasAnyRole(["ADMIN", "SECRETARY"]),
    [hasAnyRole],
  );

  return {
    roles,
    hasRole,
    hasAnyRole,
    hasAllRoles,
    isAdmin,
    isBarber,
    isSecretary,
    canAccess,
  };
}
