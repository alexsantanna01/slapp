import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { EntityState, IQueryParams, IQueryParamsStudio, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IStudio, defaultValue } from 'app/shared/model/studio.model';

// Interface estendida para suportar load more
interface StudioEntityState extends EntityState<IStudio> {
  currentPage: number;
  hasMore: boolean;
  loadingMore: boolean;
  pageSize: number;
}

const initialState: StudioEntityState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  currentPage: 0,
  hasMore: true,
  loadingMore: false,
  pageSize: 6,
};

const apiUrl = 'api/studios';

// Actions

export const getEntities = createAsyncThunk(
  'studio/fetch_entity_list',
  async ({ page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<IStudio[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'studio/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IStudio>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'studio/create_entity',
  async (entity: IStudio, thunkAPI) => {
    const result = await axios.post<IStudio>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'studio/update_entity',
  async (entity: IStudio, thunkAPI) => {
    const result = await axios.put<IStudio>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'studio/partial_update_entity',
  async (entity: IStudio, thunkAPI) => {
    const result = await axios.patch<IStudio>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'studio/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IStudio>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const getStudioPagination = createAsyncThunk(
  'studio/fetch_entity_list',
  async ({ page, size, sort, filters }: IQueryParamsStudio) => {
    const params = new URLSearchParams();

    params.append('page', '0');
    params.append('size', '6'); // Primeira carga: 6 itens
    params.append('sort', sort);

    if (page !== undefined) params.append('page', page.toString());
    if (size !== undefined) params.append('size', size.toString());
    if (filters.name && filters.name.trim()) params.append('name', filters.name.trim());
    if (filters.city && filters.city.trim()) params.append('city', filters.city.trim());
    if (filters.roomType && filters.roomType.trim()) params.append('roomType', filters.roomType.trim());
    if (filters.minPrice !== undefined && filters.minPrice >= 0) params.append('minPrice', filters.minPrice.toString());
    if (filters.maxPrice !== undefined && filters.maxPrice >= 0) params.append('maxPrice', filters.maxPrice.toString());

    params.append('cacheBuster', new Date().getTime().toString());

    const requestUrl = `${apiUrl}/pagination?${params.toString()}`;
    return axios.get<IStudio[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

// Nova action para carregar mais
export const loadMoreStudios = createAsyncThunk(
  'studio/load_more_studios',
  async ({ filters, currentPage, sort = 'name,asc' }: { filters: any; currentPage: number; sort?: string }) => {
    const params = new URLSearchParams();

    const nextPage = currentPage + 1;
    params.append('page', nextPage.toString());
    params.append('size', '6'); // Carrega mais 6 itens
    params.append('sort', sort);

    if (filters.name && filters.name.trim()) params.append('name', filters.name.trim());
    if (filters.city && filters.city.trim()) params.append('city', filters.city.trim());
    if (filters.roomType && filters.roomType.trim()) params.append('roomType', filters.roomType.trim());
    if (filters.minPrice !== undefined && filters.minPrice >= 0) params.append('minPrice', filters.minPrice.toString());
    if (filters.maxPrice !== undefined && filters.maxPrice >= 0) params.append('maxPrice', filters.maxPrice.toString());

    params.append('cacheBuster', new Date().getTime().toString());

    const requestUrl = `${apiUrl}/pagination?${params.toString()}`;
    const response = await axios.get<IStudio[]>(requestUrl);

    return {
      ...response,
      nextPage,
    };
  },
  { serializeError: serializeAxiosError },
);

// slice

export const StudioSlice = createSlice({
  name: 'studio',
  initialState,
  reducers: {
    reset(state) {
      return {
        ...initialState,
      };
    },
    resetPagination(state) {
      state.entities = [];
      state.currentPage = 0;
      state.hasMore = true;
      state.loadingMore = false;
    },
  },
  extraReducers(builder) {
    builder
      // Load more - adiciona à lista existente
      .addCase(loadMoreStudios.fulfilled, (state, action) => {
        const { data, headers, nextPage } = action.payload;
        const totalItems = parseInt(headers['x-total-count'], 10);

        return {
          ...state,
          loadingMore: false,
          entities: [...state.entities, ...data],
          currentPage: nextPage,
          hasMore: state.entities.length + data.length < totalItems,
        };
      })
      .addCase(loadMoreStudios.pending, state => {
        state.loadingMore = true;
        state.errorMessage = null;
      })
      .addCase(loadMoreStudios.rejected, (state, action) => {
        state.loadingMore = false;
        state.errorMessage = action.error.message;
      })
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset, resetPagination } = StudioSlice.actions;

// Reducer
export default StudioSlice.reducer;
