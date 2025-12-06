import api from './api';

export interface AddToCollectionRequest {
  cardApiId: string;
  quantity?: number;
  conditionId?: string;
  purchasePrice?: number;
  notes?: string;
  isGraded?: boolean;
  gradeValue?: string;
  gradingCompany?: string;
}

export interface UserCard {
  id: string;
  userId: string;
  card: {
    id: string;
    apiId: string;
    name: string;
    setName?: string;
    cardNumber?: string;
    rarity?: string;
    imageUrl?: string;
    smallImageUrl?: string;
    marketPrice?: number;
  };
  quantity: number;
  condition?: string;
  purchasePrice?: number;
  currentValue?: number;
  acquisitionDate?: string;
  notes?: string;
  isGraded?: boolean;
  gradeValue?: string;
  gradingCompany?: string;
  createdAt: string;
  updatedAt: string;
}

export const collectionApi = {
  // Get user's collection
  getCollection: async (): Promise<UserCard[]> => {
    const response = await api.get<UserCard[]>('/collection');
    return response.data;
  },

  // Get specific card from collection
  getUserCard: async (id: string): Promise<UserCard> => {
    const response = await api.get<UserCard>(`/collection/${id}`);
    return response.data;
  },

  // Add card to collection
  addToCollection: async (request: AddToCollectionRequest): Promise<UserCard> => {
    const response = await api.post<UserCard>('/collection', request);
    return response.data;
  },

  // Update card in collection
  updateUserCard: async (id: string, request: AddToCollectionRequest): Promise<UserCard> => {
    const response = await api.put<UserCard>(`/collection/${id}`, request);
    return response.data;
  },

  // Remove card from collection
  removeFromCollection: async (id: string): Promise<void> => {
    await api.delete(`/collection/${id}`);
  },
};

export default collectionApi;
