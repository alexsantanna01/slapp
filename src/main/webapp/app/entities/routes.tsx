import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserProfile from './user-profile';
import Studio from './studio';
import Room from './room';
import RoomImage from './room-image';
import Equipment from './equipment';
import Reservation from './reservation';
import Review from './review';
import SpecialPrice from './special-price';
import Availability from './availability';
import CancellationPolicy from './cancellation-policy';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="studio/*" element={<Studio />} />
        <Route path="room/*" element={<Room />} />
        <Route path="room-image/*" element={<RoomImage />} />
        <Route path="equipment/*" element={<Equipment />} />
        <Route path="reservation/*" element={<Reservation />} />
        <Route path="review/*" element={<Review />} />
        <Route path="special-price/*" element={<SpecialPrice />} />
        <Route path="availability/*" element={<Availability />} />
        <Route path="cancellation-policy/*" element={<CancellationPolicy />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
