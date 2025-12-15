import { useState } from 'react';
import { toast } from 'sonner';
import type { PokemonCard } from '@/types/pokemon';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import collectionApi from '@/services/collectionApi';
import wishlistApi from '@/services/wishlistApi';

interface PokemonCardItemProps {
  card: PokemonCard;
}

export default function PokemonCardItem({ card }: PokemonCardItemProps) {
  const [loading, setLoading] = useState(false);

  const handleAddToCollection = async () => {
    setLoading(true);
    const toastId = toast.loading('Adding card to collection...');

    try {
      await collectionApi.addToCollection({
        cardApiId: card.id,
        quantity: 1,
      });

      toast.success('Card added to collection!', {
        id: toastId,
        description: `${card.name} - ${card.set.name}`,
        duration: 3000,
      });
    } catch (error: any) {
      console.error('Failed to add card to collection:', error);
      console.error('Error response:', error.response?.data);
      const errorMsg = error.response?.data?.message || error.message || 'Failed to add card to collection';

      toast.error('Failed to add card', {
        id: toastId,
        description: errorMsg,
        duration: 4000,
      });
    } finally {
      setLoading(false);
    }
  };

  const handleAddToWishlist = async () => {
    setLoading(true);
    const toastId = toast.loading('Adding card to wishlist...');

    try {
      await wishlistApi.addToWishlist({
        cardApiId: card.id,
        priority: 3,
      });

      toast.success('Card added to wishlist!', {
        id: toastId,
        description: `${card.name} - ${card.set.name}`,
        duration: 3000,
      });
    } catch (error: any) {
      console.error('Failed to add card to wishlist:', error);
      const errorMsg = error.response?.data?.message || error.message || 'Failed to add card to wishlist';

      toast.error('Failed to add to wishlist', {
        id: toastId,
        description: errorMsg,
        duration: 4000,
      });
    } finally {
      setLoading(false);
    }
  };

  const getMarketPrice = () => {
    if (!card.tcgplayer?.prices) return null;

    const prices = card.tcgplayer.prices;
    if (prices.holofoil?.market) return prices.holofoil.market;
    if (prices.reverseHolofoil?.market) return prices.reverseHolofoil.market;
    if (prices.normal?.market) return prices.normal.market;
    return null;
  };

  const marketPrice = getMarketPrice();

  return (
    <Card className="overflow-hidden hover:shadow-lg transition-shadow">
      <div className="aspect-[2/3] overflow-hidden bg-muted">
        <img
          src={card.images.small}
          alt={card.name}
          className="w-full h-full object-contain"
        />
      </div>
      <CardHeader className="p-4">
        <CardTitle className="text-sm line-clamp-1">{card.name}</CardTitle>
        <CardDescription className="text-xs">
          {card.set.name} • {card.number}/{card.set.printedTotal}
        </CardDescription>
        {marketPrice && (
          <p className="text-sm font-semibold text-primary">
            ${marketPrice.toFixed(2)}
          </p>
        )}
      </CardHeader>
      <CardContent className="p-4 pt-0 space-y-2">
        <Button
          size="sm"
          className="w-full"
          onClick={handleAddToCollection}
          disabled={loading}
        >
          Add to Collection
        </Button>
        <Button
          size="sm"
          variant="outline"
          className="w-full"
          onClick={handleAddToWishlist}
          disabled={loading}
        >
          Add to Wishlist
        </Button>
      </CardContent>
    </Card>
  );
}
