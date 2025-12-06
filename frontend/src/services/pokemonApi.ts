import api from './api';
import type { PokemonCard, PokemonCardResponse, SearchFilters } from '@/types/pokemon';

export const pokemonApi = {
  // Search cards with filters
  searchCards: async (filters: SearchFilters = {}): Promise<PokemonCardResponse> => {
    const params = new URLSearchParams();

    if (filters.name) {
      params.append('q', `name:"${filters.name}*"`);
    }
    if (filters.type) {
      params.append('q', `types:${filters.type}`);
    }
    if (filters.rarity) {
      params.append('q', `rarity:"${filters.rarity}"`);
    }
    if (filters.setId) {
      params.append('q', `set.id:${filters.setId}`);
    }
    if (filters.page) {
      params.append('page', filters.page.toString());
    }
    if (filters.pageSize) {
      params.append('pageSize', filters.pageSize.toString());
    }

    const response = await api.get<PokemonCardResponse>(`/pokemon/cards?${params.toString()}`);
    return response.data;
  },

  // Get card by ID
  getCardById: async (cardId: string): Promise<PokemonCard> => {
    const response = await api.get<PokemonCard>(`/pokemon/cards/${cardId}`);
    return response.data;
  },

  // Search cards by name
  searchByName: async (name: string, page = 1, pageSize = 20): Promise<PokemonCardResponse> => {
    const response = await api.get<PokemonCardResponse>('/pokemon/cards/search/name', {
      params: { name, page, pageSize }
    });
    return response.data;
  },

  // Get cards by set
  getCardsBySet: async (setId: string, page = 1, pageSize = 20): Promise<PokemonCardResponse> => {
    const response = await api.get<PokemonCardResponse>(`/pokemon/cards/set/${setId}`, {
      params: { page, pageSize }
    });
    return response.data;
  },

  // Get cards by type
  getCardsByType: async (type: string, page = 1, pageSize = 20): Promise<PokemonCardResponse> => {
    const response = await api.get<PokemonCardResponse>(`/pokemon/cards/type/${type}`, {
      params: { page, pageSize }
    });
    return response.data;
  },

  // Get cards by rarity
  getCardsByRarity: async (rarity: string, page = 1, pageSize = 20): Promise<PokemonCardResponse> => {
    const response = await api.get<PokemonCardResponse>(`/pokemon/cards/rarity/${rarity}`, {
      params: { page, pageSize }
    });
    return response.data;
  },
};

export default pokemonApi;
