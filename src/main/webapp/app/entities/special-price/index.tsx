import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SpecialPrice from './special-price';
import SpecialPriceDetail from './special-price-detail';
import SpecialPriceUpdate from './special-price-update';
import SpecialPriceDeleteDialog from './special-price-delete-dialog';

const SpecialPriceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SpecialPrice />} />
    <Route path="new" element={<SpecialPriceUpdate />} />
    <Route path=":id">
      <Route index element={<SpecialPriceDetail />} />
      <Route path="edit" element={<SpecialPriceUpdate />} />
      <Route path="delete" element={<SpecialPriceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpecialPriceRoutes;
