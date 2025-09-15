import { ActivityType } from "./activity-type";

export interface Activity {
  id: number;
  date: string;
  content?: string;
  comment?: string;
  type: ActivityType;
}