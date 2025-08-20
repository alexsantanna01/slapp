import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { UserType } from 'app/shared/model/enumerations/user-type.model';

export interface IUserProfile {
  id?: number;
  phone?: string | null;
  profileImage?: string | null;
  userType?: keyof typeof UserType;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  user?: IUser;
}

export const defaultValue: Readonly<IUserProfile> = {};
