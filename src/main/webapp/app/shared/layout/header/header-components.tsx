import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import EqualizerIcon from '@mui/icons-material/Equalizer';
import SpeakerIcon from '@mui/icons-material/Speaker';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">Slapp</Translate>
    </span>
    <span className="navbar-version">{VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/studio" className="d-flex align-items-center">
      <SpeakerIcon />
      <span>Estúdios</span>
    </NavLink>
  </NavItem>
);
export const Dashboard = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <EqualizerIcon />
      <span>Dashboard</span>
    </NavLink>
  </NavItem>
);
