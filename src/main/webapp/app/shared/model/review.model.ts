import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IStudio } from 'app/shared/model/studio.model';
import { IReservation } from 'app/shared/model/reservation.model';

export interface IReview {
  id?: number;
  rating?: number;
  comment?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  customer?: IUserProfile;
  studio?: IStudio;
  reservation?: IReservation | null;
}

export const defaultValue: Readonly<IReview> = {};
