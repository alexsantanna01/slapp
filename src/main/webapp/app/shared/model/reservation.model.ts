import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IRoom } from 'app/shared/model/room.model';
import { ReservationStatus } from 'app/shared/model/enumerations/reservation-status.model';

export interface IReservation {
  id?: number;
  startDateTime?: dayjs.Dayjs;
  endDateTime?: dayjs.Dayjs;
  totalPrice?: number;
  status?: keyof typeof ReservationStatus;
  notes?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  cancelledAt?: dayjs.Dayjs | null;
  cancelReason?: string | null;
  customer?: IUserProfile;
  room?: IRoom;
}

export const defaultValue: Readonly<IReservation> = {};
