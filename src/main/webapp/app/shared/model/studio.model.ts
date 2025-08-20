import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ICancellationPolicy } from 'app/shared/model/cancellation-policy.model';

export interface IStudio {
  id?: number;
  name?: string;
  description?: string | null;
  address?: string;
  city?: string;
  state?: string;
  zipCode?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  phone?: string | null;
  email?: string | null;
  website?: string | null;
  image?: string | null;
  active?: boolean;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  owner?: IUserProfile;
  cancellationPolicy?: ICancellationPolicy | null;
}

export const defaultValue: Readonly<IStudio> = {
  active: false,
};
