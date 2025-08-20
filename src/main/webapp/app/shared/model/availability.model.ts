import dayjs from 'dayjs';
import { IRoom } from 'app/shared/model/room.model';

export interface IAvailability {
  id?: number;
  startDateTime?: dayjs.Dayjs;
  endDateTime?: dayjs.Dayjs;
  available?: boolean;
  reason?: string | null;
  createdAt?: dayjs.Dayjs | null;
  room?: IRoom;
}

export const defaultValue: Readonly<IAvailability> = {
  available: false,
};
