import { IRoom } from 'app/shared/model/room.model';

export interface IRoomImage {
  id?: number;
  url?: string;
  altText?: string | null;
  displayOrder?: number | null;
  active?: boolean;
  room?: IRoom;
}

export const defaultValue: Readonly<IRoomImage> = {
  active: false,
};
