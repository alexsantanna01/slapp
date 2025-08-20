import dayjs from 'dayjs';
import { IRoom } from 'app/shared/model/room.model';
import { EquipmentType } from 'app/shared/model/enumerations/equipment-type.model';

export interface IEquipment {
  id?: number;
  name?: string;
  brand?: string | null;
  model?: string | null;
  description?: string | null;
  available?: boolean;
  equipmentType?: keyof typeof EquipmentType;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  room?: IRoom;
}

export const defaultValue: Readonly<IEquipment> = {
  available: false,
};
