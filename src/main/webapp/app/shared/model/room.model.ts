import dayjs from 'dayjs';
import { IStudio } from 'app/shared/model/studio.model';
import { RoomType } from 'app/shared/model/enumerations/room-type.model';

export interface IRoom {
  id?: number;
  name?: string;
  description?: string | null;
  hourlyRate?: number;
  capacity?: number | null;
  soundproofed?: boolean | null;
  airConditioning?: boolean | null;
  roomType?: keyof typeof RoomType;
  active?: boolean;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  studio?: IStudio;
}

export const defaultValue: Readonly<IRoom> = {
  soundproofed: false,
  airConditioning: false,
  active: false,
};
