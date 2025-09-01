import React, { useLayoutEffect } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';

export const Logout = () => {
  const authentication = useAppSelector(state => state.authentication);
  const dispatch = useAppDispatch();

  useLayoutEffect(() => {
    dispatch(logout());

    setTimeout(() => {
      if (authentication.logoutUrl) {
        window.location.href = authentication.logoutUrl;
      } else {
        window.location.href = '/';
      }
    }, 5000);
  }, []);

  return (
    <div className="p-5">
      <h4>Logged out successfully!</h4>
    </div>
  );
};

export default Logout;
