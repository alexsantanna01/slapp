import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Availability from './availability';
import AvailabilityDetail from './availability-detail';
import AvailabilityUpdate from './availability-update';
import AvailabilityDeleteDialog from './availability-delete-dialog';

const AvailabilityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Availability />} />
    <Route path="new" element={<AvailabilityUpdate />} />
    <Route path=":id">
      <Route index element={<AvailabilityDetail />} />
      <Route path="edit" element={<AvailabilityUpdate />} />
      <Route path="delete" element={<AvailabilityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AvailabilityRoutes;
