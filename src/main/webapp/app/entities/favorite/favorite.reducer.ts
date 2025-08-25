import axios from 'axios';
import { createAsyncThunk, createSlice, isRejected } from '@reduxjs/toolkit';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IFavorite } from 'app/shared/model/favorite.model';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFavorite>,
  entity: {} as Readonly<IFavorite>,
  updating: false,
  updateSuccess: false,
  // Estados específicos para favoritos
  userFavoriteStudioIds: [] as number[],
  loadingFavorites: false,
  toggleLoading: false,
};

const apiUrl = 'api/favorites';

// Async thunks
export const getFavorites = createAsyncThunk(
  'favorite/fetch_entity_list',
  async () => {
    const requestUrl = `${apiUrl}`;
    return axios.get<IFavorite[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getFavorite = createAsyncThunk(
  'favorite/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IFavorite>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createFavorite = createAsyncThunk(
  'favorite/create_entity',
  async (entity: IFavorite) => {
    const result = await axios.post<IFavorite>(apiUrl, entity);
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateFavorite = createAsyncThunk(
  'favorite/update_entity',
  async (entity: IFavorite) => {
    const result = await axios.put<IFavorite>(`${apiUrl}/${entity.id}`, entity);
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteFavorite = createAsyncThunk(
  'favorite/delete_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete(requestUrl);
    return result;
  },
  { serializeError: serializeAxiosError },
);

// Actions específicas para favoritos de studios
export const toggleStudioFavorite = createAsyncThunk(
  'favorite/toggle_studio_favorite',
  async (studioId: number) => {
    // Primeiro verifica se já é favorito
    const checkUrl = `${apiUrl}/studio/${studioId}/is-favorite`;
    const checkResponse = await axios.get<boolean>(checkUrl);

    if (checkResponse.data) {
      // Remove dos favoritos
      await axios.delete(`${apiUrl}/studio/${studioId}`);
      return { studioId, isFavorite: false };
    } else {
      // Adiciona aos favoritos
      await axios.post(`${apiUrl}/studio/${studioId}`);
      return { studioId, isFavorite: true };
    }
  },
  { serializeError: serializeAxiosError },
);

export const getUserFavoriteStudioIds = createAsyncThunk(
  'favorite/get_user_favorite_studio_ids',
  async () => {
    const requestUrl = `${apiUrl}/my/studio-ids`;
    return axios.get<number[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const checkStudioFavorite = createAsyncThunk(
  'favorite/check_studio_favorite',
  async (studioId: number) => {
    const requestUrl = `${apiUrl}/studio/${studioId}/is-favorite`;
    const response = await axios.get<boolean>(requestUrl);
    return { studioId, isFavorite: response.data };
  },
  { serializeError: serializeAxiosError },
);

// Slice
export const FavoriteSlice = createSlice({
  name: 'favorite',
  initialState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getFavorites.fulfilled, (state, action) => {
        state.loading = false;
        state.entities = action.payload.data;
      })
      .addCase(getFavorite.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(createFavorite.fulfilled, (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addCase(updateFavorite.fulfilled, (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addCase(deleteFavorite.fulfilled, state => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = {} as IFavorite;
      })
      // Estados específicos para favoritos de studios
      .addCase(getUserFavoriteStudioIds.pending, state => {
        state.loadingFavorites = true;
      })
      .addCase(getUserFavoriteStudioIds.fulfilled, (state, action) => {
        state.loadingFavorites = false;
        state.userFavoriteStudioIds = action.payload.data;
      })
      .addCase(getUserFavoriteStudioIds.rejected, state => {
        state.loadingFavorites = false;
        state.userFavoriteStudioIds = [];
      })
      .addCase(toggleStudioFavorite.pending, state => {
        state.toggleLoading = true;
      })
      .addCase(toggleStudioFavorite.fulfilled, (state, action) => {
        state.toggleLoading = false;
        const { studioId, isFavorite } = action.payload;

        if (isFavorite) {
          // Adiciona ao array se não estiver lá
          if (!state.userFavoriteStudioIds.includes(studioId)) {
            state.userFavoriteStudioIds.push(studioId);
          }
        } else {
          // Remove do array
          state.userFavoriteStudioIds = state.userFavoriteStudioIds.filter(id => id !== studioId);
        }
      })
      .addCase(toggleStudioFavorite.rejected, state => {
        state.toggleLoading = false;
      })
      // Estados de loading/error padrão
      .addMatcher(
        action => action.type.endsWith('/pending'),
        (state, action) => {
          if (
            action.type !== 'favorite/get_user_favorite_studio_ids/pending' &&
            action.type !== 'favorite/toggle_studio_favorite/pending'
          ) {
            state.loading = true;
            state.updating = true;
          }
          state.errorMessage = null;
        },
      )
      .addMatcher(isRejected, (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error?.message || 'Erro desconhecido';
      });
  },
});

export const { reset } = FavoriteSlice.actions;

// Reducer
export default FavoriteSlice.reducer;
