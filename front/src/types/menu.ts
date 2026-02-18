import type { SvgIconComponent } from "@mui/icons-material";

export type ModuleKey = "gobarber";

export type RoleKey = "ADMIN" | "BARBER" | "SECRETARY";

export interface MenuItem {
  id: string;
  label: string;
  path?: string;
  icon?: SvgIconComponent | React.ComponentType;
  roles?: RoleKey[];
  children?: MenuItem[];
  badge?: string | number;
  disabled?: boolean;
  external?: boolean;
  divider?: boolean;
}

export interface MenuGroup {
  id: string;
  title: string;
  icon?: SvgIconComponent | React.ComponentType;
  items: MenuItem[];
  collapsible?: boolean;
  defaultOpen?: boolean;
}

export interface ModuleMenuConfig {
  moduleKey: ModuleKey;
  title: string;
  basePath: string;
  icon?: SvgIconComponent | React.ComponentType;
  groups: MenuGroup[];
}

export interface MenuState {
  isOpen: boolean;
  expandedGroups: string[];
  activeItem: string | null;
}

export interface BreadcrumbItem {
  label: string;
  path?: string;
  icon?: SvgIconComponent | React.ComponentType;
}
