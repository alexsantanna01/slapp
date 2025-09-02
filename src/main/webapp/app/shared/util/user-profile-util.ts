import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export const useCurrentUserProfile = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const loading = useAppSelector(state => state.userProfile.loading);

  const [userProfile, setUserProfile] = useState<IUserProfile | null>(null);
  const [profileLoading, setProfileLoading] = useState(true);

  useEffect(() => {
    if (account?.id && userProfiles.length === 0) {
      dispatch(getUserProfiles({}));
    }
  }, [dispatch, account, userProfiles.length]);

  useEffect(() => {
    if (account?.id && userProfiles.length > 0) {
      const profile = userProfiles.find(p => p.user?.id === account.id);
      setUserProfile(profile || null);
      setProfileLoading(false);
    } else if (!account?.id) {
      setUserProfile(null);
      setProfileLoading(false);
    }
  }, [account, userProfiles]);

  return {
    userProfile,
    userType: userProfile?.userType || null,
    loading: loading || profileLoading,
  };
};
