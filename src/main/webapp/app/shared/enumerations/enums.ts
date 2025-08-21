// src/main/webapp/app/shared/model/enumerations/enums.ts

export enum RoomType {
  RECORDING = 'RECORDING',
  REHEARSAL = 'REHEARSAL',
  BOTH = 'BOTH',
}

export enum EquipmentType {
  MICROPHONE = 'MICROPHONE',
  AMPLIFIER = 'AMPLIFIER',
  DRUMS = 'DRUMS',
  GUITAR = 'GUITAR',
  BASS = 'BASS',
  KEYBOARD = 'KEYBOARD',
  MIXER = 'MIXER',
  MONITOR = 'MONITOR',
  OTHER = 'OTHER',
}

export enum ReservationStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  CANCELLED = 'CANCELLED',
  COMPLETED = 'COMPLETED',
}

export enum UserType {
  CUSTOMER = 'CUSTOMER',
  STUDIO_OWNER = 'STUDIO_OWNER',
  ADMIN = 'ADMIN',
}
