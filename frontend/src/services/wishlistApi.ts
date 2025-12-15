import api from './api';

export interface AddToWishlistRequest {
  cardApiId: string;
  priority?: number;
  maxPrice?: number;
  notes?: string;
}

export interface WishlistItem {
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
  priority: number;
  maxPrice?: number;
  notes?: string;
  createdAt: string;
}

export const wishlistApi = {
  // Get user's wishlist
  getWishlist: async (): Promise<WishlistItem[]> => {
    const response = await api.get<WishlistItem[]>('/wishlist');
    return response.data;
  },

  // Get specific wishlist item
  getWishlistItem: async (id: string): Promise<WishlistItem> => {
    const response = await api.get<WishlistItem>(`/wishlist/${id}`);
    return response.data;
  },

  // Add card to wishlist
  addToWishlist: async (request: AddToWishlistRequest): Promise<WishlistItem> => {
    const response = await api.post<WishlistItem>('/wishlist', request);
    return response.data;
  },

  updatePriority: async (id: string, priority: number): Promise<WishlistItem> => {
    const response = await api.patch<WishlistItem>(`/wishlist/${id}/priority`, null, {
      params: { priority }
    });
    return response.data;
  },

  updateWishlistItem: async (id: string, request: Partial<AddToWishlistRequest>): Promise<WishlistItem> => {
    const response = await api.put<WishlistItem>(`/wishlist/${id}`, request);
    return response.data;
  },

  getWishlistCount: async (): Promise<number> => {
    const response = await api.get<number>('/wishlist/count');
    return response.data;
  },

  // Remove card from wishlist
  removeFromWishlist: async (id: string): Promise<void> => {
    await api.delete(`/wishlist/${id}`);
  },

  // Get high priority items
  getHighPriorityItems: async (minPriority = 3): Promise<WishlistItem[]> => {
    const response = await api.get<WishlistItem[]>('/wishlist/high-priority', {
      params: { minPriority }
    });
    return response.data;
  },
};

export default wishlistApi;
