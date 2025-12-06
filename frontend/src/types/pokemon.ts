export interface PokemonCard {
  id: string;
  name: string;
  supertype: string;
  subtypes?: string[];
  hp?: string;
  types?: string[];
  evolvesFrom?: string;
  abilities?: Ability[];
  attacks?: Attack[];
  weaknesses?: Weakness[];
  resistances?: Resistance[];
  retreatCost?: string[];
  convertedRetreatCost?: number;
  set: CardSet;
  number: string;
  artist?: string;
  rarity?: string;
  flavorText?: string;
  nationalPokedexNumbers?: number[];
  legalities: Legalities;
  images: CardImages;
  tcgplayer?: TcgPlayer;
  cardmarket?: Cardmarket;
}

export interface Ability {
  name: string;
  text: string;
  type: string;
}

export interface Attack {
  name: string;
  cost: string[];
  convertedEnergyCost: number;
  damage: string;
  text: string;
}

export interface Weakness {
  type: string;
  value: string;
}

export interface Resistance {
  type: string;
  value: string;
}

export interface CardSet {
  id: string;
  name: string;
  series: string;
  printedTotal: number;
  total: number;
  legalities: Legalities;
  ptcgoCode?: string;
  releaseDate: string;
  updatedAt: string;
  images: CardImages;
}

export interface Legalities {
  unlimited?: string;
  standard?: string;
  expanded?: string;
}

export interface CardImages {
  small: string;
  large: string;
}

export interface TcgPlayer {
  url: string;
  updatedAt: string;
  prices: {
    holofoil?: PriceDetail;
    reverseHolofoil?: PriceDetail;
    normal?: PriceDetail;
    '1stEditionHolofoil'?: PriceDetail;
    '1stEditionNormal'?: PriceDetail;
  };
}

export interface PriceDetail {
  low?: number;
  mid?: number;
  high?: number;
  market?: number;
  directLow?: number;
}

export interface Cardmarket {
  url: string;
  updatedAt: string;
  prices: {
    averageSellPrice?: number;
    lowPrice?: number;
    trendPrice?: number;
    [key: string]: number | undefined;
  };
}

export interface PokemonCardResponse {
  data: PokemonCard[];
  page: number;
  pageSize: number;
  count: number;
  totalCount: number;
}

export interface SearchFilters {
  name?: string;
  type?: string;
  rarity?: string;
  setId?: string;
  page?: number;
  pageSize?: number;
}
