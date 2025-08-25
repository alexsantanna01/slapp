import { IUser } from 'app/shared/model/user.model';
import { IStudio } from 'app/shared/model/studio.model';

export interface IFavorite {
  id: number;
  createdDate?: string;
  user?: IUser;
  studio?: IStudio;
}

export const defaultValue: Readonly<IFavorite> = {
  id: 0,
};
