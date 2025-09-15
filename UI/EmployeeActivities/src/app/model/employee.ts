import { Activity } from "./activity";

export interface Employee {
  id: number;
  firstName: string;
  lastName: string;
  activityList: Activity[];
}