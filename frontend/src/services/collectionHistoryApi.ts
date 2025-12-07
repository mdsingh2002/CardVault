import api from './api';

export interface CollectionValueSnapshot {
  totalValue: number;
  totalCards: number;
  uniqueCards: number;
  recordedAt: string;
}

export const collectionHistoryApi = {
  // Get all collection value history
  getHistory: async (): Promise<CollectionValueSnapshot[]> => {
    const response = await api.get<CollectionValueSnapshot[]>('/collection-history');
    return response.data;
  },

  // Get collection value history for the last N days
  getHistoryForLastDays: async (days: number): Promise<CollectionValueSnapshot[]> => {
    const response = await api.get<CollectionValueSnapshot[]>(`/collection-history/last-days/${days}`);
    return response.data;
  },

  // Record a snapshot of the current collection value
  recordSnapshot: async (): Promise<CollectionValueSnapshot> => {
    const response = await api.post<CollectionValueSnapshot>('/collection-history/snapshot');
    return response.data;
  },
};

export default collectionHistoryApi;
