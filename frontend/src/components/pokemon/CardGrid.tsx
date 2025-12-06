import type { PokemonCard } from '@/types/pokemon';
import PokemonCardItem from './PokemonCardItem';

interface CardGridProps {
  cards: PokemonCard[];
}

export default function CardGrid({ cards }: CardGridProps) {
  return (
    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
      {cards.map((card) => (
        <PokemonCardItem key={card.id} card={card} />
      ))}
    </div>
  );
}
