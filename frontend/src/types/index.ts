// User Types
export interface User {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

// Card Types
export interface Card {
  id: string;
  apiId: string;
  name: string;
  setName: string;
  setSeries: string;
  cardNumber: string;
  rarity: string;
  cardType: string;
  supertype: string;
  subtypes: string;
  hp: number;
  artist: string;
  imageUrl: string;
  smallImageUrl: string;
  marketPrice: number;
  releaseDate: string;
  createdAt: string;
  updatedAt: string;
}

export interface CardRequest {
  apiId: string;
  name: string;
  setName: string;
  setSeries?: string;
  cardNumber?: string;
  rarity?: string;
  cardType?: string;
  supertype?: string;
  subtypes?: string;
  hp?: number;
  artist?: string;
  imageUrl?: string;
  smallImageUrl?: string;
  marketPrice?: number;
  releaseDate?: string;
}

// User Card Types
export interface UserCard {
  id: string;
  userId: string;
  card: Card;
  quantity: number;
  condition: string;
  purchasePrice: number;
  currentValue: number;
  totalValue: number;
  acquisitionDate: string;
  notes?: string;
  isGraded: boolean;
  gradeValue?: string;
  gradingCompany?: string;
  createdAt: string;
  updatedAt: string;
}

export interface UserCardRequest {
  cardId: string;
  quantity: number;
  condition: string;
  purchasePrice?: number;
  acquisitionDate?: string;
  notes?: string;
  isGraded?: boolean;
  gradeValue?: string;
  gradingCompany?: string;
}

// Wishlist Types
export interface Wishlist {
  id: string;
  userId: string;
  card: Card;
  priority: number;
  maxPrice?: number;
  notes?: string;
  createdAt: string;
}

export interface WishlistRequest {
  cardId: string;
  priority?: number;
  maxPrice?: number;
  notes?: string;
}

// Achievement Types
export interface Achievement {
  id: number;
  name: string;
  description: string;
  icon: string;
  criteria: string;
  points: number;
  createdAt: string;
}

export interface UserAchievement {
  id: string;
  userId: string;
  achievement: Achievement;
  unlockedAt: string;
}

// Analytics Types
export interface CollectionSummary {
  totalCards: number;
  uniqueCards: number;
  totalValue: number;
  totalInvestment: number;
  profitLoss: number;
  profitLossPercentage: number;
}

export interface RarityDistribution {
  rarity: string;
  count: number;
  percentage: number;
}

// API Response wrapper
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

// Pagination
export interface PageRequest {
  page: number;
  size: number;
  sort?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  size: number;
}
