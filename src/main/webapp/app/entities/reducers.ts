import userProfile from 'app/entities/user-profile/user-profile.reducer';
import studio from 'app/entities/studio/studio.reducer';
import room from 'app/entities/room/room.reducer';
import roomImage from 'app/entities/room-image/room-image.reducer';
import equipment from 'app/entities/equipment/equipment.reducer';
import reservation from 'app/entities/reservation/reservation.reducer';
import review from 'app/entities/review/review.reducer';
import specialPrice from 'app/entities/special-price/special-price.reducer';
import availability from 'app/entities/availability/availability.reducer';
import cancellationPolicy from 'app/entities/cancellation-policy/cancellation-policy.reducer';
import favorite from 'app/entities/favorite/favorite.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  studio,
  room,
  roomImage,
  equipment,
  reservation,
  review,
  specialPrice,
  availability,
  cancellationPolicy,
  favorite,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
