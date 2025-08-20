import { IRoom } from 'app/shared/model/room.model';
import { DayOfWeek } from 'app/shared/model/enumerations/day-of-week.model';

export interface ISpecialPrice {
  id?: number;
  dayOfWeek?: keyof typeof DayOfWeek | null;
  startTime?: string | null;
  endTime?: string | null;
  price?: number;
  description?: string | null;
  active?: boolean;
  room?: IRoom;
}

export const defaultValue: Readonly<ISpecialPrice> = {
  active: false,
};
