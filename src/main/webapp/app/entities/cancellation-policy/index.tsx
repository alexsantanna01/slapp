import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CancellationPolicy from './cancellation-policy';
import CancellationPolicyDetail from './cancellation-policy-detail';
import CancellationPolicyUpdate from './cancellation-policy-update';
import CancellationPolicyDeleteDialog from './cancellation-policy-delete-dialog';

const CancellationPolicyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CancellationPolicy />} />
    <Route path="new" element={<CancellationPolicyUpdate />} />
    <Route path=":id">
      <Route index element={<CancellationPolicyDetail />} />
      <Route path="edit" element={<CancellationPolicyUpdate />} />
      <Route path="delete" element={<CancellationPolicyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CancellationPolicyRoutes;
